package com.hackathon.job;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hackathon.fragment.SubOwnerFragment;
import com.hackathon.room.AnnounceCard.AnnounceCard;
import com.hackathon.room.AnnounceCard.AnnounceCardDao;
import com.hackathon.room.AppDatabase;
import com.hackathon.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kotlin.jvm.JvmWildcard;

public class MainOwnerFragment extends Fragment {

    Button btn_main_owner_announce;
    View view;
    TextView tv_main_owner_count_1;
    TextView tv_main_owner_count_2;
    TextView tv_main_owner_count_3;

    LinearLayout ll_main_owner_count_1;
    LinearLayout ll_main_owner_count_2;
    LinearLayout ll_main_owner_count_3;

    AppDatabase db;

    public MainOwnerFragment(){

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.main_owner_fragment, container, false);

        initView();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        new AnnounceAsyncTask(db.announceCardDao(),  tv_main_owner_count_1, tv_main_owner_count_2, tv_main_owner_count_3 ).execute(Util.loginUser.getEmail());
    }

    public void initView(){
        btn_main_owner_announce = view.findViewById(R.id.btn_main_owner_announce);

        btn_main_owner_announce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SubListActivity.class);
                startActivity(intent);
            }
        });

        tv_main_owner_count_1 = view.findViewById(R.id.tv_main_owner_count_1);
        tv_main_owner_count_2 = view.findViewById(R.id.tv_main_owner_count_2);
        tv_main_owner_count_3 = view.findViewById(R.id.tv_main_owner_count_3);

        ll_main_owner_count_1 = view.findViewById(R.id.ll_main_owner_count_1);
        ll_main_owner_count_2 = view.findViewById(R.id.ll_main_owner_count_2);
        ll_main_owner_count_3 = view.findViewById(R.id.ll_main_owner_count_3);

        ll_main_owner_count_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AlbaCardActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
            }
        });

        ll_main_owner_count_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AlbaCardActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
            }
        });

        db = AppDatabase.getInstance(view.getContext());

        new AnnounceAsyncTask(db.announceCardDao(),   tv_main_owner_count_1, tv_main_owner_count_2, tv_main_owner_count_3 ).execute(Util.loginUser.getEmail());

    }

    //메인스레드에서 AnnounceAsyncTask 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class AnnounceAsyncTask extends AsyncTask<String, Void, Void> {
        private AnnounceCardDao announceCardDao;
        List<AnnounceCard> cards;
        private TextView tv_main_owner_count_1;
        private TextView tv_main_owner_count_2;
        private TextView tv_main_owner_count_3;


        public AnnounceAsyncTask(AnnounceCardDao announceCardDao, TextView tv_main_owner_count_1,
        TextView tv_main_owner_count_2, TextView tv_main_owner_count_3) {
            this.announceCardDao = announceCardDao;
            this.tv_main_owner_count_1 = tv_main_owner_count_1;
            this.tv_main_owner_count_2 = tv_main_owner_count_2;
            this.tv_main_owner_count_3 = tv_main_owner_count_3;

        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(String... email) {


            cards = announceCardDao.getMyAnnounceCard(email[0]);

            Util.announceCard = cards;

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            int flag0 = 0;
            int flag1 = 0;
            int flag2 = 0;


            for (int i = 0 ; i < cards.size(); i++){
                AnnounceCard card = cards.get(i);
                int state = card.getState();
                int work_flag = card.getWorkFlag();
                if (state == 0 && work_flag == 1){
                    flag0 += 1;
                }else if(state == 1 && work_flag == 1){
                    flag1 += 1;
                }else if(state == 1 && work_flag == 1){
                    flag2 += 1;
                }


            }



            tv_main_owner_count_1.setText(""+flag0);
            tv_main_owner_count_2.setText(""+flag1);
            tv_main_owner_count_3.setText(""+flag2);


        }
    }




}
