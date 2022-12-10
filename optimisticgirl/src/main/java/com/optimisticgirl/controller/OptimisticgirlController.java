package com.optimisticgirl.controller;

import com.optimisticgirl.config.OtherConfig;
import com.optimisticgirl.config.RequestTypeConfig;
import com.optimisticgirl.config.TargetsConfig;
import com.optimisticgirl.tools.RespResult;
import com.optimisticgirl.tools.RunTask;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

public class OptimisticgirlController {
    @FXML
    private Button startBut;
    @FXML
    private Button stopBut;
    @FXML
    private Label progressBar;
    @FXML
    private TabPane tabPane;
    @FXML
    private TableView respTable;
    @FXML
    private TableColumn tableNum;
    @FXML
    private TableColumn tableHost;
    @FXML
    private TableColumn tableStatus;
    @FXML
    private TableColumn tableTitle;
    @FXML
    private TableColumn tableServer;
    @FXML
    private TableColumn tableLen;
    @FXML
    private TableColumn tableReadUrl;
    @FXML
    private TableColumn contentType;
    @FXML
    private TableView respTableSearch;
    @FXML
    private TableColumn tableNumSearch;
    @FXML
    private TableColumn tableHostSearch;
    @FXML
    private TableColumn tableStatusSearch;
    @FXML
    private TableColumn tableTitleSearch;
    @FXML
    private TableColumn tableServerSearch;
    @FXML
    private TableColumn tableLenSearch;
    @FXML
    private TableColumn tableReadUrlSearch;
    @FXML
    private TableColumn contentTypeSearch;
    @FXML
    private TextField queryField;

    private static Boolean isHaveTargets = false;
    public static ObservableList<RespResult> respResults;
    public static ObservableList<RespResult> respResultsSearch;

    public OptimisticgirlController() {}

    public void setProgressBar(String process) {
        this.progressBar.setText(process);
    }

    public static Boolean getIsHaveTargets() {
        return isHaveTargets;
    }

    public static void setIsHaveTargets(Boolean isHaveTargets) {
        OptimisticgirlController.isHaveTargets = isHaveTargets;
    }

