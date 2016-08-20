package main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import main.core.CPU;
import main.core.Sim;
import main.core.Utils;
import main.compiler.Compiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Controller {

    @FXML
    private TextArea instructions;
    @FXML
    private ComboBox instruction;
    @FXML
    private ComboBox source;
    @FXML
    private ComboBox destination;
    @FXML
    private Button getCommand;
    @FXML
    private ListView<String> AListView;
    @FXML
    private ListView<String> DListView;
    @FXML
    private ListView<String> otherRegsListView;
    @FXML
    private ListView<String> memListView;

    private static int STATUS_REGISTER = 0;
    private static int PROGRAM_COUNTER = 1;

    //Observable lists for the registers and memory ListViews.
    private ObservableList<String> ARegsObservable = FXCollections.observableArrayList();
    private ObservableList<String> DRegsObservable = FXCollections.observableArrayList();
    private ObservableList<String> otherRegsObservable = FXCollections.observableArrayList();
    private ObservableList<String> MemObservable = FXCollections.observableArrayList();


    Sim system = new Sim();
    Compiler compiler = new Compiler(system);

    @FXML
    protected void initialize(){

        //Registers setup
        for(int i = 0; i <= 7; i++){
            String AregI="A" + i + "=" + Utils.getHexWithTrailingZeroes(system.getCpu().getA(i));
            String DregI = "D" + i + "=" + Utils.getHexWithTrailingZeroes(system.getCpu().getD(i));
            ARegsObservable.add(AregI);
            DRegsObservable.add(DregI);
        }

        AListView.setItems(ARegsObservable);
        DListView.setItems(DRegsObservable);
        otherRegsListView.setItems(otherRegsObservable);
        memListView.setItems(MemObservable);

        otherRegsObservable.add(STATUS_REGISTER,"SR = " + Utils.getBinWithTrailingZeroes((int) system.getCpu().getSR()));
        otherRegsObservable.add(PROGRAM_COUNTER,"PC = " + system.getCpu().getPC());

        //Textarea setup
        instructions.setEditable(false);

        //Setting up Memory UI
        updateMemoryUI();
        //TODO: Not sure why scroll to doesn't scroll exactly to 4000 here. Temporary hotfix of adding 6 to it.
        memListView.scrollTo(system.getCpu().getPC()+6);

        //Instruction setup
        instruction.getItems().addAll("MOVE","ADD","SUB","JMP");
        source.getItems().addAll(Utils.getDataRegsStrings());
        destination.getItems().addAll(Utils.getDataRegsStrings());
        source.getItems().add("Custom Operand");
        source.setOnAction(new EventHandlerDialog());


        //Button setup
        getCommand.setOnAction(event -> {
            int oldPC = system.getCpu().getPC();
            compiler.compileInstruction( instruction.getSelectionModel().getSelectedItem().toString() , source.getSelectionModel().getSelectedItem().toString() , destination.getSelectionModel().getSelectedItem().toString());
            //One Von Neumann cycle.
            system.VonNeumann();

            Platform.runLater(() -> {
                refreshUI(oldPC);
                instructions.appendText(Utils.getPCstr(oldPC) + instruction.getSelectionModel().getSelectedItem().toString() + " " + source.getSelectionModel().getSelectedItem().toString() + "," + destination.getSelectionModel().getSelectedItem().toString() + "\n");
            });
        });
    }



    private void refreshUI(int oldPC) {
        //Updating the UI.
        int dindex = destination.getSelectionModel().getSelectedIndex();
        for(int i = 0; i < 7; i++){
            DRegsObservable.set(i,DRegsObservable.get(i).substring(0,3) + Utils.getHexWithTrailingZeroes(system.getCpu().getD(i)));
        }
        otherRegsObservable.set(STATUS_REGISTER,"SR = " + Utils.getBinWithTrailingZeroes((int) system.getCpu().getSR()));
        otherRegsObservable.set(PROGRAM_COUNTER,"PC = " + system.getCpu().getPC());

        updateMemoryUI();
    }

    private void updateMemoryUI(){
        byte [] mem = system.getMemory().getRam();
        for(int i = 0; i < mem.length; i++ )
            MemObservable.add(i,"["+i+"]   " +   Utils.getLeadingZeroesVersion(2,String.valueOf(mem[i])) + "        -->  " + (char)mem[i]);


        Platform.runLater(() -> {
            memListView.getSelectionModel().select(system.getCpu().getPC());
            memListView.getFocusModel().focus(system.getCpu().getPC());
            memListView.scrollTo(system.getCpu().getPC());
        });
    }

    public void loadAssembly(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(instruction.getScene().getWindow());
        if (file != null) {
            try {
                ArrayList<String> decodedInstructions = compiler.loadAssembly(file);
                Platform.runLater(() -> {
                    int tempPC = system.getCpu().getPC();
                    for(String instruction1 : decodedInstructions) {
                        instructions.appendText(Utils.getPCstr(tempPC) + instruction1 + "\n");
                        tempPC = tempPC + 8;
                    }

                    Platform.runLater(this::updateMemoryUI);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void stepOver(ActionEvent actionEvent) {
        int oldPC = system.getCpu().getPC();
        //One Von Neumann cycle.
        system.VonNeumann();

        Platform.runLater(() -> refreshUI(oldPC));
    }

    private class EventHandlerDialog implements javafx.event.EventHandler<ActionEvent> {
        private int oldVal;

        @Override
        public void handle(ActionEvent event) {
            if(source.getSelectionModel().getSelectedIndex() == 8 && source.getSelectionModel().getSelectedIndex() != oldVal){
                TextInputDialog dialog = new TextInputDialog("6");
                dialog.setTitle("Insert immediate number");
                dialog.setHeaderText("Insert the source operand number");
                dialog.setContentText("Please enter the source operand number (MAX = 127. If it exceeds 127 it will be rounded to 127.):");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> source.getItems().set(8,name));
            }

            oldVal = source.getSelectionModel().getSelectedIndex();
        }
    }


}
