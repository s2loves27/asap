package com.hackathon.job;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.hackathon.adapter.SubPagerAdapter;
import com.hackathon.fragment.AlbaCardOwnerFragment;
import com.hackathon.fragment.AlbaCardWorkerFragment;
import com.hackathon.fragment.BalanceFragment1;
import com.hackathon.fragment.BalanceFragment2;

import java.util.Arrays;
import java.util.List;

public class BalanceActivity extends AppCompatActivity {

    BottomNavigationView bn_balance_position;
    BalanceFragment1 balanceFragment1;
    BalanceFragment2 balanceFragment2;

    ViewPager vp_balance_pager_content;
    private SubPagerAdapter subPagerAdapter;
    TabLayout tl_balance_tab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_activity);

        createFragment();
        createViewpager();
        settingTabLayout();
        initView();

    }

    public void initView(){

    }

    public void createFragment() {
        balanceFragment1 = new BalanceFragment1();
        balanceFragment2 = new BalanceFragment2();
    }

    public void createViewpager(){
        vp_balance_pager_content = findViewById(R.id.vp_balance_pager_content);
        subPagerAdapter = new SubPagerAdapter(getSupportFragmentManager());
        subPagerAdapter.addFragment(balanceFragment1);
        subPagerAdapter.addFragment(balanceFragment2);

        vp_balance_pager_content.setAdapter(subPagerAdapter);
        vp_balance_pager_content.setCurrentItem(0);
    }

    public void settingTabLayout(){
        tl_balance_tab = findViewById(R.id.tl_balance_tab);


        vp_balance_pager_content.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_balance_tab));

        tl_balance_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                vp_balance_pager_content.setCurrentItem(pos);
                if (pos == 0){
                    vp_balance_pager_content.setCurrentItem(0);

                }else{
                    vp_balance_pager_content.setCurrentItem(1);
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
