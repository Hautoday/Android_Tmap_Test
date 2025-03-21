# TMAP API 활용 네비게이션 구현

## 해당 앱의 작동방식

1. 앱을 실행할 시 GPS 정보값을 받아와 Tmap에 중심좌표를 사용자의 위치로 변경합니다. 
메인위치로 변경되고나서 앱을 사용하는것을 권고드립니다. 
2. 앱의 메인화면에는 지도, 줌인, 줌아웃, 센터, 주소검색 이 있습니다. 
3. 이동할 거리가 가까울 경우 지도에 위치를 터치하는것만으로도 마커를 찍고 주소를 얻어올 수 있습니다. 
얻어온 주소를 보여주는 레이아웃이 숨어있다 나타나게 되며, 경로검색 버튼을 통해 경로와 거리를 알수있습니다.
4. 취소버튼을 누를 시 주소와 마커를 지우며 하단 레이아웃을 숨기게 됩니다. 
5. 경로버튼을 누를경우 다이얼로그로 경로에 대한 경로를 5개를 알려주며, 
경로를 선택할시 현위치에서 도착할 위치까지의 거리 및 경로를 지도로 알려줍니다. 
6. 만약 이동할 거리가 멀 경우 검색기능을 사용할 수 있습니다. 
상단 검색바를 통해 원하는 주소를 입력 후 확인버튼을 누를 시 검색어를 통한 주소 5개를 다이얼로그로 보여줍니다. 
7. 해당 다이얼로그에서 주소를 선택하면 해당위치로 화면이 이동하게되며 주소에 마커를 표시합니다. 
8. 검색 시 하단에 숨겨진 경로탐색, 취소버튼, 주소 가 나오게 되며 경로탐색 버튼을 누름으로서 
현 위치에서 도착할 위치로의 경로를 찾아줍니다. 
경로의 대한 선택은 위와 동일하게 다이얼로그로 보여주며 사용자가 선택하는 형식입니다. 
9. 기능은 모두 취소를 통해 이전상태로 되돌릴 수 있습니다. 

## 메소드에 대한 설명

1. 패키지 및 클래스 임포트:
    - `com.example.tmaptest_hautoday`: 현재 앱의 패키지 이름입니다.
    - 다양한 안드로이드 클래스와 Tmap API의 클래스들을 임포트하고 있습니다.
2. `MainActivity` 클래스:
    - `AppCompatActivity` 클래스를 상속하는 액티비티입니다.
    - `LocationListener` 인터페이스를 구현하여 위치 정보를 받을 수 있습니다.
3. 멤버 변수:
    - `locationManager`: 위치 관리자를 나타내는 객체입니다.
    - `linMapView`, `lin1`: 레이아웃을 나타내는 `LinearLayout` 객체입니다.
    - `edtSerch`, `tvAddress`, `pay`: 텍스트 입력과 텍스트 출력을 위한 `EditText`와 `TextView` 객체입니다.
    - `btnSearch`, `btnZoomIn`, `btnZoomOut`, `btnMyLocation`, `btnStop`, `btnRouteSearch`: 버튼을 나타내는 `Button` 객체입니다.
    - `tMapView`: Tmap 지도를 나타내는 `TMapView` 객체입니다.
    - `tMapData`: Tmap 데이터를 가져오기 위한 `TMapData` 객체입니다.
    - `poiResult`: 검색된 POI 항목을 저장하기 위한 `ArrayList<TMapPOIItem>` 객체입니다.
    - `lat`, `lon`, `lat2`, `lon2`: 위도와 경도 값을 저장하기 위한 변수입니다.
    - `count`, `moveCount`, `selectCount`: 카운트 값을 저장하기 위한 변수입니다.
    - `route`: 경로 유형을 저장한 문자열 배열입니다.
    - `search`: 검색 결과를 저장하기 위한 문자열 배열입니다.
    - `address`, `text`, `total`: 주소와 텍스트를 저장하기 위한 변수입니다.
4. `onCreate()` 메서드:
    - 액티비티가 생성될 때 호출되는 메서드입니다.
    - `setContentView()`를 통해 액티비티에 레이아웃을 설정합니다.
    - `hideSystemUI()` 메서드를 호출하여 전체화면 모드로 설정합니다.
    - `initView()` 메서드를 호출하여 위젯을 초기화합니다.
    - `initInstance()` 메서드를 호출하여 필요한 인스턴스를 초기화합니다.
    - `eventListner()` 메서드를 호출하여 버튼 클릭 이벤트 리스너를 설정합니다.
5. `onResume()` 메서드:
    - 액티비티가 화면에 표시될 때 호출되는 메서드입니다.
    - 위치 업데이트를 요청하기 위해 `locationManager.requestLocationUpdates()`를 호출합니다.
    - `mapTouch()` 메서드를 호출하여 지도 터치 이벤트를 처리합니다.
6. `initView()` 메서드:
    - 레이아웃에서 사용되는 위젯들을 초기화하는 메서드입니다.
    - `findViewById()`를 사용하여 각 위젯을 찾아 멤버 변수에 할당합니다.
