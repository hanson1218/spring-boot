package com.test.common.config.utils;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    private static final String YYYYMMMDDHHMM = "yyyy-MM-dd HH:mm";

    public static String parse(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "";
//        formatter.format(new Date());
//        System.out.println(formatter.format(zdt));
    }
}
