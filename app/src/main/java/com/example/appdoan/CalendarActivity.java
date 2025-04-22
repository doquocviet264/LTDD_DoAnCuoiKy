package com.example.appdoan;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    private TextView tvMonthYear;
    private GridView gridCalendar;
    private Calendar currentDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        tvMonthYear = findViewById(R.id.tvMonthYear);
        gridCalendar = findViewById(R.id.gridCalendar);

        // Cập nhật lịch
        updateCalendar();

        // Xử lý nút next/previous
        findViewById(R.id.btnNext).setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, -1);
            updateCalendar();
        });
    }

    private void updateCalendar() {
        // Cập nhật tiêu đề tháng/năm
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        tvMonthYear.setText(sdf.format(currentDate.getTime()));

        // Lấy danh sách ngày trong tháng và thiết lập adapter
        List<Date> daysInMonth = getDaysInMonth(currentDate);
        CalendarAdapter adapter = new CalendarAdapter(this, daysInMonth, currentDate);
        gridCalendar.setAdapter(adapter);
    }

    private List<Date> getDaysInMonth(Calendar calendar) {
        List<Date> days = new ArrayList<>();

        // Clone calendar để không ảnh hưởng đến calendar gốc
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // Xác định tháng hiện tại
        int month = cal.get(Calendar.MONTH);

        // Tính toán ngày bắt đầu (ngày đầu tiên hiển thị có thể thuộc tháng trước)
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1; // Chủ nhật = 0, Thứ 2 = 1,...
        cal.add(Calendar.DAY_OF_MONTH, -firstDayOfWeek);

        // Tạo danh sách 42 ngày (6 tuần)
        while (days.size() < 42) {
            days.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;
    }
}