7. `initInstance()` 메서드:
    - 필요한 인스턴스를 초기화하는 메서드입니다.
    - `LocationManager` 객체를 생성하고 위치 제공자를 설정합니다.
    - `TMapView` 객체를 생성하고 API 키를 설정합니다.
8. `eventListner()` 메서드:
    - 버튼 클릭 이벤트를 처리하는 메서드입니다.
    - `setOnClickListener()`를 사용하여 각 버튼에 클릭 리스너를 등록합니다.
9. `searchPOI()` 메서드:
    - 입력된 검색어를 통해 Tmap을 통합검색하는 메서드입니다.
    - `tMapData.findAllPOI()`를 호출하여 검색 결과를 `poiResult`에 저장합니다.
    - 검색 결과를 다이얼로그로 표시합니다.
10. `convertAddressToCoordinates()` 메서드:
    - 선택된 주소를 좌표로 변환하는 메서드입니다.
    - `tMapData.convertToGeoPoint()`를 호출하여 주소를 좌표로 변환합니다.
    - 선택된 위치에 마커를 표시하고 주소를 `tvAddress`에 표시합니다.
11. `locationListener` 객체:
    - 위치 정보를 받아오기 위한 `LocationListener` 인터페이스의 구현체입니다.
    - `onLocationChanged()` 메서드를 재정의하여 위치가 변경될 때마다 호출됩니다.
    - 현재 위치를 표시하고, 반경 25m의 원을 그려줍니다.
12. `listener` 객체:
    - 버튼 클릭 등의 이벤트를 처리하는 리스너 객체입니다.
    - `onClick()` 메서드를 재정의하여 버튼 클릭 이벤트를 처리합니다.
    - 검색, 확대/축소, 현재 위치 표시, 경로 검색 등의 기능을 수행합니다.
13. `hideSystemUI()` 메서드:
    - 안드로이드 시스템 UI를 숨기는 메서드입니다.
    - `setSystemUiVisibility()`를 호출하여 전체화면 모드로 설정합니다.
14. `searchaDialog()` 메서드:
    - 주소 선택 다이얼로그를 표시하는 메서드입니다.
    - 선택한 주소를 좌표로 변환합니다.
15. `aDialog()` 메서드:
    - 경로 선택 다이얼로그를 표시하는 메서드입니다.
    - 선택한 경로 유형에 따라 경로를 탐색합니다.
16. `mapTouch()` 메서드:
    - 지도 터치 이벤트를 처리하는 메서드입니다.
    - `setOnClickListener()`를 사용하여 지도 터치 이벤트를 처리합니다.
    - 터치된 좌표에 마커를 표시하고 좌표를 주소로 변환합니다.




1. 초기화 및 인스턴스 설정:
    - TMapView를 초기화하고 API 키를 설정합니다.
    - 필요한 위젯들을 초기화합니다.
    - 위치 관리자(LocationManager)를 초기화합니다.
    - TMapData 객체를 생성합니다.
2. onResume():
    - 액티비티가 활성화되거나 다시 시작될 때 호출되는 메소드입니다.
    - 위치 업데이트를 요청하고 지도 터치 이벤트를 처리합니다.
3. initView():
    - 액티비티에 배치된 뷰들을 초기화하는 메소드입니다.
4. initInstance():
    - 인스턴스를 초기화하는 메소드입니다.
    - 위치 관리자(LocationManager)와 TMapView를 초기화하고 화면에 추가합니다.
    - TMapData와 TMapPOIItem 목록을 초기화합니다.
5. eventListner():
    - 버튼 이벤트 리스너를 등록하는 메소드입니다.
6. searchPOI():
    - 통합 검색 기능을 수행하는 메소드입니다.
    - 입력한 검색어로 TMapData의 findAllPOI 메소드를 호출하여 검색 결과를 가져옵니다.
    - 검색 결과를 다이얼로그로 표시합니다.
7. convertAddressToCoordinates():
    - 주소를 좌표로 변환하고 마커를 표시하는 메소드입니다.
    - TMapData의 findAddressPOI 메소드를 호출하여 주소를 좌표로 변환합니다.
    - 변환된 좌표로 마커를 생성하고 지도에 추가합니다.
8. locationListener:
    - 위치 변화를 감지하는 리스너입니다.
    - GPS로부터 현재 위치를 가져와 지도에 마커와 원을 표시합니다.
9. listener:
    - 버튼의 클릭 이벤트를 처리하는 리스너입니다.
    - 검색 버튼: 입력한 검색어로 통합 검색을 수행합니다.
    - 줌 인/줌 아웃 버튼: 지도의 줌 인/줌 아웃을 수행합니다.
    - 내 위치 버튼: 현재 위치로 지도를 이동합니다.
    - 중지 버튼: 주소와 경로를 제거하고 화면을 초기화합니다.
    - 경로 검색 버튼: 경로 검색을 위한 다이얼로그를 호출합니다.
10. hideSystemUI():
    - 안드로이드 시스템 UI를 숨기는 메소드입니다.
11. searchaDialog():
    - 주소 선택을 위한 다이얼로그를 생성합니다.
12. aDialog():
    - 경로 선택을 위한 다이얼로그를 생성합니다.
