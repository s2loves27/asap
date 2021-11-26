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
import com.google.android.material.tabs.TabLayout;
import com.hackathon.adapter.SubPagerAdapter;
import com.hackathon.fragment.AlbaCardOwnerFragment;
import com.hackathon.fragment.AlbaCardWorkerFragment;
import com.hackathon.fragment.SubOwnerFragment;
import com.hackathon.fragment.SubWorkerFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class AlbaCardActivity extends AppCompatActivity {
    BottomNavigationView bn_alba_card_position;
    AlbaCardOwnerFragment albaCardOwnerFragment;
    AlbaCardWorkerFragment albaCardWorkerFragment;

    ViewPager vp_alba_card_pager_content;
    private SubPagerAdapter subPagerAdapter;
    TabLayout tl_alba_card_tab;
    int flag = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alba_card_activity);


        Intent intent = getIntent();
        flag = intent.getIntExtra("flag",0);

        createFragment();
        createViewpager();
        settingTabLayout();
        initView();
    }

    public void initView() {
        bn_alba_card_position = findViewById(R.id.bn_alba_card_position);

        bn_alba_card_position.getMenu().getItem(flag).setChecked(true);
        bn_alba_card_position.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.page_1) {
                    Intent intent = new Intent(AlbaCardActivity.this, TestActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.page_2) {

                } else {

                    Intent intent = new Intent(AlbaCardActivity.this, MyInfoActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
    }


    public void createFragment() {
        albaCardOwnerFragment = new AlbaCardOwnerFragment();
        albaCardWorkerFragment = new AlbaCardWorkerFragment();
    }

    public void createViewpager(){
        vp_alba_card_pager_content = findViewById(R.id.vp_alba_card_pager_content);
        subPagerAdapter = new SubPagerAdapter(getSupportFragmentManager());
        subPagerAdapter.addFragment(albaCardWorkerFragment);
        subPagerAdapter.addFragment(albaCardOwnerFragment);

        vp_alba_card_pager_content.setAdapter(subPagerAdapter);
        vp_alba_card_pager_content.setCurrentItem(flag);
    }

    public void settingTabLayout(){
        tl_alba_card_tab = findViewById(R.id.tl_alba_card_tab);


        vp_alba_card_pager_content.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_alba_card_tab));

        tl_alba_card_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                vp_alba_card_pager_content.setCurrentItem(pos);
                if (pos == 0){
                    vp_alba_card_pager_content.setCurrentItem(0);

                }else{
                    vp_alba_card_pager_content.setCurrentItem(1);
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
    }
}
