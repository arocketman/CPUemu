package main;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.core.Sim;
import main.core.Utils;

public class Controller {

    @FXML
    private TextArea Instructions;
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
    private ListView<String> OtherRegsListView;
    @FXML
    private ListView<String> MemListView;

    private static int STATUS_REGISTER = 0;

    //Observable lists for the registers and memory ListViews.
    private ObservableList<String> ARegsObservable = FXCollections.observableArrayList();
    private ObservableList<String> DRegsObservable = FXCollections.observableArrayList();
    private ObservableList<String> otherRegsObservable = FXCollections.observableArrayList();
    private ObservableList<String> MemObservable = FXCollections.observableArrayList();

    Sim system = new Sim();

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
        OtherRegsListView.setItems(otherRegsObservable);
        MemListView.setItems(MemObservable);

        otherRegsObservable.add(STATUS_REGISTER,"SR = " + Utils.getBinWithTrailingZeroes((int) system.getCpu().getSR()));

        //Textarea setup
        Instructions.setEditable(false);

        //Setting up Memory UI
        updateMemoryUI();
        //TODO: Not sure why scroll to doesn't scroll exactly to 4000 here. Temporary hotfix of adding 6 to it.
        MemListView.scrollTo(system.getCpu().getPC()+6);

        //Instruction setup
        instruction.getItems().addAll("MOVE","ADD","SUB");
        source.getItems().addAll(Utils.getRegsStrings());
        destination.getItems().addAll(Utils.getRegsStrings());



        //Button setup
        getCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //We put the instruction in memory. The instruction is put in a way that it's always 4 bytes long. So
                //ADD -> 0ADD , MOVE -> MOVE . This little hack helps a lot during the fetch phase.
                String instruction4 = Utils.getLeadingZeroesVersion(4,instruction.getSelectionModel().getSelectedItem().toString());
                system.getMemory().putInstruction( instruction4 + source.getSelectionModel().getSelectedItem().toString() + destination.getSelectionModel().getSelectedItem().toString());
                //One Von Neumann cycle.
                system.VonNeumann();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //Updating the UI.
                        int dindex = destination.getSelectionModel().getSelectedIndex();
                        DRegsObservable.set(dindex,DRegsObservable.get(dindex).substring(0,3) + Utils.getHexWithTrailingZeroes(system.getCpu().getD(dindex)));
                        otherRegsObservable.set(STATUS_REGISTER,"SR = " + Utils.getBinWithTrailingZeroes((int) system.getCpu().getSR()));
                        Instructions.appendText(instruction.getSelectionModel().getSelectedItem().toString() + " " + source.getSelectionModel().getSelectedItem().toString() + "," + destination.getSelectionModel().getSelectedItem().toString() + "\n");

                        updateMemoryUI();
                    }
                });
            }
        });
    }

    private void updateMemoryUI(){
        byte [] mem = system.getMemory().getRam();
        for(int i = 0; i < mem.length; i++ )
            MemObservable.add(i,"["+i+"]   " +   Utils.getLeadingZeroesVersion(2,String.valueOf(mem[i])) + "        -->  " + (char)mem[i]);
    }

}
