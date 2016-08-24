package main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import main.core.Sim;
import main.core.Utils;
import main.compiler.Compiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Controller {

    @FXML
    private ComboBox instructionComboBox;
    @FXML
    private ComboBox sourceComboBox;
    @FXML
    private ComboBox destinationComboBox;
    @FXML
    private Button getCommandButton;
    @FXML
    private ListView<String> AListView;
    @FXML
    private ListView<String> DListView;
    @FXML
    private ListView<String> otherRegsListView;
    @FXML
    private ListView<String> memListView;
    @FXML
    private ListView<String> instructionsListView;

    private static final int STATUS_REGISTER = 0;
    private static final int PROGRAM_COUNTER = 1;
    private static final int MEM_WRITING_LOCATION = 2;

    //Observable lists for the registers and memory ListViews.
    private ObservableList<String> ARegsObservable = FXCollections.observableArrayList();
    private ObservableList<String> DRegsObservable = FXCollections.observableArrayList();
    private ObservableList<String> otherRegsObservable = FXCollections.observableArrayList();
    private ObservableList<String> MemObservable = FXCollections.observableArrayList();
    private ObservableList<String> instructionsObservable = FXCollections.observableArrayList();


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
        instructionsListView.setItems(instructionsObservable);

        otherRegsObservable.add(STATUS_REGISTER,"SR = " + Utils.getBinWithTrailingZeroes((int) system.getCpu().getSR()));
        otherRegsObservable.add(PROGRAM_COUNTER,"PC = " + system.getCpu().getPC());
        otherRegsObservable.add(MEM_WRITING_LOCATION,"Mem writes to = " + system.getMemory().getCurrentInstructionAddress());

        //Setting up Memory UI
        updateMemoryUI(0,true);
        //TODO: Not sure why scroll to doesn't scroll exactly to 4000 here. Temporary hotfix of adding 6 to it.
        memListView.scrollTo(system.getCpu().getPC()+6);

        //Instruction setup
        instructionComboBox.getItems().addAll("MOVE","ADD","SUB","JMP");
        sourceComboBox.getItems().addAll(Utils.getDataRegsStrings());
        destinationComboBox.getItems().addAll(Utils.getDataRegsStrings());
        sourceComboBox.getItems().add("Custom Operand");
        sourceComboBox.setOnAction(new CustomOperandInsertionHandler());


        //Button setup
        getCommandButton.setOnAction(event -> {
            if(instructionComboBox.getSelectionModel().isEmpty() || sourceComboBox.getSelectionModel().isEmpty() || destinationComboBox.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Please fill all the necessary comboboxes");
                alert.setHeaderText("Instruction, sourceComboBox and destinationComboBox must be selected before issuing the command.");
                alert.showAndWait();
                return;
            }

            int oldMem = system.getMemory().getCurrentInstructionAddress();
            compiler.compileInstruction( instructionComboBox.getSelectionModel().getSelectedItem().toString() , sourceComboBox.getSelectionModel().getSelectedItem().toString() , destinationComboBox.getSelectionModel().getSelectedItem().toString());
            //One Von Neumann cycle.

            Platform.runLater(() -> {
                refreshUI();
                instructionsObservable.add(Utils.getPCstr(oldMem) + instructionComboBox.getSelectionModel().getSelectedItem().toString() + " " + sourceComboBox.getSelectionModel().getSelectedItem().toString() + "," + destinationComboBox.getSelectionModel().getSelectedItem().toString() + "\n");
            });
        });
    }

    private void refreshUI() {
        for(int i = 0; i < 7; i++){
            DRegsObservable.set(i,DRegsObservable.get(i).substring(0,3) + Utils.getHexWithTrailingZeroes(system.getCpu().getD(i)));
        }
        otherRegsObservable.set(STATUS_REGISTER,"SR = " + Utils.getBinWithTrailingZeroes((int) system.getCpu().getSR()));
        otherRegsObservable.set(PROGRAM_COUNTER,"PC = " + system.getCpu().getPC());
        otherRegsObservable.set(MEM_WRITING_LOCATION,"Mem writes to = " + system.getMemory().getCurrentInstructionAddress());
        updateMemoryUI(1,false);
    }

    private void updateMemoryUI(int numberOfChanges , boolean initialize){
        byte [] mem = system.getMemory().getRam();
        int startIndex = 0;
        int finalIndex = mem.length;
        if(!initialize){
            startIndex = system.getMemory().getCurrentInstructionAddress()-8*numberOfChanges;
            finalIndex = startIndex+8*numberOfChanges;
        }

        for(int i = startIndex ; i < finalIndex; i++ ) {
            if(MemObservable.size() <= i)
                MemObservable.add(i, "[" + i + "]   " + Utils.getLeadingZeroesVersion(2, String.valueOf(mem[i])) + "        -->  " + (char) mem[i]);
            else
                MemObservable.set(i, "[" + i + "]   " + Utils.getLeadingZeroesVersion(2, String.valueOf(mem[i])) + "        -->  " + (char) mem[i]);
        }


        Platform.runLater(() -> {
            focusIndexListView(memListView,system.getCpu().getPC());
        });
    }

    private void focusIndexListView(ListView<String> listView , int index){
        listView.getSelectionModel().select(index);
        listView.getFocusModel().focus(index);
        listView.scrollTo(index);
    }

    public void loadAssembly(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(instructionComboBox.getScene().getWindow());
        if (file != null) {
            try {
                ArrayList<String> decodedInstructions = compiler.loadAssembly(file);
                int tempPC = system.getCpu().getPC();
                for(String instruction1 : decodedInstructions) {
                    instructionsObservable.add(Utils.getPCstr(tempPC) + instruction1 + "\n");
                    tempPC = tempPC + 8;
                }

                Platform.runLater(() -> {
                    focusIndexListView(instructionsListView,instructionsListView.getItems().size()-decodedInstructions.size());
                    updateMemoryUI(decodedInstructions.size(), false);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void stepOver(ActionEvent actionEvent) {
        //One Von Neumann cycle.
        system.VonNeumann();

        int instructionToFocus = 0;
        for(int i = 0; i < instructionsObservable.size(); i++){
            if(instructionsObservable.get(i).contains("[" + system.getCpu().getPC() + "]"))
                instructionToFocus = i;
        }

        int finalInstructionToFocus = instructionToFocus;
        Platform.runLater(() -> {
            refreshUI();
            focusIndexListView(instructionsListView, finalInstructionToFocus);
        });
    }

    public void editRegister(ActionEvent actionEvent) {
        Dialog<Pair<String,String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Register dialog");
        VBox vbox = new VBox();
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("PC","Memory Location","D0","D1","D2","D3","D4","D5","D6","D7");
        TextField valueTextField = new TextField();
        ButtonType submitButton = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(submitButton);
        vbox.getChildren().addAll(comboBox,new Label("Value (base 10)"),valueTextField,dialog.getDialogPane().lookupButton(submitButton));
        dialog.getDialogPane().setContent(vbox);
        dialog.setResultConverter(param -> {
            if(param == submitButton && comboBox.getSelectionModel().getSelectedItem() != null && !valueTextField.getText().isEmpty())
                return new Pair<>(comboBox.getSelectionModel().getSelectedItem().toString(),valueTextField.getText());
            return null;
        });
        Optional<Pair<String,String>> result = dialog.showAndWait();
        if(result.isPresent()) {
            system.editRegister(result.get());
            Platform.runLater(() -> refreshUI());
        }
    }


    private class CustomOperandInsertionHandler implements javafx.event.EventHandler<ActionEvent> {
        private int oldVal;

        @Override
        public void handle(ActionEvent event) {
            if(sourceComboBox.getSelectionModel().getSelectedIndex() == 8 && sourceComboBox.getSelectionModel().getSelectedIndex() != oldVal){
                TextInputDialog dialog = new TextInputDialog("6");
                dialog.setTitle("Insert immediate number");
                dialog.setHeaderText("Insert the sourceComboBox operand number");
                dialog.setContentText("Please enter the sourceComboBox operand number (MAX = 127. If it exceeds 127 it will be rounded to 127.):");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> sourceComboBox.getItems().set(8,name));
            }

            oldVal = sourceComboBox.getSelectionModel().getSelectedIndex();
        }
    }


}
