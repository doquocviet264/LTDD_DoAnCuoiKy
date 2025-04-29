package com.example.appdoan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appdoan.Adapter.CryptoWallerAdapter;
import com.example.appdoan.Model.CryptoWallet;
import com.example.appdoan.TransactionExpenseActivity;
import com.example.appdoan.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Khởi tạo RecyclerView
        RecyclerViewInit();

        // Xử lý sự kiện khi click vào nút btnExpense
        binding.btnExpense.setOnClickListener(v -> {
            // Chuyển sang màn hình nhập khoản chi
            Intent intent = new Intent(getActivity(), TransactionExpenseActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void RecyclerViewInit() {
        binding.list.setLayoutManager(new GridLayoutManager(getContext(), 2));

        ArrayList<CryptoWallet> cryptoWalletArrayList = new ArrayList<>();
        cryptoWalletArrayList.add(new CryptoWallet("BTC", "btc", 2.13, 1.4, 1423.33));
        cryptoWalletArrayList.add(new CryptoWallet("ETH", "eth", -1.3, 4.5, 233.4));
        cryptoWalletArrayList.add(new CryptoWallet("XRP", "xrp", -3.14, 2.4, 235.32));
        cryptoWalletArrayList.add(new CryptoWallet("LTC", "ltc", 4.45, 3.5, 23423.44));

        binding.list.setAdapter(new CryptoWallerAdapter(cryptoWalletArrayList));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
