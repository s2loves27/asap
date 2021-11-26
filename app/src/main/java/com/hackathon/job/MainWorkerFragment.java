package com.hackathon.job;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.media.tv.TvContract;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.hackathon.room.AppDatabase;
import com.hackathon.room.User.User;
import com.hackathon.room.User.UserDao;
import com.hackathon.util.BusProvider;
import com.hackathon.util.Util;
import com.shashank.sony.fancytoastlib.FancyToast;


import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.hackathon.adapter.LocationAdapter;
import com.hackathon.model.kakao.category_search.CategoryResult;
import com.hackathon.model.kakao.category_search.Document;
import com.hackathon.restapi.ApiInterface;
import com.hackathon.restapi.kakao.ApiClient;
import com.kakao.sdk.user.UserApiClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainWorkerFragment extends Fragment implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.OpenAPIKeyAuthenticationResultListener,MapView.POIItemEventListener{


    private static final String LOG_TAG = "MainWorkerFragment";
    private View view;
    private String TAG = "@MAP";

    private Button btn_main_work_matching;
    private LinearLayout ll_main_work_map;

    private TextView tv_main_work_place;
    private TextView tv_main_work_address;
    RangeSlider rs_main_worker_time;


    private TextView tv_main_worker_time;

    private LinearLayout ll_main_worker_alba;
    private LinearLayout ll_main_worker_errand;
    private LinearLayout ll_main_worker_tutoring;

    private ImageView iv_main_worker_alba;
    private ImageView iv_main_worker_errand;
    private ImageView iv_main_worker_tutoring;

    //Data
    ArrayList<Document> documentArrayList = new ArrayList<>(); //지역명 검색 결과 리스트
    Bus bus = BusProvider.getInstance();
    private String searchName;
    private String searchAddress;
    private double currentLng; //Long = X, Lat = Yㅌ
    private double currentLat;
    private double searchLng = -1;
    private double searchLat = -1;



    MapPOIItem searchMarker = new MapPOIItem();

    boolean alba = false;
    boolean errand = false;
    boolean tutoring = false;

    public MainWorkerFragment(){

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_worker_fragment, container, false);



        bus.register(this); //정류소 등록
        initView();//view 시작



        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bus.unregister(this);
    }

    private void initView(){



        ll_main_worker_alba = view.findViewById(R.id.ll_main_worker_alba);
        ll_main_worker_errand = view.findViewById(R.id.ll_main_worker_errand);
        ll_main_worker_tutoring = view.findViewById(R.id.ll_main_worker_tutoring);


        iv_main_worker_alba = view.findViewById(R.id.iv_main_worker_alba);
        iv_main_worker_errand = view.findViewById(R.id.iv_main_worker_errand);
        iv_main_worker_tutoring = view.findViewById(R.id.iv_main_worker_tutoring);


        if (alba) {

            iv_main_worker_alba.setImageResource(R.drawable.uclock);
            ll_main_worker_alba.setBackgroundResource(R.drawable.round_background_clicked);
        }
        else{
            iv_main_worker_alba.setImageResource(R.drawable.clock);
            ll_main_worker_alba.setBackgroundResource(R.drawable.round_background);
        }

        if (errand) {

            iv_main_worker_errand.setImageResource(R.drawable.uclock);
            ll_main_worker_errand.setBackgroundResource(R.drawable.round_background_clicked);
        }
        else{
            iv_main_worker_errand.setImageResource(R.drawable.clock);
            ll_main_worker_errand.setBackgroundResource(R.drawable.round_background);
        }

        if (tutoring) {

            iv_main_worker_tutoring.setImageResource(R.drawable.ubook);
            ll_main_worker_tutoring.setBackgroundResource(R.drawable.round_background_clicked);
        }
        else{
            iv_main_worker_tutoring.setImageResource(R.drawable.book);
            ll_main_worker_tutoring.setBackgroundResource(R.drawable.round_background);
        }

        ll_main_worker_alba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alba) {
                    iv_main_worker_alba.setImageResource(R.drawable.clock);
                    ll_main_worker_alba.setBackgroundResource(R.drawable.round_background);
                    alba = false;
                }
                else{
                    iv_main_worker_alba.setImageResource(R.drawable.uclock);
                    ll_main_worker_alba.setBackgroundResource(R.drawable.round_background_clicked);
                    alba = true;
                }
            }
        });

        ll_main_worker_errand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (errand) {
                    iv_main_worker_errand.setImageResource(R.drawable.map);
                    ll_main_worker_errand.setBackgroundResource(R.drawable.round_background);
                    errand = false;
                }
                else{
                    iv_main_worker_errand.setImageResource(R.drawable.umap);
                    ll_main_worker_errand.setBackgroundResource(R.drawable.round_background_clicked);
                    errand = true;
                }
            }
        });

        ll_main_worker_tutoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tutoring) {
                    iv_main_worker_tutoring.setImageResource(R.drawable.book);
                    ll_main_worker_tutoring.setBackgroundResource(R.drawable.round_background);
                    tutoring = false;
                }
                else{
                    iv_main_worker_tutoring.setImageResource(R.drawable.ubook);
                    ll_main_worker_tutoring.setBackgroundResource(R.drawable.round_background_clicked);
                    tutoring = true;
                }
            }
        });

        ll_main_work_map = view.findViewById(R.id.ll_main_work_map);
        btn_main_work_matching = view.findViewById(R.id.btn_main_work_matching);


        ll_main_work_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchMap.class);
                intent.putExtra("flag",0);
                startActivity(intent);
            }
        });

        btn_main_work_matching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MatchingActivity.class);
                startActivity(intent);
            }
        });

        tv_main_worker_time = view.findViewById(R.id.tv_main_worker_time);

        tv_main_work_place = view.findViewById(R.id.tv_main_work_place);
        tv_main_work_address = view.findViewById(R.id.tv_main_work_address);

        String place = Util.loginUser.getPlace();
        String address = Util.loginUser.getAddress();

        if (place == null || place.equals("")) {
            tv_main_work_place.setText("위치를 선택해주세요.");
        }else{
            tv_main_work_place.setText(place);
        }if (address == null || address.equals("") ){
            tv_main_work_address.setText("장소가 지정되지 않았습니다.");
        }else{
            tv_main_work_address.setText(address);
        }
        rs_main_worker_time = view.findViewById(R.id.rs_main_worker_time);

        rs_main_worker_time.setLabelFormatter(new LabelFormatter() {
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
        rs_main_worker_time.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> range = slider.getValues();

                String startMinute = "";
                String finishMinute = "";
                String workMinute = "";

                Float start = range.get(0);
                Float finish = range.get(1);

                Float workTime = finish - start;

                int starthour = (int)Math.floor(start);
                int finishhour = (int)Math.floor(finish);
                int workHour = (int)Math.floor(workTime);

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
                if((workTime - workHour) != 0){
                    workMinute  = "30";
                }

                String startTime = String.format("%02d",starthour) +":"+ startMinute;
                String finishTime = String.format("%02d",finishhour) +":"+ finishMinute;
                String Time = "";
                if (workMinute != "") {
                    Time = String.format("%02d", workHour) + "시간 " + workMinute + "분";
                }else{
                    Time = String.format("%02d", workHour) + "시간";
                }
                tv_main_worker_time.setText(Time);
                Log.i(TAG, startTime + " " + finishTime );
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        currentLat = mapPointGeo.latitude;
        currentLng = mapPointGeo.longitude;
        MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
//
        mapView.setMapCenterPoint(currentMapPoint, true);
        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
//        Log.i(TAG, mapPoint.getMapPointGeoCoord().latitude + " " + mapPoint.getMapPointGeoCoord().longitude );

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
//        getmaker(mapPoint.getMapPointGeoCoord().latitude, mapPoint.getMapPointGeoCoord().longitude);
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }


    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    @Subscribe //검색예시 클릭시 이벤트 오토버스
    public void search(Document document) {//public항상 붙여줘야함
        Log.i("MainWorker","bus");
        FancyToast.makeText(view.getContext(), document.getPlaceName() + " 검색", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
        searchName = document.getPlaceName();
        searchLng = Double.parseDouble(document.getX());
        searchLat = Double.parseDouble(document.getY());
        searchAddress = document.getAddressName();
        Util.loginUser.setPlace(searchName);
        Util.loginUser.setLng(searchLng);
        Util.loginUser.setLat(searchLat);
        Util.loginUser.setAddress(searchAddress);

        AppDatabase db = AppDatabase.getInstance(view.getContext());

        DbAsyncTask task = new DbAsyncTask(db.userDao(), tv_main_work_place, tv_main_work_address);
        task.execute(Util.loginUser);



    }

    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class DbAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userdao;
        private TextView tv_main_work_place;
        private TextView tv_main_work_adress;
        User user;
        public DbAsyncTask(UserDao userdao, TextView tv_main_work_place, TextView tv_main_work_adress) {
            this.userdao = userdao;
            this.tv_main_work_place = tv_main_work_place;
            this.tv_main_work_adress = tv_main_work_adress;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(User... user) {
            //추가만하고 따로 SELECT문을 안해도 라이브데이터로 인해
            //getAll()이 반응해서 데이터를 갱신해서 보여줄 것이다,  메인액티비티에 옵저버에 쓴 코드가 실행된다. (라이브데이터는 스스로 백그라운드로 처리해준다.)
            this.user = user[0];

            userdao.update(user[0]);


            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            tv_main_work_place.setText(user.getPlace());
            tv_main_work_adress.setText(user.getAddress());


        }
    }



//    public void getmaker(double latitude, double longitude){
//
//
//        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
//        mapView.removePOIItem(searchMarker);
//        searchMarker.setItemName("알바 위치");
//        searchMarker.setTag(10000);
//
//        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
//        searchMarker.setMapPoint(mapPoint);
//        searchMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
//        searchMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
//        //마커 드래그 가능하게 설정
//        searchMarker.setDraggable(true);
//        mapView.addPOIItem(searchMarker);
//
//
//
////        MapPoint currentMapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);
////        mapView.setMapCenterPoint(currentMapPoint, true);
//    }
}
