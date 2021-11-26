package com.hackathon.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hackathon.adapter.AlbaCardOwnerAdapter;
import com.hackathon.adapter.AlbaCardWorkerAdapter;
import com.hackathon.job.R;
import com.hackathon.room.AnnounceCard.AnnounceCard;
import com.hackathon.room.AnnounceCard.AnnounceCardDao;
import com.hackathon.room.AppDatabase;
import com.hackathon.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AlbaCardWorkerFragment extends Fragment {

    View view;

    private RecyclerView rv_alba_card_worker_list;

    public AlbaCardWorkerFragment(){

    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alba_card_worker_flagment, container, false);

        initView();


        return view;
    }

    public void initView(){
        rv_alba_card_worker_list = view.findViewById(R.id.rv_alba_card_worker_list);

        AppDatabase db = AppDatabase.getInstance(view.getContext());


        new AnnounceAsyncTask(db.announceCardDao(), view).execute(Util.loginUser.getEmail());



    }

    //메인스레드에서 AnnounceAsyncTask 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class AnnounceAsyncTask extends AsyncTask<String, Void, Void> {
        private AnnounceCardDao announceCardDao;
        List<AnnounceCard> cards;
        View view;

        public AnnounceAsyncTask(AnnounceCardDao announceCardDao, View view) {
            this.announceCardDao = announceCardDao;
            this.view = view;


        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(String... email) {


            cards = announceCardDao.getMyAnnounceFlagCard(email[0], 0);




            Util.announceCard = cards;

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            RecyclerView rv_alba_card_worker_list = view.findViewById(R.id.rv_alba_card_worker_list);
            AlbaCardWorkerAdapter albaCardWorkerAdapter = new AlbaCardWorkerAdapter(cards, view.getContext(), rv_alba_card_worker_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성

            rv_alba_card_worker_list.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL)); //아래구분선 세팅
            rv_alba_card_worker_list.setLayoutManager(layoutManager);
            rv_alba_card_worker_list.setAdapter(albaCardWorkerAdapter);

        }
    }
}
