package com.hackathon.job;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hackathon.adapter.MainPagerAdapter;
import com.hackathon.room.User.User;
import com.hackathon.room.User.UserDao;
import com.hackathon.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private TabLayout tl_test_tab;

    private ViewPager vp_test_pager_content;
    private MainOwnerFragment mainOwnerFragment;
    private MainWorkerFragment mainWorkerFragment;
    private MainPagerAdapter mainPagerAdapter;
    private BottomNavigationView bn_test_position;

    private Button btn_user_info;
    private Button btn_balance_info;

    private TextView tv_test_name;
    private TextView tv_test_payment;

    private ShapeableImageView test_circle_image;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);


        createFragment();
        createViewpager();
        settingTabLayout();
        initView();

    }

    public void initView(){

        tv_test_name = findViewById(R.id.tv_test_name);
        tv_test_payment = findViewById(R.id.tv_test_payment);

        tv_test_name.setText(Util.loginUser.getName());
//        int money = Util.getInt(, )
        tv_test_payment.setText(Util.loginUser.getMoney() + "원");
        bn_test_position = findViewById(R.id.bn_test_position);

        test_circle_image = findViewById(R.id.test_circle_image);

        if (Util.loginUser.getName().equals("구인자")) {
            test_circle_image.setImageResource(R.drawable.gs25);
        }else{
            test_circle_image.setImageResource(R.drawable.ebichu);
        }
        bn_test_position.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.page_1){

                }else if (id == R.id.page_2){
                    Intent intent = new Intent(TestActivity.this, AlbaCardActivity.class);
                    intent.putExtra("flag",vp_test_pager_content.getCurrentItem());
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(TestActivity.this, MyInfoActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        btn_user_info = findViewById(R.id.btn_user_info);

        btn_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this, MyInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_balance_info = findViewById(R.id.btn_balance_info);

        btn_balance_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this, BalanceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //fragment 생성
    public void createFragment(){
        mainOwnerFragment = new MainOwnerFragment();
        mainWorkerFragment = new MainWorkerFragment();
    }

    public void createViewpager(){
        vp_test_pager_content = findViewById(R.id.vp_test_pager_content);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPagerAdapter.addFragment(mainWorkerFragment);
        mainPagerAdapter.addFragment(mainOwnerFragment);

        vp_test_pager_content.setAdapter(mainPagerAdapter);

    }

    public void settingTabLayout(){

        tl_test_tab = findViewById(R.id.tl_test_tab);


        vp_test_pager_content.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_test_tab));

        tl_test_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                vp_test_pager_content.setCurrentItem(pos);
                if (pos == 0){
                    vp_test_pager_content.setCurrentItem(0);

                }else{
                    vp_test_pager_content.setCurrentItem(1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        final List<String> tabElement = Arrays.asList("일하기","사람구하기");

//        //tabLyout와 viewPager 연결
//        new TabLayoutMediator(tl_test_tab, vp_test_pager_content, new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
//                TextView textView = new TextView(TestActivity.this);
//////                textView.setTextColor(Color.parseColor("#000000"));
//                textView.setText(tabElement.get(position));
//                tab.setCustomView(textView);
//
//            }
//        }).attach();

    }




}
