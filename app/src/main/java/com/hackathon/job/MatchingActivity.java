package com.hackathon.job;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hackathon.adapter.AlbaCardOwnerAdapter;
import com.hackathon.fragment.AlbaCardOwnerFragment;
import com.hackathon.fragment.SubOwnerFragment;
import com.hackathon.room.AnnounceCard.AnnounceCard;
import com.hackathon.room.AnnounceCard.AnnounceCardDao;
import com.hackathon.room.AppDatabase;
import com.hackathon.room.User.User;
import com.hackathon.room.User.UserDao;
import com.hackathon.util.BusProvider;
import com.hackathon.util.Util;
import com.squareup.otto.Bus;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.List;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

public class MatchingActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener, MapView.OpenAPIKeyAuthenticationResultListener,MapView.POIItemEventListener{

    private MapView mapView;
    private ViewGroup mapViewContainer;


//    Bus bus = BusProvider.getInstance();

    private double currentLng; //Long = X, Lat = Yㅌ
    private double currentLat;
    private String currentPlace;
    private double searchLng = -1;
    private double searchLat = -1;

    Button btn_matching_confirm;
    Button btn_matching_cancle;

    LinearLayout ll_matching_loading;
    LinearLayout ll_matching_start;

    // matching 관련 변경!
    TextView tv_matching_time;
    TextView tv_matching_address;
    TextView tv_matching_place;
    TextView tv_matching_category;
    TextView tv_matching_payment;
    TextView tv_matching_grade;




    CardView cv_test;

    MapPOIItem searchMarker = new MapPOIItem();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_activity);


        mapView = new MapView(this);
        mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);


        //리스너
        mapView.setMapViewEventListener(this); // this에 MapView.MapViewEventListener 구현.
        mapView.setPOIItemEventListener(this);
        mapView.setOpenAPIKeyAuthenticationResultListener(this);


        //현재위치 업데이트
//        mapView.setCurrentLocationEventListener(this);
//        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

//        bus.register(this); //정류소 등록
        initView();//view 시작

        currentLat = Util.loginUser.getLat();
        currentLng = Util.loginUser.getLng();
        currentPlace = Util.loginUser.getPlace();

        Log.i("JUN",""+currentLat + " : " + currentLng);

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(currentLat, currentLng);
        mapView.setMapCenterPoint(mapPoint, true);
        mapView.removePOIItem(searchMarker);
        searchMarker.setItemName("현재 위치 " + "(" +currentPlace+ ")");
        searchMarker.setTag(10000);


        searchMarker.setMapPoint(mapPoint);
        searchMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        searchMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(searchMarker);
        mapView.selectPOIItem(searchMarker, true);

//        mapView.setShowCurrentLocationMarker(true);

//

        btn_matching_confirm = findViewById(R.id.btn_matching_confirm);
        btn_matching_cancle = findViewById(R.id.btn_matching_cancle);





        cv_test = findViewById(R.id.cv_test);
        ll_matching_loading = findViewById(R.id.ll_matching_loading);
        ll_matching_start = findViewById(R.id.ll_matching_start);
//        cv_test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(ll_matching_loading.getVisibility() == View.GONE){
//                    ll_matching_loading.setVisibility(View.VISIBLE);
//                    ll_matching_start.setVisibility(View.GONE);
//                }else{
//                    ll_matching_loading.setVisibility(View.GONE);
//                    ll_matching_start.setVisibility(View.VISIBLE);
//                }
//            }
//        });

//        try {
//            sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }




        AppDatabase db = AppDatabase.getInstance(this);

        tv_matching_time = findViewById(R.id.tv_matching_time);
        tv_matching_address = findViewById(R.id.tv_matching_address);
        tv_matching_place= findViewById(R.id.tv_matching_place);
        tv_matching_category = findViewById(R.id.tv_matching_category);
        tv_matching_payment = findViewById(R.id.tv_matching_payment);
