package com.example.appdoan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends BaseAdapter {
    private Context context;
    private List<Date> dates;
    private Calendar currentDate;
    private LayoutInflater inflater;

    public CalendarAdapter(Context context, List<Date> dates, Calendar currentDate) {
        this.context = context;
        this.dates = dates;
        this.currentDate = currentDate;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Date date = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);

        int day = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int month = dateCalendar.get(Calendar.MONTH);
        int year = dateCalendar.get(Calendar.YEAR);

        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentYear = currentDate.get(Calendar.YEAR);

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_day, parent, false);
            holder.tvDay = convertView.findViewById(R.id.tvDay);
            holder.viewSelected = convertView.findViewById(R.id.viewSelected);
            holder.viewEventIndicator = convertView.findViewById(R.id.viewEventIndicator);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvDay.setText(String.valueOf(day));

        // Đặt màu sắc khác nhau cho ngày của tháng khác
        if (month == currentMonth && year == currentYear) {
            holder.tvDay.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        } else {
            holder.tvDay.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        }

        // Kiểm tra nếu là ngày hiện tại
        Calendar today = Calendar.getInstance();
        if (dateCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                dateCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                dateCalendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            holder.viewSelected.setVisibility(View.VISIBLE);
            holder.tvDay.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            holder.viewSelected.setVisibility(View.INVISIBLE);
        }

        // Kiểm tra nếu có sự kiện (tùy chỉnh theo nhu cầu)
        if (hasEvent(date)) {
            holder.viewEventIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.viewEventIndicator.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private boolean hasEvent(Date date) {
        // Logic kiểm tra sự kiện
        return false;
    }

    private static class ViewHolder {
        TextView tvDay;
        View viewSelected;
        View viewEventIndicator;
    }
}
