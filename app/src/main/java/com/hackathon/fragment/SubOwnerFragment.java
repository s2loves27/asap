package com.hackathon.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.AccessNetworkConstants;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;
import com.hackathon.adapter.WorkTypeAdapter;
import com.hackathon.job.MainWorkerFragment;
import com.hackathon.job.R;
import com.hackathon.job.SearchMap;
import com.hackathon.model.kakao.category_search.Document;
import com.hackathon.room.AnnounceCard.AnnounceCard;
import com.hackathon.room.AnnounceCard.AnnounceCardDao;
import com.hackathon.room.AppDatabase;
import com.hackathon.room.worktype.Type;
import com.hackathon.room.worktype.TypeDao;
import com.hackathon.util.BusProvider;
import com.hackathon.util.BusProvider2;
import com.hackathon.util.Util;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubOwnerFragment extends Fragment {

    View view;
//    TextInputEditText et_sub_owner_worktype;
//    ArrayList<Type> documentArrayList = new ArrayList<>(); //업종 list
    String TAG = "@SubOwnerFragment";
//    RecyclerView rv_sub_owner_worktype;


    LinearLayout ll_sub_owner_require;

    ArrayList<Type> types;


    TextView tv_sub_worker_time;





    // 데이터 정보 관련
    // 공고명
    TextInputEditText et_sub_owner_title;
    // 주소
    TextView tv_sub_owner_place;
    Button bt_sub_owner_place;
    //업무시간
    RangeSlider rs_sub_owner_time;
    //임금
    TextInputEditText et_sub_owner_payment;
    Button bt_sub_owner_payment;
    // 업무내용
    Spinner sn_sub_owner_type;
    // 기타
    TextInputEditText et_sub_owner_desc;
    // 자격요건
    Button btn_sub_owner_require;
    CheckBox rb_sub_owner_1;
    CheckBox rb_sub_owner_2;
    CheckBox rb_sub_owner_3;
    CheckBox rb_sub_owner_4;
    CheckBox rb_sub_owner_5;
    CheckBox rb_sub_owner_6;
    CheckBox rb_sub_owner_7;
    CheckBox rb_sub_owner_8;
    CheckBox rb_sub_owner_9;
    CheckBox rb_sub_owner_10;

    // 근로 기준법
    CheckBox cb_sub_owner_standard;

    // 공고 등록
    Button btn_sub_owner_confirm;

    // 저장 Data
    String sTime = "";
    String fTime = "";
    String place = "";
    double placeLng = -1;
    double placeLat = -1;
    String startTime = "00:00";
    String finishTime = "23:59";
    String address = "";

    Bus bus = BusProvider2.getInstance();

    public SubOwnerFragment() {

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sub_owner_fragment, container, false);


        bus.register(this);
        initView();
        confirm();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bus.unregister(this);
    }

    public void initView() {

        et_sub_owner_title = view.findViewById(R.id.et_sub_owner_title);
        tv_sub_owner_place = view.findViewById(R.id.tv_sub_owner_place);
        bt_sub_owner_place = view.findViewById(R.id.bt_sub_owner_place);

        bt_sub_owner_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchMap.class);
                intent.putExtra("flag",1);
                startActivity(intent);
            }
        });


        et_sub_owner_payment = view.findViewById(R.id.et_sub_owner_payment);
        bt_sub_owner_payment = view.findViewById(R.id.bt_sub_owner_payment);

        bt_sub_owner_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_sub_owner_payment.setText("9160");
            }
        });


        et_sub_owner_desc = view.findViewById(R.id.et_sub_owner_desc);

        rb_sub_owner_1 = view.findViewById(R.id.rb_sub_owner_1);
        rb_sub_owner_2 = view.findViewById(R.id.rb_sub_owner_2);
        rb_sub_owner_3 = view.findViewById(R.id.rb_sub_owner_3);
        rb_sub_owner_4 = view.findViewById(R.id.rb_sub_owner_4);
        rb_sub_owner_5 = view.findViewById(R.id.rb_sub_owner_5);
        rb_sub_owner_6 = view.findViewById(R.id.rb_sub_owner_6);
        rb_sub_owner_7 = view.findViewById(R.id.rb_sub_owner_7);
        rb_sub_owner_8 = view.findViewById(R.id.rb_sub_owner_8);
        rb_sub_owner_9 = view.findViewById(R.id.rb_sub_owner_9);
        rb_sub_owner_10 = view.findViewById(R.id.rb_sub_owner_10);

        cb_sub_owner_standard = view.findViewById(R.id.cb_sub_owner_standard);

//        et_sub_owner_worktype = view.findViewById(R.id.et_sub_owner_worktype);
//        rv_sub_owner_worktype = view.findViewById(R.id.rv_sub_owner_worktype);
//        WorkTypeAdapter workTypeAdapter = new WorkTypeAdapter(documentArrayList, view.getContext(), et_sub_owner_worktype, rv_sub_owner_worktype);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성

//        rv_sub_owner_worktype.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL)); //아래구분선 세팅
//        rv_sub_owner_worktype.setLayoutManager(layoutManager);
//        rv_sub_owner_worktype.setAdapter(workTypeAdapter);

