package com.hackathon.job;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.hackathon.adapter.SubPagerAdapter;
import com.hackathon.fragment.AlbaCardOwnerFragment;
import com.hackathon.fragment.AlbaCardWorkerFragment;
import com.hackathon.fragment.MyInfoFragment;
import com.hackathon.fragment.MyReviewFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MyInfoActivity extends AppCompatActivity {
    BottomNavigationView bn_my_info_position;
    MyInfoFragment myInfoFragment;
    MyReviewFragment myReviewFragment;

    SubPagerAdapter subPagerAdapter;
    ViewPager vp_my_info_pager_content;
    TabLayout tl_my_info_tab;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState  ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo_activity);
        createFragment();
        createViewpager();
        settingTabLayout();
        initView();
    }

    public void initView(){
        bn_my_info_position = findViewById(R.id.bn_my_info_position);
        bn_my_info_position.getMenu().getItem(2).setChecked(true);
        bn_my_info_position.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.page_1){
                    Intent intent = new Intent(MyInfoActivity.this, TestActivity.class);
                    startActivity(intent);
                    finish();
                }else if (id == R.id.page_2){
                    Intent intent = new Intent(MyInfoActivity.this, AlbaCardActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
    }


    public void createFragment() {
        myInfoFragment = new MyInfoFragment();
        myReviewFragment = new MyReviewFragment();
    }

    public void createViewpager(){
        vp_my_info_pager_content = findViewById(R.id.vp_my_info_pager_content);
        subPagerAdapter = new SubPagerAdapter(getSupportFragmentManager());
        subPagerAdapter.addFragment(myInfoFragment);
        subPagerAdapter.addFragment(myReviewFragment);

        vp_my_info_pager_content.setAdapter(subPagerAdapter);
        vp_my_info_pager_content.setCurrentItem(0);
    }

    public void settingTabLayout(){
        tl_my_info_tab = findViewById(R.id.tl_my_info_tab);


        vp_my_info_pager_content.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_my_info_tab));

        tl_my_info_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                vp_my_info_pager_content.setCurrentItem(pos);
                if (pos == 0){
                    vp_my_info_pager_content.setCurrentItem(0);

                }else{
                    vp_my_info_pager_content.setCurrentItem(1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}