//        tv_matching_grade = findViewById(R.id.tv_matching_grade);

        new MatchingAsyncTask(db.announceCardDao(), db.userDao(), tv_matching_time, tv_matching_address,
                tv_matching_place, tv_matching_category, tv_matching_payment, mapView).execute(Util.loginUser.getEmail());




        btn_matching_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppDatabase db = AppDatabase.getInstance(MatchingActivity.this);

                AnnounceCard announceCard = new AnnounceCard();

                new AnnounceAsyncTask(db.announceCardDao()).execute(Util.loginUser.getEmail());


                finish();
            }
        });

        btn_matching_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class AnnounceAsyncTask extends AsyncTask<String, Void, Void> {
        private AnnounceCardDao announceCardDao;
        List<AnnounceCard> cards;

        public AnnounceAsyncTask(AnnounceCardDao announceCardDao) {
            this.announceCardDao = announceCardDao;

        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(String... email) {


            cards = announceCardDao.getMyAnnounceCard(email[0]);

            AnnounceCard card = cards.get(0);

            AnnounceCard announceCard = new AnnounceCard();

            announceCard.setState(1);
            announceCard.setTitle(card.getTitle());
            announceCard.setLocation(card.getLocation());
            announceCard.setAddress(card.getAddress());
            announceCard.setLat(card.getLat());
            announceCard.setLng(card.getLng());
            announceCard.setDate(card.getDate());
            announceCard.setStartTime(card.getStartTime());
            announceCard.setFinishTime(card.getFinishTime());
            announceCard.setPayment(card.getPayment());
            announceCard.setCategory(card.getCategory());
            announceCard.setDesc(card.getDesc());
            announceCard.setQualify(card.getQualify());
            announceCard.setUserName(Util.loginUser.getEmail());
            announceCard.setLevel(0);
            //workFlag - 0(worker)/ 1 (구인자)
            announceCard.setWorkFlag(0);
//            announceCard.setScore(Util.loginUser.getScore());

//            List<AnnounceCard> card = announceCardDao.getALL();
            card.setState(1);


            announceCardDao.insert(announceCard);
            announceCardDao.update(card);

            return null;
        }


    }



    public static class MatchingAsyncTask extends AsyncTask<String, Void, Void> {
        private AnnounceCardDao announceCardDao;
        private UserDao userDao;
        List<AnnounceCard> cards;
        List<Double> users;
        TextView tv_matching_time;
        TextView tv_matching_address;
        TextView tv_matching_place;
        TextView tv_matching_category;
        TextView tv_matching_payment;

        MapView mapView;

        public MatchingAsyncTask(AnnounceCardDao announceCardDao, UserDao userDao ,TextView tv_matching_time,
                                 TextView tv_matching_address, TextView tv_matching_place,
                                 TextView tv_matching_category, TextView tv_matching_payment,
                                  MapView mapView) {
            this.announceCardDao = announceCardDao;
            this.userDao = userDao;
            this.tv_matching_time = tv_matching_time;
            this.tv_matching_address = tv_matching_address;
            this.tv_matching_place = tv_matching_place;
            this.tv_matching_category = tv_matching_category;
            this.tv_matching_payment = tv_matching_payment;

            this.mapView = mapView;


        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(String... email) {


            cards = announceCardDao.getMyAnnounceCard(email[0]);

            users = userDao.getUserScore(email[0]);

            Util.announceCard = cards;

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            AnnounceCard card = cards.get(0);
            double score = users.get(0);
            tv_matching_time.setText(card.getStartTime() + " - " + card.getFinishTime());
            tv_matching_address.setText(card.getAddress());
            tv_matching_place.setText(card.getLocation());
            tv_matching_category.setText(card.getCategory());
            tv_matching_payment.setText(card.getPayment()+ "");
//            tv_matching_grade.setText(""+score);

            MapPOIItem searchMarker = new MapPOIItem();

            double placeLat = card.getLat();
            double placeLng = card.getLng();

            Log.i("JUN",""+placeLat + " : " + placeLng);
            String place = card.getLocation();


            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(placeLat, placeLng);
            mapView.setMapCenterPoint(mapPoint, true);
            mapView.removePOIItem(searchMarker);
            searchMarker.setItemName("일할 위치 " + "("+place +")");
            searchMarker.setTag(10000);


            searchMarker.setMapPoint(mapPoint);
            searchMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            searchMarker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            mapView.addPOIItem(searchMarker);
            mapView.selectPOIItem(searchMarker, true);


        }
    }


    void initView(){

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

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

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewContainer.removeAllViews();
//        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mapView.setShowCurrentLocationMarker(false);
    }
}
