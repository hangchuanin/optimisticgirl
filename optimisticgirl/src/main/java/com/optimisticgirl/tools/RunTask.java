package com.optimisticgirl.tools;

import com.optimisticgirl.controller.OptimisticgirlController;
import com.optimisticgirl.config.*;
import java.lang.String;
import javafx.beans.property.SimpleStringProperty;
import me.gv7.woodpecker.requests.Proxies;
import me.gv7.woodpecker.requests.RawResponse;
import me.gv7.woodpecker.requests.Requests;
import me.gv7.woodpecker.requests.exception.RequestsException;
import javax.swing.*;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunTask {
    private int requestThread;
    private Boolean enableProxy;
    private String proxyType;
    private int proxyPort;
    private String proxyHost;
    private String proxyUsername;
    private String proxyPassword;
    private String requestHeader;
    private String enctype;
    private String requestContents;
    private String requestType;
    private String requestPath;
    private ArrayList targetsArray;
    public static ExecutorService pool;
    private int num = 1;
    private String postContentString = "";
    private String postContentNullString = "";
    private Map<String, Object> headersMap = new HashMap<>();

    public void taskInit(){
        headersMap.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        headersMap.put("Accept","charset=UTF-8");
        requestThread = OtherConfig.getRequestThread();
        enableProxy = ProxyConfig.getEnableProxy();
        proxyType = ProxyConfig.getProxyType();
        proxyPort = ProxyConfig.getProxyPort();
        proxyHost = ProxyConfig.getProxyHost();
        proxyUsername = ProxyConfig.getProxyUsername();
        proxyPassword = ProxyConfig.getProxyPassword();
        requestHeader = RequestHeaderConfig.getRequestHeader();
        enctype = RequestTypeConfig.getEnctype();
        requestContents = RequestTypeConfig.getRequestContents();
        requestType = RequestTypeConfig.getRequestType();
        requestPath = RequestTypeConfig.getRequestPaths();
        targetsArray = TargetsConfig.getTargetsArray();
        pool = Executors.newFixedThreadPool(requestThread);
        headersMap = getHeader();
    }

    public void run(){
        taskInit();
        start();
    }

//    public String getRequestPacket(){
//        String requestTemplate01 =
//                requestType + " /requestPath_by" + " HTTP/1.1\n";
//
//        String requestTemplate02 = "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36\n";
//
//        String requestTemplate03 =
//                "Accept-Encoding: gzip, deflate\n" +
//                "Cache-Control: no-cache\n" +
//                "Pragma: no-cache\n" +
//                "Host: It's a placeholder\n" +
//                "Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\n" +
//                "Connection: close\n";
//        String requestTemplate04 = "";
//        Map header = getHeader();
//        for (Object o : header.keySet()) {
//            String key = String.valueOf(o);
//            if (key.contains("Accept-Encoding")){
//                requestTemplate03 = requestTemplate03.replace("gzip, deflate", String.valueOf(header.get("Accept-Encoding")));
//            }else if (key.contains("Cache-Control")){
//                requestTemplate03 = requestTemplate03.replace("no-cache", String.valueOf(header.get("Cache-Control")));
//            }else if (key.contains("Pragma")){
//                requestTemplate03 = requestTemplate03.replace("no-cache", String.valueOf(header.get("Pragma")));
//            }else if (key.contains("Accept")){
//                requestTemplate03 = requestTemplate03.replace("text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2", String.valueOf(header.get("Accept")));
//            }else if (key.contains("Connection")){
//                requestTemplate03 = requestTemplate03.replace("close", String.valueOf(header.get("Connection")));
//            }else if (key.contains("User-Agent")){
//                requestTemplate02 = requestTemplate02.replace("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36", String.valueOf(header.get("User-Agent")));
//            }else if (key.contains("Content-Type")){
//                requestTemplate01 = requestTemplate01 + key + ": " + header.get(key) + "\n";
//            }else {
//                requestTemplate02 = requestTemplate02 + key + ": " + header.get(key) + "\n";
//            }
//        }
//        if (requestType != "GET" && !requestContents.isEmpty()){
//            requestTemplate03 = requestTemplate03 + "Content-Length: " + String.valueOf(requestContents.length()) + "\n\n";
//            requestTemplate04 = requestTemplate04 + requestContents;
//        }else {
//            requestTemplate03  =requestTemplate03 + "\n";
//        }
//        return requestTemplate01 + requestTemplate02 + requestTemplate03 + requestTemplate04;
//    }


    public void start(){
        Iterator iterator = targetsArray.iterator();
        while (iterator.hasNext()){
            String target = (String) iterator.next();
            SwingWorker<Void,Void> swingWorker = new SwingWorker<Void,Void>(){
                protected Void doInBackground() throws Exception {
                    String onlyHost = target.substring(0,target.indexOf("/",10));
                    String target1 = target;
                    String statusCode = "";
                    String respMessage = "";
                    String title = "";
                    String server = "";
                    String contentLen = "";
                    String location = "";
                    String content_Type = "";
                    String headers = "";
                    List list = new ArrayList();
                    if (requestType == "GET"){
                        try {
                            RawResponse resp;
                            if (enableProxy){
                                System.out.println(target);
                                resp = Requests.get(target).headers(headersMap).proxy(getProxy()).followRedirect(false).verify(false).send();
                            }else {
                                resp = Requests.get(target).headers(headersMap).followRedirect(false).verify(false).send();
                            }
                            statusCode = String.valueOf(resp.statusCode());
                            list = resp.getHeaders();
                            for (int i=0;i<list.size();i++){
                                String keyValue = list.get(i).toString().replace("(", "").replace(")", "").replaceFirst(" ", "");
                                keyValue = keyValue.replaceFirst(" ","");
                                headers = headers + keyValue + "&&";
                            }
                            headers = headers.substring(0,headers.length() - 2);
                            if (resp.getHeader("Content-Type") != null){
                                content_Type = resp.getHeader("Content-Type");
                                if (content_Type.trim() == ""){
                                    content_Type = "";
                                }
                            }else {
                                content_Type = "";
                            }
                            if (resp.getHeader("server") != null){
                                server = resp.getHeader("server");
                                if (server.trim() == ""){
                                    server = "";
                                }
                            }else {
                                server = "";
                            }
                            if (resp.getHeader("Content-Length") != null){
                                contentLen = resp.getHeader("Content-Length");
                                if (contentLen.trim() == ""){
                                    contentLen = String.valueOf(respMessage.getBytes("UTF-8").length);
                                }
                            }else {
                                contentLen = String.valueOf(respMessage.getBytes("UTF-8").length);
                            }
                            if (resp.getHeader("Location") != null){
                                if (resp.getHeader("Location").startsWith("http")){
                                    location = resp.getHeader("Location");
                                }else {
                                    if (resp.getHeader("Location").startsWith("/")){
                                        location = onlyHost + "/" + resp.getHeader("Location").substring(1);
                                    }else {
                                        location = onlyHost + "/" + resp.getHeader("Location");
                                    }
                                }
                                if (location == ""){
                                    location = "";
                                }
                            }else {
                                location = "";
                            }
                            respMessage = resp.readToText();
//                            byte[] bytes = resp.readToText().getBytes();
//                            respMessage = new String(bytes,"GBK");
//                            if (resp.readToText().contains("charset=gb2312") || resp.readToText().contains("charset=gbk")) {
//                                byte[] bytes = resp.readToText().getBytes();
//                                respMessage = new String(bytes,"UTF-8");
//                            }else {
//                                byte[] bytes = resp.readToText().getBytes();
//                                respMessage = new String(bytes,"UTF-8");
//                            }
                            title = ReTitle.Title(respMessage);
                        }catch (RequestsException requestsException){
                            if (statusCode == ""){
                                statusCode = "Connection refused";
                            }
                        }
                    }else if (requestType == "POST"){
                        try {
                            RawResponse resp;
                            if (enableProxy){
                                resp = Requests.post(target).body(getPostContent()).headers(headersMap).proxy(getProxy()).followRedirect(false).verify(false).send();
                            }else {
                                resp = Requests.post(target).body(getPostContent()).headers(headersMap).followRedirect(false).verify(false).send();
                            }
                            statusCode = String.valueOf(resp.statusCode());
                            list = resp.getHeaders();
                            for (int i=0;i<list.size();i++){
                                String keyValue = list.get(i).toString().replace("(", "").replace(")", "").replaceFirst(" ", "");
                                keyValue = keyValue.replaceFirst(" ","");
                                headers = headers + keyValue + "&&";
                            }
                            headers = headers.substring(0,headers.length() - 2);
                            if (resp.getHeader("Content-Type") != null){
                                content_Type = resp.getHeader("Content-Type");
                                if (content_Type.trim() == ""){
                                    content_Type = "";
                                }
                            }else {
                                content_Type = "";
                            }
                            respMessage = resp.readToText();
                            if (resp.getHeader("server") != null){
                                server = resp.getHeader("server");
                                if (server.trim() == ""){
                                    server = "";
                                }
                            }else {
                                server = "";
                            }
                            if (resp.getHeader("Content-Length") != null){
                                contentLen = resp.getHeader("Content-Length");
                                if (contentLen.trim() == ""){
                                    contentLen = String.valueOf(respMessage.getBytes("UTF-8").length);
                                }
                            }else {
                                contentLen = String.valueOf(respMessage.getBytes("UTF-8").length);
                            }
                            if (resp.getHeader("Location") != null){
                                if (resp.getHeader("Location").startsWith("http")){
                                    location = resp.getHeader("Location");
                                }else {
                                    if (resp.getHeader("Location").startsWith("/")){
                                        location = onlyHost + "/" + resp.getHeader("Location").substring(1);
                                    }else {
                                        location = onlyHost + "/" + resp.getHeader("Location");
                                    }
                                }
                                if (location == ""){
                                    location = "";
                                }
                            }else {
                                location = "";
                            }
                            title = ReTitle.Title(respMessage);
                        }catch (RequestsException requestsException){
                            if (statusCode == ""){
                                statusCode = "Connection refused";
                            }
                        }
                    }else if (requestType == "HEAD"){
                        try {
                            RawResponse resp;
                            if (enableProxy){
                                resp = Requests.head(target).body(getPostContent()).headers(headersMap).proxy(getProxy()).followRedirect(false).verify(false).send();
                            }else {
                                resp = Requests.head(target).body(getPostContent()).headers(headersMap).followRedirect(false).verify(false).send();
                            }
                            statusCode = String.valueOf(resp.statusCode());
                            list = resp.getHeaders();
                            for (int i=0;i<list.size();i++){
                                String keyValue = list.get(i).toString().replace("(", "").replace(")", "").replaceFirst(" ", "");
                                keyValue = keyValue.replaceFirst(" ","");
                                headers = headers + keyValue + "&&";
                            }
                            headers = headers.substring(0,headers.length() - 2);
                            if (resp.getHeader("Content-Type") != null){
                                content_Type = resp.getHeader("Content-Type");
                                if (content_Type.trim() == ""){
                                    content_Type = "";
                                }
                            }else {
                                content_Type = "";
                            }
                            respMessage = resp.readToText();
                            if (resp.getHeader("server") != null){
                                server = resp.getHeader("server");
                                if (server.trim() == ""){
                                    server = "";
                                }
                            }else {
                                server = "";
                            }
                            if (resp.getHeader("Content-Length") != null){
                                contentLen = resp.getHeader("Content-Length");
                                if (contentLen.trim() == ""){
                                    contentLen = String.valueOf(respMessage.getBytes("UTF-8").length);
                                }
                            }else {
                                contentLen = String.valueOf(respMessage.getBytes("UTF-8").length);
                            }
                            if (resp.getHeader("Location") != null){
                                if (resp.getHeader("Location").startsWith("http")){
                                    location = resp.getHeader("Location");
                                }else {
                                    if (resp.getHeader("Location").startsWith("/")){
                                        location = onlyHost + "/" + resp.getHeader("Location").substring(1);
                                    }else {
                                        location = onlyHost + "/" + resp.getHeader("Location");
                                    }
                                }
                                if (location == ""){
                                    location = "";
                                }
                            }else {
                                location = "";
                            }
                            title = ReTitle.Title(respMessage);
                        }catch (RequestsException requestsException){
                            if (statusCode == ""){
                                statusCode = "Connection refused";
                            }
                        }
                    }else if (requestType == "PUT"){
                        try {
                            RawResponse resp;
                            if (enableProxy){
                                resp = Requests.put(target).body(getPostContent()).headers(headersMap).proxy(getProxy()).followRedirect(false).verify(false).send();
                            }else {
                                resp = Requests.put(target).body(getPostContent()).headers(headersMap).followRedirect(false).verify(false).send();
                            }
                            statusCode = String.valueOf(resp.statusCode());
                            list = resp.getHeaders();
                            for (int i=0;i<list.size();i++){
                                String keyValue = list.get(i).toString().replace("(", "").replace(")", "").replaceFirst(" ", "");
                                keyValue = keyValue.replaceFirst(" ","");
                                headers = headers + keyValue + "&&";
                            }
                            headers = headers.substring(0,headers.length() - 2);
                            if (resp.getHeader("Content-Type") != null){
                                content_Type = resp.getHeader("Content-Type");
                                if (content_Type.trim() == ""){
                                    content_Type = "";
                                }
                            }else {
                                content_Type = "";
                            }
                            respMessage = resp.readToText();
                            if (resp.getHeader("server") != null){
                                server = resp.getHeader("server");
                                if (server.trim() == ""){
                                    server = "";
                                }
                            }else {
                                server = "";
                            }
                            if (resp.getHeader("Content-Length") != null){
                                contentLen = resp.getHeader("Content-Length");
                                if (contentLen.trim() == ""){
                                    contentLen = String.valueOf(respMessage.getBytes("UTF-8").length);
                                }
                            }else {
                                contentLen = String.valueOf(respMessage.getBytes("UTF-8").length);
                            }
                            if (resp.getHeader("Location") != null){
                                if (resp.getHeader("Location").startsWith("http")){
                                    location = resp.getHeader("Location");
                                }else {
                                    if (resp.getHeader("Location").startsWith("/")){
                                        location = onlyHost + "/" + resp.getHeader("Location").substring(1);
                                    }else {
                                        location = onlyHost + "/" + resp.getHeader("Location");
                                    }
                                }
                                if (location == ""){
                                    location = "";
                                }
                            }else {
                                location = "";
                            }
                            title = ReTitle.Title(respMessage);
                        }catch (RequestsException requestsException){
                            if (statusCode == ""){
                                statusCode = "Connection refused";
                            }
                        }
                    }else if (requestType == "DELETE"){
                        try {
                            RawResponse resp;
                            if (enableProxy){
                                resp = Requests.delete(target).body(getPostContent()).headers(headersMap).proxy(getProxy()).followRedirect(false).verify(false).send();
                            }else {
                                resp = Requests.delete(target).body(getPostContent()).headers(headersMap).followRedirect(false).verify(false).send();
                            }
                            statusCode = String.valueOf(resp.statusCode());
                            list = resp.getHeaders();
                            for (int i=0;i<list.size();i++){
                                String keyValue = list.get(i).toString().replace("(", "").replace(")", "").replaceFirst(" ", "");
                                keyValue = keyValue.replaceFirst(" ","");
                                headers = headers + keyValue + "&&";
                            }
                            headers = headers.substring(0,headers.length() - 2);
                            if (resp.getHeader("Content-Type") != null){
                                content_Type = resp.getHeader("Content-Type");
                                if (content_Type.trim() == ""){
                                    content_Type = "";
                                }
                            }else {
                                content_Type = "";
                            }
                            respMessage = resp.readToText();
                            if (resp.getHeader("server") != null){
                                server = resp.getHeader("server");
                                if (server.trim() == ""){
                                    server = "";
                                }
                            }else {
                                server = "";
                            }
                            if (resp.getHeader("Content-Length") != null){
                                contentLen = resp.getHeader("Content-Length");
                                if (contentLen.trim() == ""){
                                    contentLen = String.valueOf(respMessage.getBytes("UTF-8").length);
                                }
                            }else {
                                contentLen = String.valueOf(respMessage.getBytes("UTF-8").length);
                            }
                            if (resp.getHeader("Location") != null){
                                if (resp.getHeader("Location").startsWith("http")){
                                    location = resp.getHeader("Location");
                                }else {
                                    if (resp.getHeader("Location").startsWith("/")){
                                        location = onlyHost + "/" + resp.getHeader("Location").substring(1);
                                    }else {
                                        location = onlyHost + "/" + resp.getHeader("Location");
                                    }
                                }
                                if (location == ""){
                                    location = "";
                                }
                            }else {
                                location = "";
                            }
                            title = ReTitle.Title(respMessage);
                        }catch (RequestsException requestsException){
                            if (statusCode == ""){
                                statusCode = "Connection refused";
                            }
                        }
                    }else if (requestType == "PATCH"){
                        try {
                            RawResponse resp;
                            if (enableProxy){
                                resp = Requests.patch(target).body(getPostContent()).headers(headersMap).proxy(getProxy()).followRedirect(false).verify(false).send();
                            }else {
                                resp = Requests.patch(target).body(getPostContent()).headers(headersMap).followRedirect(false).verify(false).send();
                            }
                            statusCode = String.valueOf(resp.statusCode());
                            list = resp.getHeaders();
                            for (int i=0;i<list.size();i++){
                                String keyValue = list.get(i).toString().replace("(", "").replace(")", "").replaceFirst(" ", "");
                                keyValue = keyValue.replaceFirst(" ","");
                                headers = headers + keyValue + "&&";
                            }
                            headers = headers.substring(0,headers.length() - 2);
                            if (resp.getHeader("Content-Type") != null){
                                content_Type = resp.getHeader("Content-Type");
                                if (content_Type.trim() == ""){
                                    content_Type = "";
                                }
                            }else {
                                content_Type = "";
                            }
                            respMessage = resp.readToText();
                            if (resp.getHeader("server") != null){
                                server = resp.getHeader("server");
                                if (server.trim() == ""){
                                    server = "";
                                }
                            }else {
                                server = "";
                            }
                            if (resp.getHeader("Content-Length") != null){
                                contentLen = resp.getHeader("Content-Length");
                                if (contentLen.trim() == ""){
                                    contentLen = String.valueOf(respMessage.getBytes("UTF-8").length);
                                }
                            }else {
                                contentLen = String.valueOf(respMessage.getBytes("UTF-8").length);
                            }
                            if (resp.getHeader("Location") != null){
                                if (resp.getHeader("Location").startsWith("http")){
                                    location = resp.getHeader("Location");
                                }else {
                                    if (resp.getHeader("Location").startsWith("/")){
                                        location = onlyHost + "/" + resp.getHeader("Location").substring(1);
                                    }else {
                                        location = onlyHost + "/" + resp.getHeader("Location");
                                    }
                                }
                                if (location == ""){
                                    location = "";
                                }
                            }else {
                                location = "";
                            }
                            title = ReTitle.Title(respMessage);
                        }catch (RequestsException requestsException){
                            if (statusCode == ""){
                                statusCode = "Connection refused";
                            }
                        }
                    }
                    try {
                        RespResult respResultone = new RespResult(new SimpleStringProperty(String.valueOf(num++)), new SimpleStringProperty(onlyHost), new SimpleStringProperty(statusCode), new SimpleStringProperty(title), new SimpleStringProperty(server), new SimpleStringProperty(contentLen), new SimpleStringProperty(location), new SimpleStringProperty(respMessage), new SimpleStringProperty(content_Type), new SimpleStringProperty(headers));
                        System.out.println(num + " " +target);
                        OptimisticgirlController.respResults.add(respResultone);
                    }catch (Exception e){
                        System.out.println(e);
                    }
                    return null;
                }
                protected void done() {}
            };
            pool.execute(swingWorker);
        }
    }

    public Proxy getProxy(){
        if (!this.proxyUsername.equals("") && !this.proxyPassword.equals("")) {
            Authenticator.setDefault(new Authenticator(){
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyUsername, proxyPassword.toCharArray());
                }
            });
        } else {
            Authenticator.setDefault(null);
        }

        if (this.proxyType.contains("HTTP")){
            return Proxies.httpProxy(this.proxyHost, this.proxyPort);
        }else {
            return Proxies.socksProxy(this.proxyHost, this.proxyPort);
        }
    }

    public Map getHeader(){
        if (requestHeader.trim().isEmpty()){
            return headersMap;
        }else {
            if (requestHeader.contains("\n")){
                String[] headers = requestHeader.split("\n");
                for (String header : headers) {
                    String key = header.substring(0, header.indexOf(":"));
                    String value = header.substring(header.indexOf(":") + 1);
                    if (value.startsWith(" ")){
                        value = value.substring(1);
                    }
                    headersMap.put(key,value);
                }
            }else {
                String key = requestHeader.substring(0, requestHeader.indexOf(":"));
                String value = requestHeader.substring(requestHeader.indexOf(":") + 1);
                if (value.startsWith(" ")){
                    value = value.substring(1);
                }
                headersMap.put(key,value);
            }
        }
        return headersMap;
    }


    public String getPostContent(){
        if (requestContents.trim().isEmpty()){
            return postContentNullString;
        }else {
            postContentString = requestContents;
        }
        return postContentString;
    }
}
