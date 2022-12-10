package com.optimisticgirl.controller;

import com.optimisticgirl.config.RequestHeaderConfig;
import com.optimisticgirl.config.RequestTypeConfig;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RequestTypeController {

    @FXML
    private ComboBox requestType;
    @FXML
    private TextArea requestPaths;
    @FXML
    private TextArea requestContents;
    @FXML
    private ComboBox enctype;

    @FXML
    private void initialize(){
        this.requestType.setValue(RequestTypeConfig.getRequestType());
        if (requestType.getValue().equals("GET")){
            this.requestContents.setDisable(true);
        }
        requestType.getItems().addAll("GET","POST","PUT","PATCH","DELETE","HEAD");
        enctype.getItems().addAll("application/x-www-form-urlencoded","multipart/form-data","application/json");
        requestPaths.setPromptText("/index.php\r\n/admin/index.php");
        requestContents.setPromptText("a=1&b=2");
        showConfig();
    }

    private void showConfig(){
        if (!(RequestTypeConfig.getRequestType().equals("POST"))){
            this.enctype.setDisable(true);
        }else {
            this.enctype.setDisable(false);
        }
        this.enctype.setValue(RequestTypeConfig.getEnctype());
        this.requestPaths.setText(RequestTypeConfig.getRequestPaths());
        this.requestContents.setText(RequestTypeConfig.getRequestContents());
    }

    public void executeDetermine(DialogPane dialogPane){
        dialogPane.lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RequestTypeConfig.setRequestType(String.valueOf(requestType.getValue()));
                RequestTypeConfig.setRequestPaths(requestPaths.getText());
                if (requestType.getValue() == "POST"){
                    RequestTypeConfig.setRequestContents(requestContents.getText());
                    RequestTypeConfig.setEnctype(String.valueOf(enctype.getValue()));
                    if (RequestHeaderConfig.requestHeader.contains("Content-Type")){
                        RequestHeaderConfig.requestHeader = RequestHeaderConfig.requestHeader.replace(RequestHeaderConfig.requestHeader.substring(RequestHeaderConfig.requestHeader.indexOf("Content-Type"),RequestHeaderConfig.requestHeader.indexOf("\n",RequestHeaderConfig.requestHeader.indexOf("Content-Type"))),"Content-Type: " + String.valueOf(enctype.getValue()));
                    }else {
                        if (RequestHeaderConfig.requestHeader.length() == 1 && RequestHeaderConfig.requestHeader.contains("\n")){
                            RequestHeaderConfig.requestHeader = "";
                        }
                        RequestHeaderConfig.requestHeader = RequestHeaderConfig.requestHeader + "Content-Type: " + String.valueOf(enctype.getValue()) + "\n";
                    }
                }else {
                    RequestTypeConfig.setEnctype("application/x-www-form-urlencoded");
                    if (requestType.getValue() == "GET"){
                        RequestTypeConfig.setRequestContents("");
                    }else {
                        RequestTypeConfig.setRequestContents(requestContents.getText());
                    }
                    if (RequestHeaderConfig.requestHeader.contains("Content-Type")){
                        RequestHeaderConfig.requestHeader = RequestHeaderConfig.requestHeader.replace(RequestHeaderConfig.requestHeader.substring(RequestHeaderConfig.requestHeader.indexOf("Content-Type"),RequestHeaderConfig.requestHeader.indexOf("\n",RequestHeaderConfig.requestHeader.indexOf("Content-Type")) + 1),"");
                        System.out.println(RequestHeaderConfig.requestHeader + "1");
                    }
                }
            }
        });
    }

    @FXML
    private void changeRequestType() {
        if (requestType.getValue().equals("GET")) {
            this.enctype.setDisable(true);
            this.requestContents.setDisable(true);
        } else {
            this.requestContents.setDisable(false);
            if (requestType.getValue().equals("POST")) {
                this.enctype.setDisable(false);
            }else {
                this.enctype.setDisable(true);
            }
        }
    }
}
