package com.hackathon.job;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;

public class PreActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_activity);



//        try {
//            sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Intent intent = new Intent(PreActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
