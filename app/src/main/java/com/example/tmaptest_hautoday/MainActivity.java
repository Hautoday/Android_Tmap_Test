package com.example.tmaptest_hautoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.Tmap.TMapCircle;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.address_info.TMapAddressInfo;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LinearLayout linMapView,lin1;
    EditText edtSerch;
    TextView tvAddress,pay;
    Button btnSearch, btnZoomIn, btnZoomOut, btnMyLocation, btnStop, btnRouteSearch;
    TMapView tMapView;
    TMapData tMapData;
    ArrayList<TMapPOIItem> poiResult = null;
    double lat = 0,lon = 0, lat2 = 0, lon2 = 0;
    int count = 0, moveCount = 0, nullCount = 0;
    String[] route ={
            "교통최적 + 추천(기본값)",
            "교통최적 + 무료우선",
            "교통최적 + 최소시간",
            "교통최적 + 초보",
            "교통최적 + 고속도로우선",
    };
    String address,text,total;

    // main문
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();
        initView();
        initInstance();
        eventListner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 1, locationListener);
        }catch(SecurityException e){

        }
        mapTouch();
    }

    // 위젯들을(뷰) 초기화 하는 함수
    public void initView(){
        linMapView = findViewById(R.id.linMapView);
        edtSerch = findViewById(R.id.edtSerch);
        btnSearch = findViewById(R.id.btnSearch);
        btnZoomIn = findViewById(R.id.btnZoomIn);
        btnZoomOut = findViewById(R.id.btnZoomOut);
        btnMyLocation = findViewById(R.id.btnMyLocation);
        tvAddress = findViewById(R.id.tvAddress);
        btnStop = findViewById(R.id.btnStop);
        btnRouteSearch = findViewById(R.id.btnRouteSearch);
        lin1 = findViewById(R.id.lin1);
        pay = findViewById(R.id.pay);
    }

    // 필요한 인스턴스 초기화 함수
    public void initInstance(){
        // 위치정보용
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 보여줄 티맵뷰 선언
        // API 키값 적용
        // 만들어둔 linMapView 에 addView를 사용하여 화면 표출
        // tMapData 는 TMapData를 담아줄 인스턴스 객체를 선언.
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("???");
        linMapView.addView(tMapView);
        tMapData = new TMapData(this);
        poiResult = new ArrayList<>();
    }

    public void eventListner(){
        btnSearch.setOnClickListener(listener);
        btnZoomIn.setOnClickListener(listener);
        btnZoomOut.setOnClickListener(listener);
        btnMyLocation.setOnClickListener(listener);
        btnStop.setOnClickListener(listener);
        btnRouteSearch.setOnClickListener(listener);
    }

    // 티맵으로 통합검색을 수행하는 함수
    // 통합검색 시 여러개의 경로를 다이알로그로 띄워준다. ( 주소를 띄워주는 것 )
    public void searchPOI(String strData){
        tMapData.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                nullCount = 0;
                poiResult.addAll(arrayList);
                // 검색된 좌표를 센터로 -> 이동하며 좌표값을 ArrayList에 등록
                tMapView.setCenterPoint(
                        arrayList.get(0).getPOIPoint().getLongitude(),
                        arrayList.get(1).getPOIPoint().getLatitude()
                );
                TMapMarkerItem markerItem1 = new TMapMarkerItem();
                TMapPoint tMapPoint1 = new TMapPoint(arrayList.get(1).getPOIPoint().getLatitude(), arrayList.get(0).getPOIPoint().getLongitude()); // 검색된 좌표값
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
                markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
                tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가
                arrayList.clear();
                    lat2 = markerItem1.latitude;
                    lon2 = markerItem1.longitude;
                }
        });
        TMapData tMapData = new TMapData();
        while(true) {
            tMapData.reverseGeocoding(lat2, lon2, "A04", new TMapData.reverseGeocodingListenerCallback() {
                @Override
                public void onReverseGeocoding(TMapAddressInfo addressInfo) {
                    // 국내 지도가 아닌 해외를 입력할경우 NullPointException 으로 인한 오류 방지.
                    try {
                        address = "검색한 위치의 주소는\n" + addressInfo.strFullAddress;
                        text = addressInfo.strFullAddress;
                    } catch (Exception e) {
                        address = "요청 좌표가 허용범위를 벗어났습니다.";
                        nullCount++;
                    }
                }
            });
            if (text != null) {
                lin1.setVisibility(View.VISIBLE);
                tvAddress.setText(address);
                pay.setText("");
                break;
            }else if( nullCount > 4 ){
                break;
            }
        }
    }
    // 위지정보를 위한 GPS
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            if(count == 0){
                count++;
                tMapView.setCenterPoint(lon, lat,true);
            }
            TMapPoint tMapPoint = new TMapPoint(lat, lon);
            TMapCircle tMapCircle = new TMapCircle();
            tMapCircle.setCenterPoint( tMapPoint );
            tMapCircle.setRadius(25);
            tMapCircle.setCircleWidth(2);
            tMapCircle.setLineColor(Color.BLUE);
            tMapCircle.setAreaColor(Color.RED);
            tMapCircle.setAreaAlpha(10);
            tMapView.addTMapCircle("circle1", tMapCircle);
        }

        @Override
        public void onLocationChanged(@NonNull List<Location> locations) {
            LocationListener.super.onLocationChanged(locations);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LocationListener.super.onStatusChanged(provider, status, extras);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

    };

    // view에 배치된 버튼을 사용하는 listener
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.btnSearch:
                    tMapView.removeAllMarkerItem();
                    String strData = edtSerch.getText().toString();
                    if(!strData.equals("")){
                        // 티맵을 통해 검색
                        searchPOI(strData);
                    }else{
                        Toast.makeText(getApplicationContext(),"검색어를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnZoomIn:
                    tMapView.MapZoomIn();
                    break;
                case R.id.btnZoomOut:
                    tMapView.MapZoomOut();
                    break;
                case R.id.btnMyLocation:
                    tMapView.setCenterPoint(lon, lat,true);
                    tMapView.setZoomLevel(15);
                    break;
                case R.id.btnStop:
                    lin1.setVisibility(View.GONE);
                    tMapView.removeAllMarkerItem();
                    tMapView.removeAllTMapPolyLine();
                    break;
                case R.id.btnRouteSearch:
                    aDialog();
                    break;
            }
        }
    };

    // 안드로이드 시스템 UI를 숨김으로서 전체화면 모드로 하는 코드.
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // title 바 visibility gone
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // navigation 바 ( 뒤로가기, 홈, 최근목록) visibility gone
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void aDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경로를 선택해주세요.");
        builder.setItems(route, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0 :
                        Toast.makeText(getApplicationContext(), "교통최적+추천(기본값)", Toast.LENGTH_SHORT).show();
                        route(i);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "교통최적+무료우선", Toast.LENGTH_SHORT).show();
                        route(i);
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "교통최적+최소시간", Toast.LENGTH_SHORT).show();
                        route(i);
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "교통최적+초보", Toast.LENGTH_SHORT).show();
                        route(i);
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(), "교통최적+고속도로우선", Toast.LENGTH_SHORT).show();
                        route(i);
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Tmap을 터치할때마다 불러올 메소드
    // 뷰가 터치되면 터치된 좌표값을 불러온다
    // 좌표값을 불러와 해당 좌표값에 마커를 찍고
    // 좌표값을 주소로 변환하여 리턴한다.
    public void mapTouch() {
        tMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == event.ACTION_DOWN){
                        moveCount = 0;
                    }
                    if(event.getAction() == event.ACTION_MOVE){
                        moveCount++;
                    }
                    if (event.getAction() == event.ACTION_UP && moveCount <= 10) {
                            tMapView.removeAllMarkerItem();
                            nullCount = 0;
                            int x = (int) event.getX();
                            int y = (int) event.getY();
                            TMapPoint tMapPoint = tMapView.getTMapPointFromScreenPoint(x, y);
                            TMapMarkerItem marker = new TMapMarkerItem();
                            marker.setTMapPoint(tMapPoint);
                            lat2 = tMapPoint.getLatitude();
                            lon2 = tMapPoint.getLongitude();
                            tMapView.addMarkerItem("marker", marker);

                            TMapData tMapData = new TMapData();
                            while(true) {
                                tMapData.reverseGeocoding(tMapPoint.getLatitude(), tMapPoint.getLongitude(), "A04", new TMapData.reverseGeocodingListenerCallback() {
                                    @Override
                                    public void onReverseGeocoding(TMapAddressInfo addressInfo) {
                                        // 국내 지도가 아닌 해외를 입력할경우 NullPointException 으로 인한 오류 방지.
                                        // Toast로 알람주기
                                        try {
                                            address = "선택한 위치의 주소는\n" + addressInfo.strFullAddress;
                                            text = addressInfo.strFullAddress;
                                        } catch (Exception e) {
                                            address = "요청 좌표가 허용범위를 벗어났습니다.";
                                            nullCount++;
                                        }
                                    }
                                });
                                if(text != null){
                                    lin1.setVisibility(View.VISIBLE);
                                    tvAddress.setText(address);
                                    pay.setText("");
                                    break;
                                }else if( nullCount > 4 ){
                                    break;
                                }
                            }
                        }

                return false;
            }
        });
    }
    // 예상 이동거리 (Km) 메소드
    public void route(int numbers){
        new Thread(() -> {
            TMapPoint tMapPointStart = new TMapPoint(lat, lon); //출발지 ( 현위치 )
            TMapPoint tMapPointEnd = new TMapPoint(lat2, lon2); // 도착지
            try {
                TMapPolyLine tMapPolyLine = tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, tMapPointStart, tMapPointEnd, null, numbers);
                tMapPolyLine.setLineColor(Color.BLUE);
                tMapPolyLine.setLineWidth(2);
                tMapView.addTMapPolyLine("Line1", tMapPolyLine);
                total = "총 거리 : "+(int)tMapPolyLine.getDistance()+"m "+ ((int)tMapPolyLine.getDistance() / 1000)+"Km";
                pay.setText(total);
            }catch(Exception e) {
                e.printStackTrace();
            }
        })
        .start();
    }
}