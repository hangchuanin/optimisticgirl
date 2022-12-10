package com.optimisticgirl.config;

import java.util.ArrayList;

public class TargetsConfig {

    public static ArrayList<String> targetsArray = new ArrayList<String>();
    private static String targetsArea = "";

    public static ArrayList<String> getTargetsArray() {
        return targetsArray;
    }

    public static String getTargetsArea() {
        return targetsArea;
    }

    public static void setTargetsArea(String targetsArea) {
        TargetsConfig.targetsArea = targetsArea;
    }

    public static void appendTargetsArea(String appendString){
        TargetsConfig.targetsArea = TargetsConfig.targetsArea + appendString;
    }

    public static void appendTargetsArray(String appendString){
        TargetsConfig.targetsArray.add(appendString);
    }
}
