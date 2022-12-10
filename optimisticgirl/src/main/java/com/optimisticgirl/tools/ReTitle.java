package com.optimisticgirl.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReTitle {
    public static String Title(String str) {
        String title = "";
        if (title.isEmpty()) {
            String titleTag = "<title>\\r?\\n?\\t?(.*)\\r?\\n?</title>";
            Matcher titleTagMatcher = Pattern.compile(titleTag).matcher(str);
            if (titleTagMatcher.find()) {
                title = titleTagMatcher.group(1);
            }

            String capitalizeH1Tag;
            Matcher capitalizeH1TagMatcher;
            if (title.isEmpty()) {
                capitalizeH1Tag = "document.title='(.*)'";
                capitalizeH1TagMatcher = Pattern.compile(capitalizeH1Tag).matcher(str);
                if (capitalizeH1TagMatcher.find()) {
                    title = capitalizeH1TagMatcher.group(1);
                }
            }

            if (title.isEmpty()) {
                capitalizeH1Tag = "<TITLE>\\r?\\n?\\t?(.*)\\r?\\n?</TITLE>";
                capitalizeH1TagMatcher = Pattern.compile(capitalizeH1Tag).matcher(str);
                if (capitalizeH1TagMatcher.find()) {
                    title = capitalizeH1TagMatcher.group(1);
                }
            }

            if (title.isEmpty()) {
                capitalizeH1Tag = "<h1>\\r?\\n?\\t?(.*)\\r?\\n?</h1>";
                capitalizeH1TagMatcher = Pattern.compile(capitalizeH1Tag).matcher(str);
                if (capitalizeH1TagMatcher.find()) {
                    title = capitalizeH1TagMatcher.group(1);
                }
            }

            if (title.isEmpty()) {
                capitalizeH1Tag = "<H1>\\r?\\n?\\t?(.*)\\r?\\n?</H1>";
                capitalizeH1TagMatcher = Pattern.compile(capitalizeH1Tag).matcher(str);
                if (capitalizeH1TagMatcher.find()) {
                    title = capitalizeH1TagMatcher.group(1);
                }
            }
        }

        return title;
    }
}
