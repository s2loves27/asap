package com.hackathon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hackathon.job.AnnounceInputActivity;
import com.hackathon.job.R;

import org.jetbrains.annotations.NotNull;

public class SubWorkerFragment extends Fragment {


    View view;

    public SubWorkerFragment(){

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.sub_worker_flagment, container, false);

        initView();


        return view;
    }
    public void initView(){

    }
}
