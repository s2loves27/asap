package com.hackathon.job;

import android.media.Ringtone;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hackathon.adapter.MainPagerAdapter;
import com.hackathon.adapter.SubPagerAdapter;
import com.hackathon.fragment.SubOwnerFragment;
import com.hackathon.fragment.SubWorkerFragment;

import java.util.Arrays;
import java.util.List;

public class SubListActivity extends AppCompatActivity {
    TabLayout tl_sub_list_tab;
    SubOwnerFragment subOwnerFragment;
    SubWorkerFragment subWorkerFragment;

    private SubPagerAdapter subPagerAdapter;
    ViewPager vp_sub_list_pager_content;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_list_activity);

        createFragment();
        createViewpager();
        settingTabLayout();
        initView();

    }

    void initView(){
        tl_sub_list_tab = findViewById(R.id.tl_sub_list_tab);


    }
    //fragment 생성
    public void createFragment(){
        subOwnerFragment = new SubOwnerFragment();
        subWorkerFragment = new SubWorkerFragment();
    }


    public void createViewpager(){
        vp_sub_list_pager_content = findViewById(R.id.vp_sub_list_pager_content);
        subPagerAdapter = new SubPagerAdapter(getSupportFragmentManager());
        subPagerAdapter.addFragment(subWorkerFragment);
        subPagerAdapter.addFragment(subOwnerFragment);

        vp_sub_list_pager_content.setAdapter(subPagerAdapter);
        vp_sub_list_pager_content.setCurrentItem(1);

    }

    public void settingTabLayout(){

        tl_sub_list_tab = findViewById(R.id.tl_sub_list_tab);


        vp_sub_list_pager_content.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_sub_list_tab));

        tl_sub_list_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                vp_sub_list_pager_content.setCurrentItem(pos);
                if (pos == 0){
                    vp_sub_list_pager_content.setCurrentItem(0);

                }else{
                    vp_sub_list_pager_content.setCurrentItem(1);
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
