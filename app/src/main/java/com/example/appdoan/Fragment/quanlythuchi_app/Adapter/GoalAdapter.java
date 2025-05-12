package com.example.appdoan.Fragment.quanlythuchi_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlythuchi_app.Activity.Goal.GoalDepositActivity;
import com.example.quanlythuchi_app.Activity.Goal.GoalDetailActivity;
import com.example.quanlythuchi_app.Activity.Goal.GoalUpdateActivity;
import com.example.quanlythuchi_app.Helper.Format;
import com.example.quanlythuchi_app.Model.GoalModel;
import com.example.quanlythuchi_app.R;
import com.example.quanlythuchi_app.Utils.FormatDate;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    private List<GoalModel> listGoal;
    private Context context;
    public GoalAdapter(Context context, List<GoalModel> listGoal){
        this.context=context;
        this.listGoal=listGoal;
    }
    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final GoalModel goalModel = listGoal.get(position);
        Double amount = Double.valueOf(goalModel.getAmount());
        Double balance= Double.valueOf(goalModel.getBalance());
        Date currentTime = new Date();
        Date deadline = null;
        try {
            deadline = FormatDate.formatDate(goalModel.getDeadline());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        int progress = (int) ((balance/amount)*100);
        int compareTime = currentTime.compareTo(deadline);
        if(goalModel==null){return;}
        holder.tvGoalName.setText(goalModel.getName());
        holder.tvGoalAmount.setText(Format.formatNumber(goalModel.getAmount()));
        try {
            holder.tvGoalDeadline.setText(FormatDate.ChangeFormatToDMY(goalModel.getDeadline()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if(compareTime>0){
            holder.tvIconGoal.setBackground(Drawable.createFromPath("@drawable/goal"));
            holder.tvGoalBalance.setText("Quá hạn");
            holder.tvGoalBalance.setTextColor(Color.RED);

        }
        else if(balance>=amount){
            holder.tvIconGoal.setBackground(Drawable.createFromPath("@drawable/goal"));
            holder.tvGoalBalance.setText("Hoàn thành");
            holder.tvGoalBalance.setTextColor(Color.GREEN);
        }else {
            holder.tvGoalBalance.setText("Đã có: "+ Format.formatNumber(goalModel.getBalance()));
        }
        holder.pbGoalProgress.setProgress(progress);
        holder.layoutItemGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onClickGoToUpdate(position);
            }
        });
    }
    private void onClickGoToUpdate(int position)
    {

        String[] option = {"Sửa","Chi tiết", "Thêm tiền"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn một tùy chọn").setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = option[which];
                switch (selected){
                    case "Sửa":
                        Intent intent = new Intent(context, GoalUpdateActivity.class);
                        intent.putExtra("position", position);
                        context.startActivity(intent);
                        break;
                    case "Chi tiết":
                        Intent intent1 = new Intent(context, GoalDetailActivity.class);
                        intent1.putExtra("position", position);
                        context.startActivity(intent1);
                        break;
                    case "Thêm tiền":
                        Intent intent2 = new Intent(context, GoalDepositActivity.class);
                        intent2.putExtra("position", position);
                        context.startActivity(intent2);
                        break;
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return listGoal==null?0:listGoal.size();
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvGoalName, tvGoalAmount, tvGoalBalance, tvGoalDeadline,tvIconGoal;
        private final ProgressBar pbGoalProgress;
        private final LinearLayout layoutItemGoal;
        public GoalViewHolder(@NonNull View view) {
            super(view);
            tvGoalName =view.findViewById(R.id.tvGoalName);
            tvIconGoal=view.findViewById(R.id.tvIconGoal);
            tvGoalAmount=view.findViewById(R.id.tvGoalAmount);
            tvGoalBalance=view.findViewById(R.id.tvGoalBalance);
            tvGoalDeadline=view.findViewById(R.id.tvGoalDeadline);
            pbGoalProgress=view.findViewById(R.id.pbGoalProgress);

            layoutItemGoal=view.findViewById(R.id.layoutItemGoal);
        }
    }
}
