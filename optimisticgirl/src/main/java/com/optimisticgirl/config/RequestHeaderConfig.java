package com.optimisticgirl.config;

public class RequestHeaderConfig {
    private static RequestHeaderConfig requestHeaderConfig = new RequestHeaderConfig();
    private static String Key;
    private static String Value;
    public static String requestHeader;

    private RequestHeaderConfig(){
        Key = "";
        Value = "";
        requestHeader = "";
    }

    public static String getRequestHeader() {
        return requestHeader;
    }

    public static void setRequestHeader(String requestHeader) {
        RequestHeaderConfig.requestHeader = requestHeader;
    }
}
