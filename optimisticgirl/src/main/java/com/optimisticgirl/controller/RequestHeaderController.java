package com.optimisticgirl.controller;

import com.optimisticgirl.config.RequestHeaderConfig;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RequestHeaderController {
    @FXML
    private TextField Key;
    @FXML
    private TextField Value;
    @FXML
    private TextArea requestHeader;

    @FXML
    private void initialize(){
        Key.setPromptText("Cookie");
        Value.setPromptText("a=1");
        requestHeader.setPromptText("Cookie: a=1; b=2");
        showConfig();
    }

    private void showConfig(){
        this.requestHeader.setText(RequestHeaderConfig.getRequestHeader());
    }

    public void executeDetermine(DialogPane dialogPane){
        dialogPane.lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (requestHeader.getText().endsWith("\n")){
                    RequestHeaderConfig.setRequestHeader(requestHeader.getText());
                }else {
                    RequestHeaderConfig.setRequestHeader(requestHeader.getText() + "\n");
                }
            }
        });
    }

    @FXML
    private void addRequestHeadKeyValue(){
        if (!Key.getText().isEmpty() && !Value.getText().isEmpty()){
            if (requestHeader.getText().endsWith("\n")){
                requestHeader.appendText(Key.getText()+": "+Value.getText()+"\n");
                Key.setText("");
                Value.setText("");
            }else {
                requestHeader.appendText(Key.getText()+": "+Value.getText()+"\n");
                Key.setText("");
                Value.setText("");
            }

        }
        if (!Key.getText().isEmpty() && Value.getText().isEmpty()){
            if (requestHeader.getText().endsWith("\n")){
                requestHeader.appendText(Key.getText()+"\n");
                Key.setText("");
                Value.setText("");
            }else {
                requestHeader.appendText("\n" + Key.getText()+"\n");
                Key.setText("");
                Value.setText("");
            }
        }
    }
}
