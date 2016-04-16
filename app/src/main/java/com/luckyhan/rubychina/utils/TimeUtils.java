package com.luckyhan.rubychina.utils;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static final DateFormat RUBY_SERVER_SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+'", Locale.CHINA);
    public static final DateFormat SIMPLE_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    private static PrettyTime mPrettyTime = new PrettyTime();

    public static String getPrettyTime(String serverTime) {
        if (serverTime == null) {
            return "";
        }
        Date date = new Date();
        try {
            date = RUBY_SERVER_SDF.parse(serverTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mPrettyTime.format(date).replaceAll("\\s", "");
    }

    public static String getTime(String serverTime) {
        Date date = new Date();
        try {
            date = RUBY_SERVER_SDF.parse(serverTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return SIMPLE_SDF.format(date);
    }


}