    @FXML
    private void initialize(){
        tableNum.setComparator(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.parseInt(String.valueOf(o1))- Integer.parseInt(String.valueOf(o2));
            }
        });
        tableLen.setComparator(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.parseInt(String.valueOf(o1))- Integer.parseInt(String.valueOf(o2));
            }
        });
        tableNumSearch.setComparator(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.parseInt(String.valueOf(o1))- Integer.parseInt(String.valueOf(o2));
            }
        });
        tableLenSearch.setComparator(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.parseInt(String.valueOf(o1))- Integer.parseInt(String.valueOf(o2));
            }
        });
        respResults = FXCollections.observableArrayList();
        respResultsSearch = FXCollections.observableArrayList();
        tableNum.setCellValueFactory(new PropertyValueFactory<>("num"));
        tableHost.setCellValueFactory(new PropertyValueFactory<>("host"));
        tableStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableServer.setCellValueFactory(new PropertyValueFactory<>("server"));
        tableLen.setCellValueFactory(new PropertyValueFactory<>("contentLen"));
        tableReadUrl.setCellValueFactory(new PropertyValueFactory<>("realUrl"));
        contentType.setCellValueFactory(new PropertyValueFactory<>("content_Type"));
        respTable.setItems(respResults);
        tableNumSearch.setCellValueFactory(new PropertyValueFactory<>("num"));
        tableHostSearch.setCellValueFactory(new PropertyValueFactory<>("host"));
        tableStatusSearch.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableTitleSearch.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableServerSearch.setCellValueFactory(new PropertyValueFactory<>("server"));
        tableLenSearch.setCellValueFactory(new PropertyValueFactory<>("contentLen"));
        tableReadUrlSearch.setCellValueFactory(new PropertyValueFactory<>("realUrl"));
        contentTypeSearch.setCellValueFactory(new PropertyValueFactory<>("content_Type"));
        respTableSearch.setItems(respResultsSearch);
        startBut.setDisable(true);
        stopBut.setDisable(true);
        TreeItem rootItem = new TreeItem();
        TreeItem treeItem = new TreeItem();
        rootItem.getChildren().add(treeItem);
        TreeView treeView = new TreeView(rootItem);
        respTable.setRowFactory(param -> {
            final TableRow tableRow = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem copyMenu = new MenuItem("复制");
            copyMenu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int index = tableRow.getIndex();
                    String host = OptimisticgirlController.respResults.get(index).getHost();
                    ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.putString(host);
                    Clipboard.getSystemClipboard().setContent(clipboardContent);
                }
            });

            MenuItem deleteMenu = new MenuItem("删除此记录");
            deleteMenu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    OptimisticgirlController.respResults.remove(tableRow.getIndex());
                }
            });

            MenuItem seeBodyMenu = new MenuItem("使用浏览器访问");
            seeBodyMenu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int index = tableRow.getIndex();
                    String host = OptimisticgirlController.respResults.get(index).getHost();
                    URI uri = URI.create(host);
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)){
                        try {
                            desktop.browse(uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            contextMenu.getItems().addAll(copyMenu,deleteMenu,seeBodyMenu);
            tableRow.contextMenuProperty().bind(Bindings.when(tableRow.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
            tableRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2 && (! tableRow.isEmpty())){
                        if (OptimisticgirlController.respResults.get(tableRow.getIndex()).getHeaders() != "" && OptimisticgirlController.respResults.get(tableRow.getIndex()).getRespMessage() != ""){
                            String headers = "";
                            if (OptimisticgirlController.respResults.get(tableRow.getIndex()).getStatus() == "200"){
                                headers = "HTTP/1.1 200 OK" + "\n";
                            }else {
                                headers = "HTTP/1.1 " + OptimisticgirlController.respResults.get(tableRow.getIndex()).getStatus() + "\n";
                            }
                            String headers1 = OptimisticgirlController.respResults.get(tableRow.getIndex()).getHeaders();
                            String[] split = headers1.split("&&");
                            for (int i = 0;i < split.length;i++){
                                headers = headers + split[i].substring(0,split[i].indexOf("=")) + ": " + split[i].substring(split[i].indexOf("=") + 1) + "\n";
                            }
                            RespResult.resp = headers + "\n" + OptimisticgirlController.respResults.get(tableRow.getIndex()).getRespMessage();
                        }else {
                            RespResult.resp = "未获取到响应报文";
                        }
                        Dialog dialog = new Dialog();
                        DialogPane dialogPane = new DialogPane();
                        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/optimisticgirl/fxml/ShowResRespMessage.fxml"));
                        Pane pane = null;
                        try {
                            pane = fxmlLoader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialogPane.setContent(pane);
                        dialogPane.getButtonTypes().addAll((ButtonType[]) new ButtonType[]{ButtonType.OK,ButtonType.CANCEL});
                        dialog.setDialogPane(dialogPane);
                        dialog.setTitle("查看返回包");
                        dialog.showAndWait();
                    }
                }
            });
            return tableRow;
        });

        respTableSearch.setRowFactory(param -> {
            final TableRow tableRow = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem copyMenu = new MenuItem("复制");
            copyMenu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int index = tableRow.getIndex();
                    String host = OptimisticgirlController.respResultsSearch.get(index).getHost();
                    ClipboardContent clipboardContent = new ClipboardContent();
                    clipboardContent.putString(host);
                    Clipboard.getSystemClipboard().setContent(clipboardContent);
                }
            });
            MenuItem deleteMenu = new MenuItem("删除此记录");
            deleteMenu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    OptimisticgirlController.respResultsSearch.remove(tableRow.getIndex());
                }
            });
            MenuItem seeBodyMenu = new MenuItem("使用浏览器访问");
            seeBodyMenu.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int index = tableRow.getIndex();
                    String host = OptimisticgirlController.respResultsSearch.get(index).getHost();
                    URI uri = URI.create(host);
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)){
                        try {
                            desktop.browse(uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            contextMenu.getItems().addAll(deleteMenu,seeBodyMenu);
            tableRow.contextMenuProperty().bind(Bindings.when(tableRow.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
            tableRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2 && (! tableRow.isEmpty())){
                        if (OptimisticgirlController.respResultsSearch.get(tableRow.getIndex()).getHeaders() != "" && OptimisticgirlController.respResultsSearch.get(tableRow.getIndex()).getRespMessage() != ""){
                            String headers = "";
                            if (OptimisticgirlController.respResultsSearch.get(tableRow.getIndex()).getStatus() == "200"){
                                headers = "HTTP/1.1 200 OK" + "\n";
                            }else {
                                headers = "HTTP/1.1 " + OptimisticgirlController.respResultsSearch.get(tableRow.getIndex()).getStatus() + "\n";
                            }
                            String headers1 = OptimisticgirlController.respResultsSearch.get(tableRow.getIndex()).getHeaders();
                            String[] split = headers1.split("&&");
                            for (int i = 0;i < split.length;i++){
                                headers = headers + split[i].substring(0,split[i].indexOf("=")) + ": " + split[i].substring(split[i].indexOf("=") + 1) + "\n";
                            }
                            RespResult.resp = headers + "\n" + OptimisticgirlController.respResultsSearch.get(tableRow.getIndex()).getRespMessage();
                        }else {
                            RespResult.resp = "未获取到响应报文";
                        }
                        Dialog dialog = new Dialog();
                        DialogPane dialogPane = new DialogPane();
                        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/optimisticgirl/fxml/ShowResRespMessage.fxml"));
                        Pane pane = null;
                        try {
                            pane = fxmlLoader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialogPane.setContent(pane);
                        dialogPane.getButtonTypes().addAll((ButtonType[]) new ButtonType[]{ButtonType.OK,ButtonType.CANCEL});
                        dialog.setDialogPane(dialogPane);
                        dialog.setTitle("查看请求包和返回包");
                        dialog.showAndWait();
                    }
                }
            });
            return tableRow;
        });
    }

    @FXML
    private void loadTargetsFromFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("从文件导入目标");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.txt"));
        File selectFile = fileChooser.showOpenDialog(new Stage());
        if (selectFile == null) {
            return;
        }
        String filepath = selectFile.getPath().trim();
        readTxtFile(filepath);
        if (TargetsConfig.getTargetsArea() != ""){
            startBut.setDisable(false);
            isHaveTargets = true;
        }else {
            startBut.setDisable(true);
        }
    }

    @FXML
    private void exportResultIntoCsvFile() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出结果到CSV文件");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files","*.csv"));
        File selectFile = fileChooser.showSaveDialog(new Stage());
        if (selectFile == null) {
            return;
        }
        String filepath = selectFile.getPath().trim();
        CsvWriter csvWriter = new CsvWriter(filepath, ',', Charset.forName("UTF-8"));
        String[] csvHeaders = {"序号","HOST","Status","Title","Server","ContentLength","网站跳转地址","ContentType"};
        csvWriter.writeRecord(csvHeaders);
        String id = tabPane.getSelectionModel().getSelectedItem().getId();
        if (id.contains("tabAll")) {
            if (!OptimisticgirlController.respResults.isEmpty()){
                for (RespResult respResult : OptimisticgirlController.respResults) {
                    try {
                        String[] csvData = {respResult.getNum(),respResult.getHost(),respResult.getStatus(),respResult.getTitle(),respResult.getServer(),respResult.getContentLen(),respResult.getRealUrl(),respResult.getContent_Type()};
                        csvWriter.writeRecord(csvData);
                    }catch (Exception e){}
                }
            }
        }else {
            if (!OptimisticgirlController.respResultsSearch.isEmpty()){
                for (RespResult respResult : OptimisticgirlController.respResultsSearch) {
                    try {
                        String[] csvData = {respResult.getNum(),respResult.getHost(),respResult.getStatus(),respResult.getTitle(),respResult.getServer(),respResult.getContentLen(),respResult.getRealUrl(),respResult.getContent_Type()};
                        csvWriter.writeRecord(csvData);
                    }catch (Exception e){}
                }
            }
        }
        csvWriter.close();
        System.out.println("导出完毕");
    }

    @FXML
    private void exportResultIntoTxtFile() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出结果到TXT文件");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files","*.txt"));
        File selectFile = fileChooser.showSaveDialog(new Stage());
        if (selectFile == null) {
            return;
        }
        String filepath = selectFile.getPath().trim();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), "UTF-8"));
        String id = tabPane.getSelectionModel().getSelectedItem().getId();
        if (id.contains("tabAll")) {
            if (!OptimisticgirlController.respResults.isEmpty()){
                for (RespResult respResult : OptimisticgirlController.respResults) {
                    try {
                        out.write("Num: "+respResult.getNum().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("Host: "+respResult.getHost().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("Status: "+respResult.getStatus().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("Title: "+respResult.getTitle().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("Server: "+respResult.getServer().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("ContentLen: "+respResult.getContentLen().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("RealUrl: "+respResult.getRealUrl().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("RespMessage: "+Base64.getEncoder().encodeToString(respResult.getRespMessage().getBytes("GBK")).replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("ContentType: "+respResult.getContent_Type().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("RespHeaders: "+Base64.getEncoder().encodeToString(respResult.getHeaders().getBytes("GBK")).replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("======================================================================================================================\n");
                        out.flush();
                    }catch (Exception e){}
                }
            }
        }else {
            if (!OptimisticgirlController.respResultsSearch.isEmpty()){
                for (RespResult respResult : OptimisticgirlController.respResultsSearch) {
                    try {
                        out.write("Num: "+respResult.getNum().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("Host: "+respResult.getHost().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("Status: "+respResult.getStatus().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("Title: "+respResult.getTitle().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("Server: "+respResult.getServer().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("ContentLen: "+respResult.getContentLen().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("RealUrl: "+respResult.getRealUrl().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("RespMessage: "+Base64.getEncoder().encodeToString(respResult.getRespMessage().getBytes("GBK")).replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("ContentType: "+respResult.getContent_Type().replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("RespHeaders: "+Base64.getEncoder().encodeToString(respResult.getHeaders().getBytes("GBK")).replaceAll("\n","").replaceAll("\r","").replaceAll("\r\n","").trim() + "\n");
                        out.write("======================================================================================================================\n");
                        out.flush();
                    }catch (Exception e){}
                }
            }
        }
        out.close();
        System.out.println("导出完毕");
    }

    @FXML
    private void loadResultsFromCsvFile() throws Exception{
        ArrayList<String[]> strings = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("从CSV文件导入结果");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files","*.csv"));
        File selectFile = fileChooser.showOpenDialog(new Stage());
        if (selectFile == null) {
            return;
        }

        String filepath = selectFile.getPath().trim();
        CsvReader csvReader = new CsvReader(filepath, ',', Charset.forName("UTF-8"));
        csvReader.readHeaders();
        while (csvReader.readRecord()){
            strings.add(csvReader.getValues());
        }
        csvReader.close();
        for (String[] string : strings) {
            try {
                System.out.println(string[0] + " " + string[1]);
                OptimisticgirlController.respResults.add(new RespResult(new SimpleStringProperty(string[0]),new SimpleStringProperty(string[1]),new SimpleStringProperty(string[2]),new SimpleStringProperty(string[3]),new SimpleStringProperty(string[4]),new SimpleStringProperty(string[5]),new SimpleStringProperty(string[6]),new SimpleStringProperty(""),new SimpleStringProperty(""),new SimpleStringProperty("")));
            }catch (Exception e){}
        }
    }

    @FXML
    private void loadResultsFromTxtFile() throws Exception{
        ArrayList<String[]> strings = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("从TXT文件导入结果");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT Files","*.txt"));
        File selectFile = fileChooser.showOpenDialog(new Stage());
        if (selectFile == null) {
            return;
        }
        String filepath = selectFile.getPath().trim();
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "UTF-8"));
        String str = "";
        String[] temStrings = new String[11];
        String[] temStrings02 = new String[11];
        int i = 0;
        while ((str = in.readLine()) != null){
            try {
                if (!str.contains("======================================================================================================================")){
                    if (str.contains("RespMessage: ") || str.contains("RespHeaders: ") || str.contains("ReqPacket: ")){
                        temStrings[i] = new String(Base64.getDecoder().decode(str.substring(str.indexOf(": ") + 2)));
                    }else {
                        temStrings[i] = str.substring(str.indexOf(": ") + 2);
                    }
                    i = i + 1;
                }else {
                    i = 0;
                    System.out.println(temStrings[0] + " " + temStrings[1]);
                    OptimisticgirlController.respResults.add(new RespResult(new SimpleStringProperty(temStrings[0]),new SimpleStringProperty(temStrings[1]),new SimpleStringProperty(temStrings[2]),new SimpleStringProperty(temStrings[3]),new SimpleStringProperty(temStrings[4]),new SimpleStringProperty(temStrings[5]),new SimpleStringProperty(temStrings[6]),new SimpleStringProperty(temStrings[7]),new SimpleStringProperty(temStrings[8]),new SimpleStringProperty(temStrings[9])));
                    temStrings = temStrings02;
                }
            }catch (Exception e){}
        }
        in.close();
    }

    @FXML
    private void setHttpProxy() throws IOException {
        Dialog dialog = new Dialog();
        DialogPane dialogPane = new DialogPane();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/optimisticgirl/fxml/Proxy.fxml"));
        Pane pane = fxmlLoader.load();
        dialogPane.setContent(pane);
        dialogPane.getButtonTypes().addAll((ButtonType[]) new ButtonType[]{ButtonType.OK,ButtonType.CANCEL});
        ProxyController proxyController = fxmlLoader.getController();
        proxyController.executeDetermine(dialogPane);
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("配置代理");
        dialog.showAndWait();
    }

    @FXML
    private void configRequestType() throws IOException{
        Dialog dialog = new Dialog();
        DialogPane dialogPane = new DialogPane();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/optimisticgirl/fxml/RequestType.fxml"));
        Pane pane = fxmlLoader.load();
        dialogPane.setContent(pane);
        dialogPane.getButtonTypes().addAll((ButtonType[]) new ButtonType[]{ButtonType.OK,ButtonType.CANCEL});
        RequestTypeController requestTypeController = fxmlLoader.getController();
        requestTypeController.executeDetermine(dialogPane);
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("配置HTTP请求方式");
        dialog.showAndWait();
    }

    @FXML
    private void configRequestHeader() throws IOException{
        Dialog dialog = new Dialog();
        DialogPane dialogPane = new DialogPane();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/optimisticgirl/fxml/RequestHeader.fxml"));
        Pane pane = fxmlLoader.load();
        dialogPane.setContent(pane);
        dialogPane.getButtonTypes().addAll((ButtonType[]) new ButtonType[]{ButtonType.OK,ButtonType.CANCEL});
        RequestHeaderController requestHeaderController = fxmlLoader.getController();
        requestHeaderController.executeDetermine(dialogPane);
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("配置HTTP请求头");
        dialog.showAndWait();
    }

    @FXML
    private void configTargets() throws IOException{
        Dialog dialog = new Dialog();
        DialogPane dialogPane = new DialogPane();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/optimisticgirl/fxml/Targets.fxml"));
        Pane pane = fxmlLoader.load();
        dialogPane.setContent(pane);
        dialogPane.getButtonTypes().addAll((ButtonType[]) new ButtonType[]{ButtonType.OK,ButtonType.CANCEL});
        TargetsController targetsController = fxmlLoader.getController();
        targetsController.executeDetermine(dialogPane);
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("配置Targets");
        dialog.showAndWait();
        if (getIsHaveTargets()){
            startBut.setDisable(false);
        }else {
            startBut.setDisable(true);
        }
    }

    @FXML
    private void setOtherConfig() throws IOException{
        Dialog dialog = new Dialog();
        DialogPane dialogPane = new DialogPane();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/com/optimisticgirl/fxml/OtherConfig.fxml"));
        Pane pane = fxmlLoader.load();
        dialogPane.setContent(pane);
        dialogPane.getButtonTypes().addAll((ButtonType[]) new ButtonType[]{ButtonType.OK,ButtonType.CANCEL});
        OtherConfigController otherConfigController = fxmlLoader.getController();
        otherConfigController.executeDetermine(dialogPane);
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("其它配置");
        dialog.showAndWait();
    }

    @FXML
    private void queryResp(){
        if (queryField.getText().contains("&&")){
            String[] queryKVS = queryField.getText().split("&&");
            for (RespResult respResult : OptimisticgirlController.respResults) {
                try {
                    Boolean flag = false;
                    for (String queryKV : queryKVS) {
                        if (queryKV.contains("statuscode")){
                            if (!respResult.getStatus().contains("Connection")){
                                if (queryKV.contains("=") && !queryKV.contains(">") && !queryKV.contains("<") && !queryKV.contains("!=")){
                                    String value = queryKV.substring(queryKV.indexOf("=")+1).trim();
                                    value = value.substring(1,value.length() - 1);
                                    if (Integer.parseInt(respResult.getStatus()) == Integer.parseInt(value)){
                                        flag = true;
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }else if (queryKV.contains("!=")){
                                    String value = queryKV.substring(queryKV.indexOf("!=")+2).trim();
                                    value = value.substring(1,value.length() - 1);
                                    if (Integer.parseInt(respResult.getStatus()) != Integer.parseInt(value)){
                                        flag = true;
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }else if (queryKV.contains(">") && !queryKV.contains("=")){
                                    String value = queryKV.substring(queryKV.indexOf(">")+1).trim();
                                    value = value.substring(1,value.length() - 1);
                                    if (Integer.parseInt(respResult.getStatus()) > Integer.parseInt(value)){
                                        flag = true;
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }else if (queryKV.contains(">") && queryKV.contains("=")){
                                    String value = queryKV.substring(queryKV.indexOf(">=")+2).trim();
                                    value = value.substring(1,value.length() - 1);
                                    if (Integer.parseInt(respResult.getStatus()) >= Integer.parseInt(value)){
                                        flag = true;
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }else if (queryKV.contains("<") && !queryKV.contains("=")){
                                    String value = queryKV.substring(queryKV.indexOf("<")+1).trim();
                                    value = value.substring(1,value.length() - 1);
                                    if (Integer.parseInt(respResult.getStatus()) < Integer.parseInt(value)){
                                        flag = true;
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }else if (queryKV.contains("<") && queryKV.contains("=")){
                                    String value = queryKV.substring(queryKV.indexOf("<=")+2).trim();
                                    value = value.substring(1,value.length() - 1);
                                    if (Integer.parseInt(respResult.getStatus()) <= Integer.parseInt(value)){
                                        flag = true;
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }
                            }
                        }else if (queryKV.contains("content-length")){
                            if (respResult.getContentLen().trim().isEmpty() || respResult.getContentLen().contains("Content")){
                                flag = false;
                                break;
                            }else {
                                if (queryKV.contains("=") && !queryKV.contains(">") && !queryKV.contains("<") && !queryKV.contains("!=")){
                                    String value = queryKV.substring(queryKV.indexOf("=")+1).trim();
                                    value = value.substring(1,value.length() - 1);
                                    if (Integer.parseInt(respResult.getContentLen()) == Integer.parseInt(value)){
                                        flag = true;
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }else if (queryKV.contains("!=")){
                                    String value = queryKV.substring(queryKV.indexOf("!=")+2).trim();
                                    value = value.substring(1,value.length() - 1);
                                    if (Integer.parseInt(respResult.getContentLen()) != Integer.parseInt(value)){
                                        flag = true;
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }else if (queryKV.contains(">") && !queryKV.contains("=")){
                                    String value = queryKV.substring(queryKV.indexOf(">")+1).trim();
                                    value = value.substring(1,value.length() - 1);
                                    if (Integer.parseInt(respResult.getContentLen()) > Integer.parseInt(value)){
                                        flag = true;
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }else if (queryKV.contains(">") && queryKV.contains("=")){
                                    String value = queryKV.substring(queryKV.indexOf(">=")+2).trim();
                                    value = value.substring(1,value.length() - 1);
                                    if (Integer.parseInt(respResult.getContentLen()) >= Integer.parseInt(value)){
                                        flag = true;
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }else if (queryKV.contains("<") && !queryKV.contains("=")){
                                    String value = queryKV.substring(queryKV.indexOf("<")+1).trim();
                                    value = value.substring(1,value.length() - 1);
                                    if (Integer.parseInt(respResult.getContentLen()) < Integer.parseInt(value)){
                                        flag = true;
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }else if (queryKV.contains("<") && queryKV.contains("=")){
                                    String value = queryKV.substring(queryKV.indexOf("<=")+2).trim();
                                    value = value.substring(1,value.length() - 1);
                                    if (Integer.parseInt(respResult.getContentLen()) <= Integer.parseInt(value)){
                                        flag = true;
                                    }else {
                                        flag = false;
                                        break;
                                    }
                                }
                            }
                        }else if (queryKV.contains("content-type.contain") && !queryKV.contains("!=")){
                            String value = queryKV.substring(queryKV.indexOf("=")+1).trim();
                            value = value.substring(1,value.length() - 1);
                            if (respResult.getContent_Type().contains(value)){
                                flag = true;
                            }else {
                                flag = false;
                                break;
                            }
                        }else if (queryKV.contains("content-type.contain") && queryKV.contains("!=")){
                            String value = queryKV.substring(queryKV.indexOf("!=")+2).trim();
                            value = value.substring(1,value.length() - 1);
                            if (!respResult.getContent_Type().contains(value)){
                                flag = true;
                            }else {
                                flag = false;
                                break;
                            }
                        }else if (queryKV.contains("location.contain") && !queryKV.contains("!=")){
                            String value = queryKV.substring(queryKV.indexOf("=")+1).trim();
                            value = value.substring(1,value.length() - 1);
                            if (respResult.getRealUrl().contains(value)){
                                flag = true;
                            }else {
                                flag = false;
                                break;
                            }
                        }else if (queryKV.contains("location.contain") && queryKV.contains("!=")){
                            String value = queryKV.substring(queryKV.indexOf("!=")+2).trim();
                            value = value.substring(1,value.length() - 1);
                            if (!respResult.getRealUrl().contains(value)){
                                flag = true;
                            }else {
                                flag = false;
                                break;
                            }
                        }else if (queryKV.contains("header.contain") && !queryKV.contains("!=")){
                            String value = queryKV.substring(queryKV.indexOf("=")+1).trim();
                            value = value.substring(1,value.length() - 1);
                            if (respResult.getHeaders().contains(value)){
                                flag = true;
                            }else {
                                flag = false;
                                break;
                            }
                        }else if (queryKV.contains("header.contain") && queryKV.contains("!=")){
                            String value = queryKV.substring(queryKV.indexOf("!=")+2).trim();
                            value = value.substring(1,value.length() - 1);
                            if (!respResult.getHeaders().contains(value)){
                                flag = true;
                            }else {
                                flag = false;
                                break;
                            }
                        }else if (queryKV.contains("body.contain") && !queryKV.contains("!=")){
                            String value = queryKV.substring(queryKV.indexOf("=")+1).trim();
                            value = value.substring(1,value.length() - 1);
                            if (respResult.getRespMessage().contains(value)){
                                flag = true;
                            }else {
                                flag = false;
                                break;
                            }
                        }else if (queryKV.contains("body.contain") && queryKV.contains("!=")){
                            String value = queryKV.substring(queryKV.indexOf("!=")+2).trim();
                            value = value.substring(1,value.length() - 1);
                            if (!respResult.getRespMessage().contains(value)){
                                flag = true;
                            }else {
                                flag = false;
                                break;
                            }
                        }
                    }
                    if (flag){
                        System.out.println(respResult.getNum() + " " + respResult.getHost());
                        respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                    }
                }catch (Exception e){

                }
            }
        }else {
            for (RespResult respResult : OptimisticgirlController.respResults) {
                try {
                    if (queryField.getText().contains("statuscode")){
                        if (!respResult.getStatus().contains("Connection")){
                            if (queryField.getText().contains("=") && !queryField.getText().contains(">") && !queryField.getText().contains("<") && !queryField.getText().contains("!=")){
                                String value = queryField.getText().substring(queryField.getText().indexOf("=")+1).trim();
                                value = value.substring(1,value.length() - 1);
                                if (Integer.parseInt(respResult.getStatus()) == Integer.parseInt(value)){
                                    System.out.println(respResult.getNum() + " " + respResult.getHost());
                                    respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                                }
                            }else if (queryField.getText().contains("!=")){
                                String value = queryField.getText().substring(queryField.getText().indexOf("!=")+2).trim();
                                value = value.substring(1,value.length() - 1);
                                if (Integer.parseInt(respResult.getStatus()) != Integer.parseInt(value)){
                                    System.out.println(respResult.getNum() + " " + respResult.getHost());
                                    respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                                }
                            }else if (queryField.getText().contains(">") && !queryField.getText().contains("=")){
                                String value = queryField.getText().substring(queryField.getText().indexOf(">")+1).trim();
                                value = value.substring(1,value.length() - 1);
                                if (Integer.parseInt(respResult.getStatus()) > Integer.parseInt(value)){
                                    System.out.println(respResult.getNum() + " " + respResult.getHost());
                                    respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                                }
                            }else if (queryField.getText().contains(">") && queryField.getText().contains("=")){
                                String value = queryField.getText().substring(queryField.getText().indexOf(">=")+2).trim();
                                value = value.substring(1,value.length() - 1);
                                if (Integer.parseInt(respResult.getStatus()) >= Integer.parseInt(value)){
                                    System.out.println(respResult.getNum() + " " + respResult.getHost());
                                    respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                                }
                            }else if (queryField.getText().contains("<") && !queryField.getText().contains("=")){
                                String value = queryField.getText().substring(queryField.getText().indexOf("<")+1).trim();
                                value = value.substring(1,value.length() - 1);
                                if (Integer.parseInt(respResult.getStatus()) < Integer.parseInt(value)){
                                    System.out.println(respResult.getNum() + " " + respResult.getHost());
                                    respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                                }
                            }else if (queryField.getText().contains("<") && queryField.getText().contains("=")){
                                String value = queryField.getText().substring(queryField.getText().indexOf("<=")+2).trim();
                                value = value.substring(1,value.length() - 1);
                                if (Integer.parseInt(respResult.getStatus()) <= Integer.parseInt(value)){
                                    System.out.println(respResult.getNum() + " " + respResult.getHost());
                                    respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                                }
                            }
                        }
                    }else if (queryField.getText().contains("content-length")){
                        if (respResult.getContentLen().trim().isEmpty() || respResult.getContentLen().contains("Content")){
                        }else {
                            if (queryField.getText().contains("=") && !queryField.getText().contains(">") && !queryField.getText().contains("<") && !queryField.getText().contains("!=")){
                                String value = queryField.getText().substring(queryField.getText().indexOf("=")+1).trim();
                                value = value.substring(1,value.length() - 1);
                                if (Integer.parseInt(respResult.getContentLen()) == Integer.parseInt(value)){
                                    System.out.println(respResult.getNum() + " " + respResult.getHost());
                                    respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                                }
                            }else if (queryField.getText().contains("!=")){
                                String value = queryField.getText().substring(queryField.getText().indexOf("!=")+2).trim();
                                value = value.substring(1,value.length() - 1);
                                if (Integer.parseInt(respResult.getContentLen()) != Integer.parseInt(value)){
                                    System.out.println(respResult.getNum() + " " + respResult.getHost());
                                    respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                                }
                            }else if (queryField.getText().contains(">") && !queryField.getText().contains("=")){
                                String value = queryField.getText().substring(queryField.getText().indexOf(">")+1).trim();
                                value = value.substring(1,value.length() - 1);
                                if (Integer.parseInt(respResult.getContentLen()) > Integer.parseInt(value)){
                                    System.out.println(respResult.getNum() + " " + respResult.getHost());
                                    respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                                }
                            }else if (queryField.getText().contains(">") && queryField.getText().contains("=")){
                                String value = queryField.getText().substring(queryField.getText().indexOf(">=")+2).trim();
                                value = value.substring(1,value.length() - 1);
                                if (Integer.parseInt(respResult.getContentLen()) >= Integer.parseInt(value)){
                                    System.out.println(respResult.getNum() + " " + respResult.getHost());
                                    respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                                }
                            }else if (queryField.getText().contains("<") && !queryField.getText().contains("=")){
                                String value = queryField.getText().substring(queryField.getText().indexOf("<")+1).trim();
                                value = value.substring(1,value.length() - 1);
                                if (Integer.parseInt(respResult.getContentLen()) < Integer.parseInt(value)){
                                    System.out.println(respResult.getNum() + " " + respResult.getHost());
                                    respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                                }
                            }else if (queryField.getText().contains("<") && queryField.getText().contains("=")){
                                String value = queryField.getText().substring(queryField.getText().indexOf("<=")+2).trim();
                                value = value.substring(1,value.length() - 1);
                                if (Integer.parseInt(respResult.getContentLen()) <= Integer.parseInt(value)){
                                    System.out.println(respResult.getNum() + " " + respResult.getHost());
                                    respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                                }
                            }
                        }
                    }else if (queryField.getText().contains("content-type.contain") && !queryField.getText().contains("!=")){
                        String value = queryField.getText().substring(queryField.getText().indexOf("=")+1).trim();
                        value = value.substring(1,value.length() - 1);
                        if (respResult.getContent_Type().contains(value)){
                            System.out.println(respResult.getNum() + " " + respResult.getHost());
                            respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                        }
                    }else if (queryField.getText().contains("content-type.contain") && queryField.getText().contains("!=")){
                        String value = queryField.getText().substring(queryField.getText().indexOf("!=")+2).trim();
                        value = value.substring(1,value.length() - 1);
                        if (!respResult.getContent_Type().contains(value)){
                            System.out.println(respResult.getNum() + " " + respResult.getHost());
                            respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                        }
                    }else if (queryField.getText().contains("location.contain") && !queryField.getText().contains("!=")){
                        String value = queryField.getText().substring(queryField.getText().indexOf("=")+1).trim();
                        value = value.substring(1,value.length() - 1);
                        if (respResult.getRealUrl().contains(value)){
                            System.out.println(respResult.getNum() + " " + respResult.getHost());
                            respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                        }
                    }else if (queryField.getText().contains("location.contain") && queryField.getText().contains("!=")){
                        String value = queryField.getText().substring(queryField.getText().indexOf("!=")+2).trim();
                        value = value.substring(1,value.length() - 1);
                        if (!respResult.getRealUrl().contains(value)){
                            System.out.println(respResult.getNum() + " " + respResult.getHost());
                            respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                        }
                    }else if (queryField.getText().contains("header.contain") && !queryField.getText().contains("!=")){
                        String value = queryField.getText().substring(queryField.getText().indexOf("=")+1).trim();
                        value = value.substring(1,value.length() - 1);
                        if (respResult.getHeaders().contains(value)){
                            System.out.println(respResult.getNum() + " " + respResult.getHost());
                            respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                        }
                    }else if (queryField.getText().contains("header.contain") && queryField.getText().contains("!=")){
                        String value = queryField.getText().substring(queryField.getText().indexOf("!=")+2).trim();
                        value = value.substring(1,value.length() - 1);
                        if (!respResult.getHeaders().contains(value)){
                            System.out.println(respResult.getNum() + " " + respResult.getHost());
                            respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                        }
                    }else if (queryField.getText().contains("body.contain") && !queryField.getText().contains("!=")){
                        String value = queryField.getText().substring(queryField.getText().indexOf("=")+1).trim();
                        value = value.substring(1,value.length() - 1);
                        if (respResult.getRespMessage().contains(value)){
                            System.out.println(respResult.getNum() + " " + respResult.getHost());
                            respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                        }
                    }else if (queryField.getText().contains("body.contain") && queryField.getText().contains("!=")){
                        String value = queryField.getText().substring(queryField.getText().indexOf("!=")+2).trim();
                        value = value.substring(1,value.length() - 1);
                        if (!respResult.getRespMessage().contains(value)){
                            System.out.println(respResult.getNum() + " " + respResult.getHost());
                            respResultsSearch.add(new RespResult(new SimpleStringProperty(respResult.getNum()),new SimpleStringProperty(respResult.getHost()),new SimpleStringProperty(respResult.getStatus()),new SimpleStringProperty(respResult.getTitle()),new SimpleStringProperty(respResult.getServer()),new SimpleStringProperty(respResult.getContentLen()),new SimpleStringProperty(respResult.getRealUrl()),new SimpleStringProperty(respResult.getRespMessage()),new SimpleStringProperty(respResult.getContent_Type()),new SimpleStringProperty(respResult.getHeaders())));
                        }
                    }
                }catch (Exception e){

                }
            }
        }
    }

    @FXML
    private void startRun(){
        if (!OptimisticgirlController.respResults.isEmpty()){
            OptimisticgirlController.respResults.clear();
        }
        stopBut.setDisable(false);
        startBut.setDisable(true);
        TargetsConfig.targetsArray.clear();
        getUrl();
        OtherConfig.targetsNum = TargetsConfig.getTargetsArray().size();
        this.setProgressBar("Num:" + OtherConfig.targetsNum);
        RunTask runTask = new RunTask();
        runTask.run();
    }

    @FXML
    private void stopRun(){
        RunTask.pool.shutdownNow();
        stopBut.setDisable(true);
        startBut.setDisable(false);
    }

    @FXML
    private void cleanRun(){
        String id = tabPane.getSelectionModel().getSelectedItem().getId();
        if (id.contains("tabAll")) {
            if (!OptimisticgirlController.respResults.isEmpty()){
                try {
                    OptimisticgirlController.respResults.remove(0, OptimisticgirlController.respResults.size());
                }catch (Exception e){}

            }
        }else {
            if (!OptimisticgirlController.respResultsSearch.isEmpty()){
                try {
                    OptimisticgirlController.respResultsSearch.remove(0, OptimisticgirlController.respResultsSearch.size());
                }catch (Exception e){}

            }
        }
    }

    public void readTxtFile(String filePath){
        try {
            String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    TargetsConfig.appendTargetsArea(lineTxt + "\n");
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }

    public void getUrl(){
        ArrayList<String> temArray = new ArrayList<String>();
        String[] targets = TargetsConfig.getTargetsArea().split("\n");
        for (String target : targets) {
            if (!target.endsWith("/")){
                target = target + "/";
            }
            if (!target.startsWith("http://") && !target.startsWith("https://")){
                temArray.add("http://"+target);
                temArray.add("https://"+target);
            }else {
                temArray.add(target);
            }
        }
        if (!RequestTypeConfig.getRequestPaths().isEmpty()){
            String[] paths = RequestTypeConfig.getRequestPaths().split("\n");
            for (String path : paths) {
                if (path.startsWith("/")){
                    path = path.substring(1);
                }
                for (String target : temArray) {
                    TargetsConfig.appendTargetsArray(target + path);
                }
            }
        }else {
            for (String target : temArray) {
                TargetsConfig.appendTargetsArray(target);
            }
        }
    }
}

