package com.example.appdoan.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDate {
    public static Date formatDate(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        return date;
    }
    public static String ChangeFormatToDMY(String s) throws ParseException{

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        Date date = simpleDateFormat.parse(s);
        String format = simpleDateFormat1.format(date);
        return format;
    }
    public static String ChangeFormatToM(String s) throws ParseException{

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM");
        Date date = simpleDateFormat.parse(s);
        String format = simpleDateFormat1.format(date);
        return format;
    }
    public static String ChangeFormatToY(String s) throws ParseException{

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy");
        Date date = simpleDateFormat.parse(s);
        String format = simpleDateFormat1.format(date);
        return format;
    }
    public static String ChangeFormatToYDM(String s) throws ParseException{

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        Date date = simpleDateFormat1.parse(s);
        String format = simpleDateFormat.format(date);
        return format;
    }

}

