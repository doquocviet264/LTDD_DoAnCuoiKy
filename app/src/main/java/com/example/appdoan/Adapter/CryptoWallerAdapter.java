package com.example.appdoan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appdoan.R;
import com.example.appdoan.Model.CryptoWallet;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CryptoWallerAdapter extends RecyclerView.Adapter<CryptoWallerAdapter.CryptoViewHolder> {

    // Class parameters
    ArrayList<CryptoWallet> cryptoWallets;
    DecimalFormat formatter;

    // Constructor


    public CryptoWallerAdapter(ArrayList<CryptoWallet> cryptoWallets) {
        this.cryptoWallets = cryptoWallets;
        formatter = new DecimalFormat("###,###,###,###.##");
    }

    // View Holder
    class CryptoViewHolder extends RecyclerView.ViewHolder{
        TextView cryptoSymbolText,cryptoPercentText,cryptoBalanceText,propertyAmountText;
        ImageView logoCrypto;

        public CryptoViewHolder(@NonNull View itemView) {
            super(itemView);
            cryptoSymbolText = itemView.findViewById(R.id.symbolText);
            cryptoBalanceText = itemView.findViewById(R.id.balanceText);
            cryptoPercentText = itemView.findViewById(R.id.percentText);
            propertyAmountText = itemView.findViewById(R.id.amountText);
            logoCrypto=itemView.findViewById(R.id.pic);

        }
    }

    // implement Methods

    @NonNull
    @Override
    public CryptoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_item,parent,false);
        return new CryptoViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CryptoViewHolder holder, int position) {
        holder.cryptoSymbolText.setText(cryptoWallets.get(position).getCryptoSymbol());
        holder.cryptoBalanceText.setText("$"+formatter.format(cryptoWallets.get(position).getCryptoBalance()));
        holder.cryptoPercentText.setText(cryptoWallets.get(position).getChangePercent()+"%");
        holder.propertyAmountText.setText(cryptoWallets.get(position).getPropertyAmount()+" "+cryptoWallets.get(position).getCryptoSymbol());

        int drawableResourceID = holder.itemView.getContext().getResources()
                .getIdentifier(cryptoWallets.get(position).getPicUrl(),"drawable",holder.itemView.getContext().getPackageName());



        // Glide

        Glide
                .with(holder.itemView.getContext())
                .load(drawableResourceID)
                .into(holder.logoCrypto);

        try {
            if(cryptoWallets.get(position).getCryptoBalance()>=0){
                holder.cryptoPercentText.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.green));
            }else{
                holder.cryptoPercentText.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red));
            }
        }catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return cryptoWallets.size();
    }
}
