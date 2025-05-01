package com.example.appdoan.Adapter;

import android.content.Context;
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

import com.example.appdoan.GoalDepositActivity;
import com.example.appdoan.GoalDetailActivity;
import com.example.appdoan.GoalUpdateActivity;
import com.example.appdoan.Model.GoalModel;
import com.example.appdoan.R;
import com.example.appdoan.Utils.Format;
import com.example.appdoan.Utils.FormatDate;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    private List<GoalModel> listGoal;
    private Context context;

    // Drawable resources reused
    private Drawable goalDrawable;

    public GoalAdapter(Context context, List<GoalModel> listGoal) {
        this.context = context;
        this.listGoal = listGoal;

        // Preload the drawable for reuse
        goalDrawable = context.getDrawable(R.drawable.ic_goal); // Replace with actual drawable resource
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        final GoalModel goalModel = listGoal.get(position);
        Double amount = Double.valueOf(goalModel.getAmount());
        Double balance = Double.valueOf(goalModel.getBalance());
        Date currentTime = new Date();
        Date deadline = null;

        try {
            deadline = FormatDate.formatDate(goalModel.getDeadline());
        } catch (ParseException e) {
            e.printStackTrace();  // Handle exception more gracefully
        }

        int progress = (int) ((balance / amount) * 100);
        int compareTime = currentTime.compareTo(deadline);

        holder.tvGoalName.setText(goalModel.getName());
        holder.tvGoalAmount.setText(Format.formatNumber(goalModel.getAmount()));

        try {
            holder.tvGoalDeadline.setText(FormatDate.ChangeFormatToDMY(goalModel.getDeadline()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Handle deadline and balance status
        if (compareTime > 0) {
            holder.tvIconGoal.setBackground(goalDrawable);
            holder.tvGoalBalance.setText("Quá hạn");
            holder.tvGoalBalance.setTextColor(Color.RED);
        } else if (balance >= amount) {
            holder.tvIconGoal.setBackground(goalDrawable);
            holder.tvGoalBalance.setText("Hoàn thành");
            holder.tvGoalBalance.setTextColor(Color.GREEN);
        } else {
            holder.tvGoalBalance.setText("Đã có: " + Format.formatNumber(goalModel.getBalance()));
        }

        holder.pbGoalProgress.setProgress(progress);

        holder.layoutItemGoal.setOnClickListener(view -> onClickGoToUpdate(position));
    }

    private void onClickGoToUpdate(int position) {
        String[] options = {"Sửa", "Chi tiết", "Thêm tiền"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn một tùy chọn")
                .setItems(options, (dialog, which) -> {
                    String selected = options[which];
                    Intent intent = new Intent();

                    switch (selected) {
                        case "Sửa":
                            intent = new Intent(context, GoalUpdateActivity.class);
                            break;
                        case "Chi tiết":
                            intent = new Intent(context, GoalDetailActivity.class);
                            break;
                        case "Thêm tiền":
                            intent = new Intent(context, GoalDepositActivity.class);
                            break;
                    }

                    intent.putExtra("position", position);
                    context.startActivity(intent);
                });

        builder.create().show();
    }


    @Override
    public int getItemCount() {
        return (listGoal != null) ? listGoal.size() : 0;
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvGoalName, tvGoalAmount, tvGoalBalance, tvGoalDeadline, tvIconGoal;
        private final ProgressBar pbGoalProgress;
        private final LinearLayout layoutItemGoal;

        public GoalViewHolder(@NonNull View view) {
            super(view);
            tvGoalName = view.findViewById(R.id.tvGoalName);
            tvIconGoal = view.findViewById(R.id.tvIconGoal);
            tvGoalAmount = view.findViewById(R.id.tvGoalAmount);
            tvGoalBalance = view.findViewById(R.id.tvGoalBalance);
            tvGoalDeadline = view.findViewById(R.id.tvGoalDeadline);
            pbGoalProgress = view.findViewById(R.id.pbGoalProgress);
            layoutItemGoal = view.findViewById(R.id.layoutItemGoal);
        }
    }
}
