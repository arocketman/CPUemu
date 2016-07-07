package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import sample.core.CPU;
import sample.core.Sim;

public class Controller {

    @FXML
    private VBox Aregs;
    @FXML
    private VBox Dregs;
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

    Sim system = new Sim();

    @FXML
    protected void initialize(){

        //Registers setup
        for(int i = 0; i <= 7; i++){
            ((Label)Aregs.getChildren().get(i)).setText("A" + i + "=" + getHexWithTrailingZeroes(system.getCpu().getA(i)));
            ((Label)Dregs.getChildren().get(i)).setText("D" + i + "=" + getHexWithTrailingZeroes(system.getCpu().getD(i)));
        }

        SR.setText(getBinWithTrailingZeroes((int) system.getCpu().getSR()));
        SR.setMinWidth(120);

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
                if(instruction.getSelectionModel().getSelectedItem().toString() == "MOVE")
                    system.getCpu().getInstructionSet().regToReg("MOVE",source.getSelectionModel().getSelectedItem().toString(),destination.getSelectionModel().getSelectedItem().toString());
                else if(instruction.getSelectionModel().getSelectedItem().toString() == "ADD")
                    system.getCpu().getInstructionSet().regToReg("ADD",source.getSelectionModel().getSelectedItem().toString(),destination.getSelectionModel().getSelectedItem().toString());

                int sindex = source.getSelectionModel().getSelectedIndex();
                int dindex = destination.getSelectionModel().getSelectedIndex();
                Label lsindex = (Label)Dregs.getChildren().get(sindex);
                Label ldindex = (Label)Dregs.getChildren().get(dindex);
                lsindex.setText(lsindex.getText().substring(0,3) + getHexWithTrailingZeroes(system.getCpu().getD(sindex)));
                ldindex.setText(ldindex.getText().substring(0,3) + getHexWithTrailingZeroes(system.getCpu().getD(dindex)));
                SR.setText(getBinWithTrailingZeroes((int) system.getCpu().getSR()));

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
