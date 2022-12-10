package com.optimisticgirl.controller;

import com.optimisticgirl.tools.RespResult;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ShowReqRespMessageController {

    @FXML
    private TextArea respMessage;

    @FXML
    private void initialize(){
        respMessage.setText(RespResult.resp);
    }

    @FXML
    private void copyRespMessage(){
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(respMessage.getText());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }
}
