package com.example.pushnotification
//[firebase]
//모바일 앱이나 웹을 보다 쉽게 만들고 운영하기 위한 플랫폼
//실무에서 서비스를 만들고 운영하기까지 활용도 높은 직군들 제공
//
//[CloudMessaging]을 활용하여 push알림 수신기를 만듬
//
//[기능상세]
//-> Firebase 토큰을 확인할 수 있다.
//-> 일반, 확장형, 커스텀 알림을 볼 수 있다.
//
//[활용기술]
//-> Firebase Cloud Messaging
//-> Notification
//
//00프로젝트 셋업
//01. 기본UI구성
//02. CloudMessaging 구성
//03. Cloud Messaging 연동
//04. Notification 구현
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {

    private val resultTextView: TextView by lazy {
        findViewById(R.id.resultTextView)
    }
    private val firebaseToken: TextView by lazy {
        findViewById(R.id.firebaseTokenTextView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
        updateResult()


    }

    override fun onNewIntent(intent: Intent?) { //싱글탑일 경우에 호출하므로 오버라이드
        super.onNewIntent(intent)
        setIntent(intent)//기존에 가져온 concreate했을 때 인텐트가 있기에 새로들어온 것으로 교체해야함
        updateResult(true)
    }


    private fun initFirebase() {
        //firebase 토큰 가져옴(task를 가져온 것을 리스너를 통해 알림을 받음)
        FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseToken.text = task.result // uI에 추가
                    }

                }
    }

    private fun updateResult(isNewIntent: Boolean = false) {//notificationType이 있다면 그대로. null일 경우 앱런처로 실행했다로 판단
        resultTextView.text = (intent.getStringExtra("notificationType") ?: "앱 런처") +
                if (isNewIntent) {
                    "(으)로 갱신했습니다."
                } else {
                    "(으)로 실행했습니다.."
                }
    }
}