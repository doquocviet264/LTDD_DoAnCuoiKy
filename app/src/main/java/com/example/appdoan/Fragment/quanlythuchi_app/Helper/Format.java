package com.example.appdoan.Fragment.quanlythuchi_app.Helper;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Format {
    public static String formatNumber(Long input)
    {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(input);
    }

    public static String formatCardNumber(String input)
    {
        if( input.length() <= 4)
            return input;

        int first = (input.length() - 1) % 4 + 1;
        StringBuilder ouput = new StringBuilder(input.substring(0, first));

        for( int i = first ; i <input.length() ;i+=4)
        {
            String fourNumber = input.substring(i, i+4);
            ouput.append(' ').append( fourNumber );
        }

        return ouput.toString();
    }


    // xử lý in chuỗi nếu chuỗi quá dài
    public static String truncate_string(String text, int max_length, String ellipsis, boolean trim){
        if (max_length < 1) {
            max_length = 50;
        }

        if (trim) {
            text = text.trim();
        }

        if(ellipsis.isEmpty()){
            ellipsis = "...";
        }

        int string_length = text.length();
        int ellipsis_length = ellipsis.length();
        if(string_length > max_length){
            if (ellipsis_length >= max_length) {
                text = ellipsis.substring(0, max_length);
            } else {
                text = text.substring(0, max_length - ellipsis_length) + ellipsis;
            }
        }

        return text;
    }

    public static String getRealColor(String hexColor){
        String prefix = "#";
        if (hexColor.length() == 6) {
            return prefix + hexColor;
        } else if (hexColor.length() > 6) {
            return prefix + hexColor.substring(hexColor.length() - 6);
        } else {
            // whatever is appropriate in this case
            throw new IllegalArgumentException("word has fewer than 6 characters!");
        }
    }

    public static String formatDate(String inputDate) {
        String outputDate = null;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Chuyển đổi chuỗi thành đối tượng Date
            Date date = inputFormat.parse(inputDate);

            // Chuyển đổi lại thành chuỗi với định dạng mới
            outputDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }


    public static String formatDateRequest(String inputDate) {
        String[] dateParts = inputDate.split("/");
        String day = dateParts[0];
        String month = dateParts[1];
        String year = dateParts[2];

        String outputDate = year + "-" + month + "-" + day;

        return outputDate;
    }
}
