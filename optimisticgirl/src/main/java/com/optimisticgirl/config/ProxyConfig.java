package com.optimisticgirl.config;

public class ProxyConfig {
    private static ProxyConfig proxyConfig = new ProxyConfig();
    private static String proxyHost;
    private static int proxyPort;
    private static String proxyUsername;
    private static String proxyPassword;
    private static Boolean enableProxy;
    private static String proxyType;

    private ProxyConfig(){
        proxyHost = "";
        proxyPort = -1;
        proxyUsername = "";
        proxyPassword = "";
        enableProxy = false;
        proxyType = "";
    }

    public static String getProxyType() {
        return proxyConfig.proxyType;
    }

    public static void setProxyType(String proxyType) {
        proxyConfig.proxyType = proxyType;
    }

    public static String getProxyHost() {
        return proxyHost;
    }

    public static void setProxyHost(String proxyHost) {
        proxyConfig.proxyHost = proxyHost;
    }

    public static int getProxyPort() {
        return proxyPort;
    }

    public static void setProxyPort(int proxyPort) {
        proxyConfig.proxyPort = proxyPort;
    }

    public static String getProxyUsername() {
        return proxyUsername;
    }

    public static void setProxyUsername(String proxyUsername) {
        proxyConfig.proxyUsername = proxyUsername;
    }

    public static String getProxyPassword() {
        return proxyPassword;
    }

    public static void setProxyPassword(String proxyPassword) {
        proxyConfig.proxyPassword = proxyPassword;
    }

    public static Boolean getEnableProxy() {
        return enableProxy;
    }

    public static void setEnableProxy(Boolean enableProxy) {
        proxyConfig.enableProxy = enableProxy;
    }

}
