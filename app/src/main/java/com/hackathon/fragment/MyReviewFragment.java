package com.hackathon.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hackathon.job.R;

import org.jetbrains.annotations.NotNull;

public class MyReviewFragment extends Fragment {

    View view;
    public MyReviewFragment(){

    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_review_fragment, container, false);

        initView();


        return view;
    }

    public void initView(){

    }
}
