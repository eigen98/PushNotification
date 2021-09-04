# PushNotification

### [firebase]   
모바일 앱이나 웹을 보다 쉽게 만들고 운영하기 위한 플랫폼   
실무에서 서비스를 만들고 운영하기까지 활용도 높은 직군들 제공   

[CloudMessaging]을 활용하여 push알림 수신기를 만듬   
![diagram-FCM](https://user-images.githubusercontent.com/68258365/132081597-eb348c4e-0c32-4a93-a969-2b8b26ce3abd.png)


### [기능상세]
-> Firebase 토큰을 확인할 수 있다.   
-> 일반, 확장형, 커스텀 알림을 볼 수 있다.   

### [활용기술]   
-> Firebase Cloud Messaging   
(FCM)은 무료로 메시지를 안정적으로 전송할 수 있는 교차 플랫폼 메시징 솔루션   
	1. 알림 메시지(구현이 쉽지만 유연하게 처리 불가)   
	2. 데이터 메시지(앱에서 처리하기에 유연하게 처리) 대부분사용   
-> Notification    


![푸시알림수신기](https://user-images.githubusercontent.com/68258365/132081521-265852b7-34a2-4669-8d38-689e2524c3a2.png)


### 00프로젝트 셋업

### 01. 기본UI구성
->RTL과 LTL (left대신 startMargin사용 이유)   

### 02. CloudMessaging 구성
https://firebase.google.com/docs/projects/learn-more?hl=ko   
-> firebase프로젝트가 만들어진 후(구글 프로젝트 자동) 안드로이드 등록   
-> firebase CloudMessaging을 firebase project와 구성.    
->제대로 동작했는지firebase CloudMessaging Gui툴을 이용해서 받아보는 것 까지 작업.   
-> 현재 등록된 토큰정보를 가져와서 그것을 통해 메시지를 보냄   
(충돌시)   
apply plugin: 'com.google.gms.google-services' ->id 'com.google.gms.google-services' 로 변경 하면 성공   

### 03. Cloud Messaging 연동   
https://firebase.google.com/docs/cloud-messaging/android/receive?hl=ko   
-> firebaseservice클래스 생성 후 FirebaseMessagingService() 상속   
->토큰은 굉장히 자주 변경될 수 있음. (삭제 및 재설치, 새기기에서 복원, 데이터 소거)   
-> onNewToken을 overriding //토큰 갱신   
-> onMessageReceived //메시지 수신될 때마다 호출   
->manifest에서 application안에 service 추가 (안에 intent-filter추가 ) //앱에서 필터에 해당되는 이벤트를 수신하겠다. <action android:name="com.google.firebase.MESSAGING_EVENT"/>   
->android:exported="false"    
연동 완료    
->확인을위해 별도의 서버를 구현, api요청을 curl을통해 날려야함 but 클라우드메시징 send에서 가능(Try this API이용)   
-> 구상했던 FCM을 서비스에 등록해서 프로젝트에 연동하는 작업까지 진행 완료   
04. Notification 기능 구현(푸시, 음악컨트롤러, 클라우드 메시지의 데이터를 수신해서 알림을 보여주는 기능)   
-> 굉장히 자주 업데이트 -> 호환성을 잘 챙겨야함 ->호환성문서 https://developer.android.com/guide/topics/ui/notifiers/notifications?hl=ko#compatibility   
->안드로이드 8.0이상 (채널 만들고  모든 알림 할당 ,관리)    
->채널을 먼저 만든다   
-> enum class정의(enumerated type 열거형)   
	열거형 클래스(Enum Class)   
	-> 상태를 구분하기 위한 객체들을 이름을 붙여 여러개 생성.   
	-> 그중하나의 상태를 선택하여 나타내기위함.    
	-> 이넘의 객체들은 고유한    
	-> valuOf()로   
->일반,확장형,커스텀 알림을 구분하여 보여주는 작업    
	 //확장형 -> 큰이미지 추가, 큰 텍스트블럭추가, 받은편지함 스타일, 대화표시   

->알림 탭 작업 설정 PendingIntent객체로 setContentIntent로 전달   
-> 인텐트 플래그 사용(기존 화면 갱신)   
	//pendingIntent를 조회를 할 때 안에 들어가있는 데이터가 같을 경우 그것을 다시 가져올 수 있음. (계속오는 메시지가 동일하면 pendingIntent을 계속생성해도 동일한 pendingIntent)   



