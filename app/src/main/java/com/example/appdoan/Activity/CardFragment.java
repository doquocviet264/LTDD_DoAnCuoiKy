package com.example.appdoan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appdoan.API.HTTPRequest;
import com.example.appdoan.API.HTTPService;
import com.example.appdoan.Adapter.CardAdapter;
import com.example.appdoan.CardCreateActivity;
import com.example.appdoan.Helper.LoadingDialog;
import com.example.appdoan.Model.CardModel;
import com.example.appdoan.databinding.FragmentCardBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CardFragment extends Fragment {

    private LoadingDialog loadingDialog;
    private FragmentCardBinding binding;
    private RecyclerView rcvCardList;
    private List<CardModel> mListCard;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadingDialog = new LoadingDialog(getActivity());

        rcvCardList = binding.cardRecycleView;
        rcvCardList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvCardList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mListCard = new ArrayList<>();
        getAllCard();

        binding.btnCardCreate.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CardCreateActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void getAllCard() {
        Retrofit retrofit = HTTPService.getInstance();
        HTTPRequest httpRequest = retrofit.create(HTTPRequest.class);
        Call<List<CardModel>> call = httpRequest.getAllCard();
        call.enqueue(new Callback<List<CardModel>>() {
            @Override
            public void onResponse(Call<List<CardModel>> call, Response<List<CardModel>> response) {
                mListCard = response.body();
                if (mListCard != null) {
                    CardAdapter cardAdapter = new CardAdapter(getActivity(), mListCard);
                    rcvCardList.setAdapter(cardAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CardModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