//        et_sub_owner_worktype.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                rv_sub_owner_worktype.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//
//                if (charSequence.length() >= 1) {
//
//                    documentArrayList.clear();
//                    workTypeAdapter.clear();
//                    workTypeAdapter.notifyDataSetChanged();
//
//                    AppDatabase db = AppDatabase.getInstance(view.getContext());
////                    AppDatabase db = Room.databaseBuilder(view.getContext(),
////                            AppDatabase.class, "asap").build();
//
//                    new DbTypeAsyncTask(db.typeDao(),workTypeAdapter).execute(charSequence.toString());
//
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                //입력 종료
//            }
//        });


//        et_sub_owner_worktype.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//
//                } else {
//                    rv_sub_owner_worktype.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        et_sub_owner_worktype.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FancyToast.makeText(view.getContext(), "업종을 선택해 주세요.", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
//            }
//        });

        sn_sub_owner_type = view.findViewById(R.id.sn_sub_owner_type);

        String[] types = getResources().getStringArray(R.array.type_data);

        ArrayList<String> types2 = new ArrayList<String>(Arrays.asList(types));

        //ArryaAdapter 객체 생성. 여기서는 List로 만든걸로 써 봄
        ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item,types2);
//        adapter.setDropDownViewResource(R.layout.work_type_item);
        sn_sub_owner_type.setAdapter(adapter);


