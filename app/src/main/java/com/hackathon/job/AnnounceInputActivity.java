package com.hackathon.job;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import net.daum.mf.map.api.MapView;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class AnnounceInputActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener callbackMethod;
    private Button btn_announce_daytime;
    private TextView tv_announce_date;
    private TextView tv_announce_time;
    private RangeSlider rs_announce_time;



    private String StartTime;
    private String finishTime;
    private String TAG = "@ANNO";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announceinputactivity);

        initView();
    }

    public void initView(){
        btn_announce_daytime = findViewById(R.id.btn_announce_daytime);

        tv_announce_date = findViewById(R.id.tv_announce_date);
        tv_announce_time = findViewById(R.id.tv_announce_time);

        rs_announce_time = findViewById(R.id.rs_announce_time);


//        callFragment();
        rs_announce_time.setLabelFormatter(new LabelFormatter() {
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
        rs_announce_time.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> range = slider.getValues();

                String startMinute = "";
                String finishMinute = "";
                Float start = range.get(0);
                Float finish = range.get(1);
                int starthour = (int)Math.floor(start);
                int finishhour = (int)Math.floor(finish);

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

                String startTime = String.format("%02d",starthour) +":"+ startMinute;
                String finishTime = String.format("%02d",finishhour) +":"+ finishMinute;

                Log.i(TAG, startTime + " " + finishTime );
            }
        });

        btn_announce_daytime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                LocalDate now = LocalDate.now();

                int year = now.getYear();
                int month = now.getMonthValue() - 1;
                int dayofmonth = now.getDayOfMonth();

                DatePickerDialog dialog = new DatePickerDialog(AnnounceInputActivity.this, callbackMethod, year,month,dayofmonth);
                dialog.show();
            }
        });

        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                tv_announce_date .setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }

        };


    }

//    private void callFragment(){
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//
//        MainWorkerFragment mainWorkerFragment = new MainWorkerFragment();
//        transaction.replace(R.id.announce_fragment_container,mainWorkerFragment);
//        transaction.commit();
//
//
//    }
}
