package com.optimisticgirl.controller;

import com.optimisticgirl.config.OtherConfig;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class OtherConfigController {
    @FXML
    private ComboBox requestThreadBox;
    @FXML
    private TextField requestThreadField;

    private Boolean flag = true;

    @FXML
    private void initialize(){
        requestThreadBox.getItems().addAll("1","5","10","15","20","25","30","35","40","45","50","55","60","65","70","75","80","85","90","95","100");
        requestThreadField.setPromptText(String.valueOf(OtherConfig.getRequestThread()));
        showConfig();
    }

    private void showConfig(){
        if (!(OtherConfig.getRequestThread() == 1 || (OtherConfig.getRequestThread() <= 100 && OtherConfig.getRequestThread()%5 == 0))){
            requestThreadBox.getItems().addAll(OtherConfig.getRequestThread());
            requestThreadBox.setValue(OtherConfig.getRequestThread());
        }else {
            requestThreadBox.setValue(OtherConfig.getRequestThread());
        }
    }

    public void executeDetermine(DialogPane dialogPane){
        dialogPane.lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                OtherConfig.setRequestThread(Integer.parseInt(String.valueOf(requestThreadBox.getValue())));
            }
        });
    }

    @FXML
    private void setRequestThread(){
        for (Object item : requestThreadBox.getItems()) {
            if (String.valueOf(requestThreadField.getText()).equals(String.valueOf(item))){
                this.flag = false;
            }
        }
        if (this.flag){
            requestThreadBox.getItems().addAll((Integer.parseInt(this.requestThreadField.getText())));
            requestThreadBox.setValue(this.requestThreadField.getText());
            requestThreadField.setText("");
        }
    }
}
