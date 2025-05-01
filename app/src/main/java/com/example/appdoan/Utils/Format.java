package com.example.appdoan.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Format {

    // Định dạng số với dấu phân cách hàng nghìn
    public static String formatNumber(Long input) {
        return String.format("%,d", input); // Sử dụng String.format() thay vì DecimalFormat
    }

    // Định dạng số thẻ tín dụng (sắp xếp thành nhóm 4 số)
    public static String formatCardNumber(String input) {
        if (input.length() <= 4) return input;

        StringBuilder output = new StringBuilder(input.substring(0, 4));
        for (int i = 4; i < input.length(); i += 4) {
            output.append(" ").append(input, i, Math.min(i + 4, input.length()));
        }

        return output.toString();
    }

    // Cắt ngắn chuỗi nếu cần, thêm dấu "..."
    public static String truncate_string(String text, int maxLength, String ellipsis, boolean trim) {
        if (maxLength < 1) maxLength = 50; // Giá trị mặc định
        if (trim) text = text.trim(); // Cắt bỏ khoảng trắng thừa

        if (ellipsis.isEmpty()) ellipsis = "..."; // Dấu ba chấm mặc định

        if (text.length() > maxLength) {
            int ellipsisLength = ellipsis.length();
            return text.substring(0, maxLength - ellipsisLength) + ellipsis;
        }
        return text;
    }

    // Xử lý màu HEX: nếu không có dấu "#" thì thêm vào
    public static String getRealColor(String hexColor) {
        if (hexColor.length() == 6) {
            return "#" + hexColor;
        } else if (hexColor.length() > 6) {
            return "#" + hexColor.substring(hexColor.length() - 6);
        } else {
            throw new IllegalArgumentException("Color code must have at least 6 characters!");
        }
    }

    // Định dạng ngày từ "yyyy-MM-dd" thành "dd/MM/yyyy"
    public static String formatDate(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu có lỗi
    }

    // Chuyển đổi ngày từ "dd/MM/yyyy" thành "yyyy-MM-dd"
    public static String formatDateRequest(String inputDate) {
        String[] dateParts = inputDate.split("/");
        return dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
    }
}
