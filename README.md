# viewpager

- 총 3개의 탭으로 이루어진 앱
- 옆으로 이동하는 스와이프를 통해 각각의 탭으로 이동할 수 있습니다.
- tedPermission을 이용해서 앱이 실행될 때, 권한을 허용하도록 요청합니다.


## 1. TAB1 : 연락처

 - 핸드폰 내부에 있는 연락처를 가져옵니다.
 - SearchView 기능을 이용하여, 돋보기 모양 아이콘을 누르면 원하는 연락처를 찾을 수 있습니다.
 
 <img src = "https://user-images.githubusercontent.com/80759746/124569496-f60a9180-de80-11eb-99e1-75ab7360b7ff.jpg" width="400" height="700">

 
## 2. TAB2 : 갤러리

 - 핸드폰 내부에 있는 갤러리를 가져옵니다.
 - RecycleView를 이용해 사진을 나타냅니다.
 - 오른쪽 하단에 있는 버튼을 누르면 카메라로 사진을 촬영할 수 있습니다.
 - 촬영한 사진은 갤러리에 저장됩니다.
 - 위로 스와이프하면, 갤러리와 동기화됩니다.

<img src = "https://user-images.githubusercontent.com/80759746/124569824-42ee6800-de81-11eb-85a5-98eea6302dd5.jpg" width="400" height="700">


## 3. TAB3 : 날씨 API / 노래 추천
 - 사용자가 현재 위치한 장소의 날씨를 기반으로 노래를 재생해주는 탭
 - 날씨를 아이콘으로 나타내고, 오른쪽에는 현재 기온을 나타내줍니다.
 - 날씨에 맞춰 노래가 바뀝니다.
 - 각 날씨별로 3개의 노래가 저장되어 있고, 다음/이전 버튼을 누르면 다른 노래를 재생할 수 있습니다.
 - Progress Bar를 원하는 위치에 누르면 그 위치에서 노래가 재생됩니다.
 
 <img src = "https://user-images.githubusercontent.com/80759746/124577700-77195700-de88-11eb-96e0-8076ff971d8a.jpg" width="400" height="700">


