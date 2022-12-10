package com.optimisticgirl.controller;

import com.optimisticgirl.config.TargetsConfig;
import com.optimisticgirl.controller.OptimisticgirlController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TargetsController {

    @FXML
    private TextField targetField;
    @FXML
    private TextArea targetArea;

    @FXML
    private void initialize(){
        targetField.setPromptText("http://127.0.0.1");
        targetArea.setPromptText("http://127.0.0.1\r\nwww.example.com");
        showConfig();
    }

    private void showConfig(){
        targetArea.setText(TargetsConfig.getTargetsArea());
    }

    public void executeDetermine(DialogPane dialogPane){
        dialogPane.lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (targetArea.getText().endsWith("\n")){
                    TargetsConfig.setTargetsArea(targetArea.getText());
                }else {
                    TargetsConfig.setTargetsArea(targetArea.getText() + "\n");
                }
                if (TargetsConfig.getTargetsArea() != "" && !TargetsConfig.getTargetsArea().isEmpty()){
                    OptimisticgirlController.setIsHaveTargets(true);
                }else {
                    OptimisticgirlController.setIsHaveTargets(false);
                }
            }
        });
    }

    @FXML
    private void addTargets(){
        targetArea.appendText(targetField.getText() + "\n");
        targetField.setText("");
    }

    @FXML
    private void cleanTargets(){
        targetArea.setText("");
    }
}