//        sn_sub_owner_type.setOnItemClickListener(n);

        btn_sub_owner_require = view.findViewById(R.id.btn_sub_owner_require);
        ll_sub_owner_require = view.findViewById(R.id.ll_sub_owner_require);

        btn_sub_owner_require.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ch = ll_sub_owner_require.getVisibility();
                if (ch == View.GONE) {
                    ll_sub_owner_require.setVisibility(View.VISIBLE);
                    Button button = (Button)view;
                    button.setText("원하는 지원자 자격 요건이 있습니다. ▲ ");
                }else{
                    ll_sub_owner_require.setVisibility(View.GONE);
                    Button button = (Button)view;
                    button.setText("원하는 지원자 자격 요건이 있습니다. ▼ ");
                }
            }
        });

        tv_sub_worker_time = view.findViewById(R.id.tv_sub_worker_time);

        rs_sub_owner_time = view.findViewById(R.id.rs_sub_owner_time);

        rs_sub_owner_time.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @NotNull
            @Override
            public String getFormattedValue(float value) {
                String time = "";
                if (value < 1){
                    time += "오전 12시";
                }
                else if (value < 13){
                    time = "오전 " + (int)value + "시";

                }

                else if(value < 23){
                    time = "오후 " + (int)(value - 12) + "시";
                }

                else if (value == 24){
                    time += "오후 11시 59분";
                }

                if ((value - (int)value) != 0){
                    time += " 30분";
                }

                return time;
            }
        });
        rs_sub_owner_time.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> range = slider.getValues();

                String startMinute = "";
                String finishMinute = "";
                String workMinute = "";

                Float start = range.get(0);
                Float finish = range.get(1);
                Float workTime = finish - start;

                int starthour = (int)Math.floor(start);
                int finishhour = (int)Math.floor(finish);
                int workHour = (int)Math.floor(workTime);


                if ((start - starthour) != 0){
                    startMinute = "30";
                }else{
                    startMinute = "00";
                }
                if ((finish - finishhour) != 0){
                    finishMinute = "30";
                }else{
                    finishMinute = "00";
                }
                if((workTime - workHour) != 0){
                    workMinute  = "30";
                }

                startTime = String.format("%02d",starthour) +":"+ startMinute;
                finishTime = String.format("%02d",finishhour) +":"+ finishMinute;
                String Time = "";
                if (workMinute != "") {
                    Time = String.format("%02d", workHour) + "시간 " + workMinute + "분";
                }else{
                    Time = String.format("%02d", workHour) + "시간";
                }
                tv_sub_worker_time.setText(Time);
                Log.i(TAG, startTime + " " + finishTime );
            }
        });
    }

    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class AnnounceAsyncTask extends AsyncTask<AnnounceCard, Void, Void> {
        private AnnounceCardDao announceCardDao;


        public AnnounceAsyncTask(AnnounceCardDao announceCardDao) {
            this.announceCardDao = announceCardDao;

        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(AnnounceCard... announceCards) {
            //추가만하고 따로 SELECT문을 안해도 라이브데이터로 인해
            //getAll()이 반응해서 데이터를 갱신해서 보여줄 것이다,  메인액티비티에 옵저버에 쓴 코드가 실행된다. (라이브데이터는 스스로 백그라운드로 처리해준다.)


//            Type type = new Type();
//
//            type.setBigtype("알바");
//            type.setSmalltype("편의점");
//
//            typedao.insert(type);
//
//            type = new Type();
//
//            type.setBigtype("알바");
//            type.setSmalltype("카페");
//
//            typedao.insert(type);
//
//            Type[] types = typedao.getItem(item[0]);
//            Log.i("@SubOwnerFragment", ""+ types.length);
//            for (Type t : types) {
//                workTypeAdapter.addItem(t);
//            }

//            publishProgress();


            announceCardDao.insert(announceCards[0]);

            List<AnnounceCard> card = announceCardDao.getALL();

            for (int i = 0 ; i < card.size(); i++){
                Log.i("JUN",card.get(i).getCategory());
                Log.i("JUN",card.get(i).getQualify().toString());

            }
            return null;
        }

//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//
//            workTypeAdapter.notifyDataSetChanged();
//        }
    }
    public void confirm(){
        btn_sub_owner_confirm = view.findViewById(R.id.btn_sub_owner_confirm);

        btn_sub_owner_confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String title = "";
                title = et_sub_owner_title.getText().toString();


                String strPayment =  et_sub_owner_payment.getText().toString();
                int payment;
                if (strPayment.equals("") || strPayment == null){
                    payment = 0;
                }
                payment = Integer.parseInt(strPayment);


                String workType = sn_sub_owner_type.getSelectedItem().toString();
                String desc = et_sub_owner_desc.getText().toString();


                ArrayList<Boolean> qualify = new ArrayList<Boolean>();
                qualify.add(rb_sub_owner_1.isChecked());
                qualify.add(rb_sub_owner_2.isChecked());
                qualify.add(rb_sub_owner_3.isChecked());
                qualify.add(rb_sub_owner_4.isChecked());
                qualify.add(rb_sub_owner_5.isChecked());
                qualify.add(rb_sub_owner_6.isChecked());
                qualify.add(rb_sub_owner_7.isChecked());
                qualify.add(rb_sub_owner_8.isChecked());
                qualify.add(rb_sub_owner_9.isChecked());
                qualify.add(rb_sub_owner_10.isChecked());

                boolean stardard = cb_sub_owner_standard.isChecked();


                String flag = checkConfirm1(title, place ,startTime, finishTime, payment, workType, desc, stardard);
                if (flag != "") {
                    announce_fail_1(flag);
                    return;
                }

                LocalDate now = LocalDate.now();

                AppDatabase db = AppDatabase.getInstance(view.getContext());

                AnnounceCard announceCard = new AnnounceCard();

                announceCard.setState(0);
                announceCard.setTitle(title);
                announceCard.setLocation(place);
                announceCard.setAddress(address);
                announceCard.setLat(placeLat);
                announceCard.setLng(placeLng);
                announceCard.setDate(now.toString());
                announceCard.setStartTime(startTime);
                announceCard.setFinishTime(finishTime);
                announceCard.setPayment(payment);
                announceCard.setCategory(workType);
                announceCard.setDesc(desc);
                announceCard.setQualify(qualify);
                announceCard.setUserName(Util.loginUser.getEmail());
                announceCard.setLevel(0);
                //workFlag - 0(worker)/ 1 (구인자)
                announceCard.setWorkFlag(1);
                announceCard.setScore(Util.loginUser.getScore());


                new AnnounceAsyncTask(db.announceCardDao()).execute(announceCard);
                ((Activity)view.getContext()).finish();
            }
        });
    }

    public String checkConfirm1(String title, String place, String startTime, String finishTime,int payment, String workType, String desc, boolean standard){
        String flag = "";

        if (title.equals("") || title == null ){
            flag = "공고명이 입력 되지 않았습니다.";
        }else if (title.length() > 15){
            flag = "공고명은 15자 이상으로 작성 할 수 없습니다.";
        }else if(place.equals("") || place == null) {
            flag = "위치를 설정 해주세요.";
        }else if(startTime.equals("") || startTime == null){
            flag = "시작 시간을 설정 해주세요.";
        }else if(finishTime.equals("") || finishTime == null){
            flag = "끝 사간을 설정 해주세요.";
        }else if(payment == 0){
            flag = "시급을 입력 해주세요.";
        }
        else if (!standard){
            flag = "근로 기준법을 동의 하지 않으면 공고를 등록할 수 없습니다.";
        }


        return flag;
    }

    // 공고 등록 실패 (입력 값 미 입력)
    private void announce_fail_1(String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("공고 등록 실패");
        builder.setMessage(message);
        builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(getApplicationContext(), "예를 선택했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }


    @Subscribe //검색예시 클릭시 이벤트 오토버스
    public void search(Document document) {//public항상 붙여줘야함
        Log.i("SUbOwner","bus");
        FancyToast.makeText(view.getContext(), document.getPlaceName() + " 검색", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();

        place = document.getPlaceName();
//        document.getPhone();
        address = document.getAddressName();
        placeLat = Double.parseDouble(document.getY());
        placeLng = Double.parseDouble(document.getX());


        tv_sub_owner_place.setText(place);


//        Util.loginUser.setPlace(searchName);
//        Util.loginUser.setLng(searchLng);
//        Util.loginUser.setLat(searchLat);

//        AppDatabase db = AppDatabase.getInstance(view.getContext());


//        MainWorkerFragment.DbAsyncTask task = new MainWorkerFragment.DbAsyncTask(db.userDao(), tv_main_work_place);
//        task.execute(Util.loginUser);



    }
}
