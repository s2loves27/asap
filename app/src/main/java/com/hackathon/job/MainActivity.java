package com.hackathon.job;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import net.daum.mf.map.api.MapView;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {


    private final int FRAGMENT1 =1;
    private final int FRAGMENT2 =2;

    private String TAG = "전지훈";
    private TextView topmenu_textview_status;
    private MaterialToolbar topAppBar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getHashKey();

        topmenu_textview_status = findViewById(R.id.topmenu_textview_status);
        topAppBar = findViewById(R.id.topAppBar);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);



        if(!checkLocationServicesStatus()){
            showDialogForLocationServiceSetting();
        }else{

            checkRunTimePermission();

        }





        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.item1:
                        Toast.makeText(MainActivity.this, "item1 clicked..", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item2:
                        Toast.makeText(MainActivity.this, "item2 clicked..", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item3:
                        Toast.makeText(MainActivity.this, "item3 clicked..", Toast.LENGTH_SHORT).show();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        topmenu_textview_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topmenu_textview_status.getText().toString() == "사람구하기"){
                    String flag = "일구하기";
                    topmenu_textview_status.setText(flag);
                    callFragment(flag);
                }else{
                    topmenu_textview_status.setText("사람구하기");
                    String flag = "사람구하기";
                    topmenu_textview_status.setText(flag);
                    callFragment(flag);

                }
            }
        });

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        //1. 위치 퍼미션을 가지고 있는지 체크
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED){
            //2. 이미 퍼미션을 가지고 있는 경우
            //(안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요 없다 (이미 허용))
            //3. 위치 값을 가져올 수 있음
            callFragment("일구하기");
        }else{ //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요
            //3-1 사용자가 퍼미션 거부 한적이 있는 경우
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])){
                //3-2. 요청을 진행하기 전에 사용가에게 퍼미션이 필요한 이유를 설명하는 부분
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                //3-3. 사용자에게 퍼미션 요청을 하는 부분
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }else {
                //4-1 사용자가 퍼미션 서부를 한적이 없는 경우 바로 퍼미션 요청
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);

            }

        }
    }
    //GPS 활성화를 위한 메소드
    private void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치서비스가 필요합니다\n" +
                "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callGPSSettingIntent
                        = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_ENABLE_REQUEST_CODE){
            if (checkLocationServicesStatus()){
                Log.d("@@@", "Result: GPS 활성화 OK");
                checkRunTimePermission();
                return;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grandResults) {
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length ){
            boolean check_result = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if(check_result){
                Log.d("@@@", "start");
                callFragment("일구하기");
            }
            else{
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])){
                    Toast.makeText(this, "퍼미션이 거부 되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(this, "퍼미션이 거부 되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.",  Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    public boolean checkLocationServicesStatus(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void callFragment(String fragment_flag){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (fragment_flag == "일구하기"){
            MainWorkerFragment mainWorkerFragment = new MainWorkerFragment();
            transaction.replace(R.id.main_fragment_container,mainWorkerFragment);
            transaction.commit();

        }else{
            MainOwnerFragment mainOwnerFragment = new MainOwnerFragment();
            transaction.replace(R.id.main_fragment_container,mainOwnerFragment);
            transaction.commit();
        }
    }


    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
}