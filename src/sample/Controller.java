package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import sample.core.CPU;
import sample.core.Sim;

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
    private Label SR;
    @FXML
    private ListView<String> AListView;
    @FXML
    private ListView<String> DListView;
    @FXML
    private ListView<String> OtherRegsListView;

    private static int STATUS_REGISTER = 0;

    ObservableList<String> ARegsObservable = FXCollections.observableArrayList();
    ObservableList<String> DRegsObservable = FXCollections.observableArrayList();
    ObservableList<String> otherRegsObservable = FXCollections.observableArrayList();



    Sim system = new Sim();

    @FXML
    protected void initialize(){

        //Registers setup
        for(int i = 0; i <= 7; i++){
            String AregI="A" + i + "=" + getHexWithTrailingZeroes(system.getCpu().getA(i));
            String DregI = "D" + i + "=" + getHexWithTrailingZeroes(system.getCpu().getD(i));
            ARegsObservable.add(AregI);
            DRegsObservable.add(DregI);
        }

        AListView.setItems(ARegsObservable);
        DListView.setItems(DRegsObservable);
        OtherRegsListView.setItems(otherRegsObservable);

        otherRegsObservable.add(STATUS_REGISTER,"SR = " + getBinWithTrailingZeroes((int) system.getCpu().getSR()));

        //Textarea setup
        Instructions.setEditable(false);

        //Instruction setup
        instruction.getItems().add("MOVE");
        instruction.getItems().add("ADD");
        source.getItems().addAll(getRegsStrings());
        destination.getItems().addAll(getRegsStrings());

        //Button setup
        getCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //We put the instruction in memory. The instruction is put in a way that it's always 4 bytes long. So
                //ADD -> 0ADD , MOVE -> MOVE . This little hack helps a lot during the fetch phase.
                String instruction4 = String.format("%4s", instruction.getSelectionModel().getSelectedItem().toString()).replace(" ","0");
                system.getMemory().putInstruction( instruction4 + source.getSelectionModel().getSelectedItem().toString() + destination.getSelectionModel().getSelectedItem().toString());

                //One Von Neumann cycle.
                system.VonNeumann();

                //Updating the UI.
                int dindex = destination.getSelectionModel().getSelectedIndex();
                DRegsObservable.set(dindex,DRegsObservable.get(dindex).substring(0,3) + getHexWithTrailingZeroes(system.getCpu().getD(dindex)));
                otherRegsObservable.set(STATUS_REGISTER,getBinWithTrailingZeroes((int) system.getCpu().getSR()));

                Instructions.appendText(instruction.getSelectionModel().getSelectedItem().toString() + " " + source.getSelectionModel().getSelectedItem().toString() + "," + destination.getSelectionModel().getSelectedItem().toString() + "\n");
            }
        });
    }

    private String getHexWithTrailingZeroes(Integer value){
        return String.format("%8s",Integer.toHexString(value)).replace(" ","0");
    }

    private String getBinWithTrailingZeroes(Integer value){
        return String.format("%16s" , Integer.toBinaryString(value)).replace(" ","0");
    }

    private String[] getRegsStrings(){
        return new String[]{"D0","D1","D2","D3","D4","D5","D6","D7"};
    }



}
