package com.example.appdoan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import com.example.appdoan.CardUpdateActivity;
import com.example.appdoan.Model.CardModel;
import com.example.appdoan.R;
import com.example.appdoan.Utils.Format;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private final List<CardModel> mListCard;

    private Context mContext;

    public CardAdapter(Context context, List<CardModel> mListCard) {
        this.mContext = context;
        this.mListCard = mListCard;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        final CardModel cardModel = mListCard.get(position);
        if(cardModel == null){
            return;
        }
        holder.tvCardNumber.setText(Format.formatCardNumber(cardModel.getCardnumber()));
        holder.tvCardBalance.setText(Format.formatNumber(cardModel.getBalance()) + " VND");
        holder.tvCardName.setText(cardModel.getName());
        holder.tvCardDescription.setText(cardModel.getDescription());

        holder.layoutItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToUpdate(cardModel);
            }
        });
    }

    private void onClickGoToUpdate(CardModel cardModel)
    {
        Intent intent = new Intent(mContext, CardUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("card_object", cardModel);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(mListCard != null)
            return mListCard.size();
        return 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{

        private final RelativeLayout layoutItemCard;
        private final TextView tvCardNumber, tvCardBalance, tvCardName, tvCardDescription;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItemCard = itemView.findViewById(R.id.cardLayout);
            tvCardNumber = itemView.findViewById(R.id.cardNumber);
            tvCardBalance = itemView.findViewById(R.id.cardBalance);
            tvCardName = itemView.findViewById(R.id.cardName);
            tvCardDescription = itemView.findViewById(R.id.cardDescription);
        }
    }
}

