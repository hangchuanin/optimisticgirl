package com.optimisticgirl.config;

public class OtherConfig {

    public static int requestThread = 1;
    public static int targetsNum;
    public static int getRequestThread() {
        return requestThread;
    }
    public static void setRequestThread(int requestThread) {
        OtherConfig.requestThread = requestThread;
    }
}
