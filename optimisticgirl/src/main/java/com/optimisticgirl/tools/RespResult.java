package com.optimisticgirl.tools;

import javafx.beans.property.SimpleStringProperty;

public class RespResult {
    private SimpleStringProperty num;
    private SimpleStringProperty host;
    private SimpleStringProperty status;
    private SimpleStringProperty title;
    private SimpleStringProperty server;
    private SimpleStringProperty contentLen;
    private SimpleStringProperty realUrl;
    private SimpleStringProperty respMessage;
    private SimpleStringProperty content_Type;
    private SimpleStringProperty headers;
    public static String resp;
    public static String req;

    public RespResult(SimpleStringProperty num, SimpleStringProperty host, SimpleStringProperty status, SimpleStringProperty title, SimpleStringProperty server, SimpleStringProperty contentLen, SimpleStringProperty realUrl, SimpleStringProperty respMessage, SimpleStringProperty content_Type, SimpleStringProperty headers) {
        this.num = num;
        this.host = host;
        this.status = status;
        this.title = title;
        this.server = server;
        this.contentLen = contentLen;
        this.realUrl = realUrl;
        this.respMessage = respMessage;
        this.content_Type = content_Type;
        this.headers = headers;
    }

    public String getNum() {
        return num.get();
    }

    public String getHost() {
        return host.get();
    }

    public String getStatus() {
        return status.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getServer() {
        return server.get();
    }

    public String getContentLen() {
        return contentLen.get();
    }

    public String getRealUrl() {
        return realUrl.get();
    }

    public String getRespMessage() {
        return respMessage.get();
    }

    public String getContent_Type() {
        return content_Type.get();
    }

    public String getHeaders() {
        return headers.get();
    }

}
