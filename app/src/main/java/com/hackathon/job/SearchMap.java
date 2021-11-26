package com.hackathon.job;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.hackathon.adapter.LocationAdapter;
import com.hackathon.model.kakao.category_search.CategoryResult;
import com.hackathon.model.kakao.category_search.Document;
import com.hackathon.restapi.ApiInterface;
import com.hackathon.restapi.kakao.ApiClient;
import com.hackathon.util.BusProvider;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.otto.Bus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMap extends AppCompatActivity {

    private TextInputEditText et_main_worker_place;
    private RecyclerView rv_main_worker_placelist;

    ArrayList<Document> documentArrayList = new ArrayList<>(); //지역명 검색 결과 리스트

    int flag;


    private double currentLng; //Long = X, Lat = Yㅌ
    private double currentLat;


    String TAG = "SearchMAP";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_map);

        Intent intent = getIntent();
        flag = intent.getIntExtra("flag",0);


        initView();
    }

    void initView(){
        et_main_worker_place = findViewById(R.id.et_main_worker_place);
        rv_main_worker_placelist = findViewById(R.id.rv_main_worker_placelist);
        LocationAdapter locationAdapter = new LocationAdapter(documentArrayList, this, et_main_worker_place, rv_main_worker_placelist, flag);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성


        rv_main_worker_placelist.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); //아래구분선 세팅
        rv_main_worker_placelist.setLayoutManager(layoutManager);
        rv_main_worker_placelist.setAdapter(locationAdapter);



        et_main_worker_place.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rv_main_worker_placelist.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                if (charSequence.length() >= 1){

                    documentArrayList.clear();
                    locationAdapter.clear();
                    locationAdapter.notifyDataSetChanged();
                    ApiInterface apiInterface = ApiClient.getAPiClient().create(ApiInterface.class);
                    Log.i(TAG, String.valueOf(currentLat));
                    Call<CategoryResult> call = apiInterface.getSearchLocationDetail(getString(R.string.KAKAO_REST_API_KEY), charSequence.toString(),
                            String.valueOf(currentLng), String.valueOf(currentLat),15);
                    call.enqueue(new Callback<CategoryResult>(){
                        @Override
                        public void onResponse(retrofit2.Call<CategoryResult> call, Response<CategoryResult> response) {

                            if(response.isSuccessful()){
                                assert response.body() != null;
                                for (Document document: response.body().getDocuments()){

                                    locationAdapter.addItem(document);
                                }
                                locationAdapter.notifyDataSetChanged();
                            }
                            else{
                                Log.i(TAG, response.body().toString());
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<CategoryResult> call, Throwable t) {
                            Log.i(TAG,"map fail");
                        }
                    });
                }else{
                    if(charSequence.length() <= 0){
                        rv_main_worker_placelist.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력 종료
            }
        });

        et_main_worker_place.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){

                }else{
                    rv_main_worker_placelist.setVisibility(View.GONE);
                }
            }
        });

        et_main_worker_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FancyToast.makeText(SearchMap.this, "검색리스트에서 장소를 선택해주세요.", FancyToast.LENGTH_SHORT, FancyToast.INFO,true).show();
            }
        });

    }
}
