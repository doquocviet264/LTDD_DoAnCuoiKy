package com.example.appdoan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Container.Response.ApiResponse;
import com.example.appdoan.Model.NotificationModel;
import com.example.appdoan.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private Context context;

    private final List<NotificationModel> mListNotification;

    public NotificationAdapter(Context context, List<NotificationModel> mListNotification) {
        this.context = context;
        this.mListNotification = mListNotification;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notificationModel = mListNotification.get(position);
        if(notificationModel == null)
        {
            return;
        }
        holder.notiTitle.setText(notificationModel.getTitle());
        holder.notiContent.setText(notificationModel.getContent());
        holder.notiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickGoToDelete(notificationModel);
            }
        });
    }

    private void OnClickGoToDelete(NotificationModel notificationModel)
    {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<ApiResponse<Object>> call = httpRequest.deleteNotification(notificationModel.getId());
        call.enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if(response.isSuccessful())
                {
                    ApiResponse apiResponse = response.body();
                    if(apiResponse.getStatus() == 200)
                    {
                        Toast.makeText(context, "Cảm ơn đã đọc thông báo!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        if(mListNotification != null)
        {
            return mListNotification.size();
        }
        return 0;
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        private final LinearLayout notiLayout;

        private final TextView notiTitle, notiContent;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            notiLayout = itemView.findViewById(R.id.layoutNoti);
            notiTitle = itemView.findViewById(R.id.titleNoti);
            notiContent = itemView.findViewById(R.id.contentNoti);
        }
    }
}
