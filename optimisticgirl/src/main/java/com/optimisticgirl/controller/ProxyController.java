package com.optimisticgirl.controller;

import com.optimisticgirl.config.ProxyConfig;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProxyController {
    @FXML
    private RadioButton enableProxy;
    @FXML
    private RadioButton disableProxy;
    @FXML
    private TextField proxyIp;
    @FXML
    private TextField proxyPort;
    @FXML
    private TextField proxyUsername;
    @FXML
    private TextField proxyPassword;
    @FXML
    private ComboBox proxyType;

    private static ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    private void initialize(){
        proxyType.getItems().addAll("HTTP","SOCKS");
        proxyIp.setPromptText("127.0.0.1");
        proxyPort.setPromptText("8080");
        proxyUsername.setPromptText("username");
        proxyPassword.setPromptText("password");
        this.enableProxy.setToggleGroup(toggleGroup);
        this.disableProxy.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                ProxyConfig.setEnableProxy(toggleGroup.getSelectedToggle().equals(enableProxy));
            }
        });
        showConfig();
    }

    private void showConfig(){
        this.proxyIp.setText(ProxyConfig.getProxyHost());
        if (ProxyConfig.getProxyPort() == -1){
            this.proxyPort.setText("");
        }else {
            this.proxyPort.setText(String.valueOf(ProxyConfig.getProxyPort()));
        }
        this.proxyUsername.setText(ProxyConfig.getProxyUsername());
        this.proxyPassword.setText(ProxyConfig.getProxyPassword());
        if (ProxyConfig.getEnableProxy()){
            toggleGroup.selectToggle(enableProxy);
        }else {
            toggleGroup.selectToggle(disableProxy);
        }
        if (ProxyConfig.getProxyType() == ""){
            proxyType.setValue("HTTP");
        }else {
            proxyType.setValue(ProxyConfig.getProxyType());
        }
    }

    public void executeDetermine(DialogPane dialogPane){
        dialogPane.lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ProxyConfig.setProxyHost(proxyIp.getText());
                if (proxyPort.getText() == ""){
                    ProxyConfig.setProxyPort(Integer.parseInt("0"));
                }else {
                    try {
                        ProxyConfig.setProxyPort(Integer.parseInt(proxyPort.getText()));
                    }catch (Exception e){}
                }
                ProxyConfig.setProxyUsername(proxyUsername.getText());
                ProxyConfig.setProxyPassword(proxyPassword.getText());
                ProxyConfig.setEnableProxy(enableProxy.isSelected());
                ProxyConfig.setProxyType(String.valueOf(proxyType.getValue()));
            }
        });
    }
}
