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

    private CPU cpu;

    Sim system = new Sim();

    @FXML
    protected void initialize(){
        cpu = new CPU();

        //Registers setup
        for(int i = 0; i <= 7; i++){
            ((Label)Aregs.getChildren().get(i)).setText(("A" + i + "=" + String.format("%8s",Integer.toHexString(system.getCpu().getA(i))).replace(" ","0")));
            ((Label)Dregs.getChildren().get(i)).setText("D" + i + "=" + getHexWithTrailingZeroes(system.getCpu().getD(i)));
        }

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
                    cpu.getInstructionSet().moveRegToReg(source.getSelectionModel().getSelectedItem().toString(),destination.getSelectionModel().getSelectedItem().toString());
                else if(instruction.getSelectionModel().getSelectedItem().toString() == "ADD")
                    cpu.getInstructionSet().addRegToReg(source.getSelectionModel().getSelectedItem().toString(),destination.getSelectionModel().getSelectedItem().toString());

                int sindex = source.getSelectionModel().getSelectedIndex();
                int dindex = destination.getSelectionModel().getSelectedIndex();
                Label lsindex = (Label)Dregs.getChildren().get(sindex);
                Label ldindex = (Label)Dregs.getChildren().get(dindex);
                lsindex.setText(lsindex.getText().substring(0,3) + getHexWithTrailingZeroes(cpu.getD(sindex)));
                ldindex.setText(ldindex.getText().substring(0,3) + getHexWithTrailingZeroes(cpu.getD(dindex)));

                Instructions.appendText(instruction.getSelectionModel().getSelectedItem().toString() + " " + source.getSelectionModel().getSelectedItem().toString() + "," + destination.getSelectionModel().getSelectedItem().toString() + "\n");
            }
        });
    }

    private String getHexWithTrailingZeroes(Integer value){
        return String.format("%8s",Integer.toHexString(value)).replace(" ","0");
    }

    private String[] getRegsStrings(){
        return new String[]{"D0","D1","D2","D3","D4","D5","D6","D7"};
    }



}
