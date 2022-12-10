package com.optimisticgirl.config;


public class RequestTypeConfig {
    private static String requestType = "GET";
    private static String requestPaths = "";
    private static String requestContents = "";
    private static String enctype = "application/x-www-form-urlencoded";
    private static RequestTypeConfig requestTypeConfig = new RequestTypeConfig();

    public RequestTypeConfig(){}

    public static String getRequestType() {
        return requestType;
    }

    public static void setRequestType(String requestType) {
        RequestTypeConfig.requestType = requestType;
    }

    public static String getRequestPaths() {
        return requestPaths;
    }

    public static void setRequestPaths(String requestPaths) {
        RequestTypeConfig.requestPaths = requestPaths;
    }

    public static String getRequestContents() {
        return requestContents;
    }

    public static void setRequestContents(String requestContents) {
        RequestTypeConfig.requestContents = requestContents;
    }

    public static String getEnctype() {
        return enctype;
    }

    public static void setEnctype(String enctype) {
        RequestTypeConfig.enctype = enctype;
    }
}
