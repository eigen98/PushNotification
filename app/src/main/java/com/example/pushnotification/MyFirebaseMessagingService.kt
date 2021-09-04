package com.example.pushnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage
import java.util.Date.from

class MyFirebaseMessagingService : FirebaseMessagingService() {

    //    ->토큰은 굉장히 자주 변경될 수 있음. (삭제 및 재설치, 새기기에서 복원, 데이터 소거)
//    -> onNewToken을 overriding 을통해 토큰 갱신필요
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    //메시지 수신할 때마다 호출 메소드-> manifest에 추가
    override fun onMessageReceived(remotemessage: RemoteMessage) {
        super.onMessageReceived(remotemessage)

        createNotificationChannel() //채널만들기


        val type = remotemessage.data["type"]
                ?.let {
                    NotificationType.valueOf(it)
                }
        val title = remotemessage.data["title"]
        val message = remotemessage.data["message"]

        type ?: return //타입이 널일 경우 알림생성 X


        //알림 Notify
        NotificationManagerCompat.from(this)
                .notify(type.id, createNotification(type, title, message))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//오레오 이상일 경우 채널을 만들어줌
            val channel = NotificationChannel(// (id,name, 중요도)
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            )


            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .createNotificationChannel(channel)
        }
    }
    //알림컨텐츠(빌더에서 설정)
    private fun createNotification(
            type : NotificationType,
            title: String?,
            message: String?
    ): Notification { //타입,타이틀,메시지

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType","${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)// 같은 게 있을 경우 하나만 생김(OnNewIntent메서드 호출) 기존 화면 갱신. 똑같은 화면 새로 안 생김//(작업 및 백스택)
        }

        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)//내가 직접다루는 것이 아닌 누군가에게 인텐트를 주는 권한
        //pendingIntent를 조회를 할 때 안에 들어가있는 데이터가 같을 경우 그것을 다시 가져올 수 있음. (계속오는 메시지가 동일하면 pendingIntent을 계속생성해도 동일한 pendingIntent)

        val notificationBuilder =  NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)//알림 아이콘
                .setContentTitle(title)//타이틀 정하기
                .setContentText(message)//메시지 정하기
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)//우선순위
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)


        when(type){
            NotificationType.Normal -> Unit //일반형 -> 아무것도 안 함
            NotificationType.EXPANDABLE->{
                //확장형 -> 큰이미지 추가, 큰 텍스트블럭추가, 받은편지함 스타일, 대화표시
                notificationBuilder.setStyle(
                        NotificationCompat.BigTextStyle()
                                .bigText(
                                       " 😀 😃 😄 😁 😆 😅 😂 🤣 😇 😉 😊 🙂 🙃 ☺ 😋 😌 😍 🥰 😘 😗 😙 😚 🥲 🤪 😜 😝 😛 🤑 😎 🤓 "+
                                               "😣 😖 😫 😩 🥱 😤 😮‍💨 😮 😱 😨 😰 😯 😦 😧 😢 😥 😪 🤤 😓 😭 🤩 😵 😵‍💫 🥴 😲 🤯 🤐 😷 🤕 🤒 🤮 🤢 🤧 🥵 🥶 😶‍🌫"+
                                               "😴 💤 😈 👿 👹 👺 💩 👻 💀 ☠ 👽 🤖 🎃 😺 😸 😹 😻"
                                )
                )
            }
            NotificationType.CUSTOM ->{ //커스텀형 알림 추가
                notificationBuilder
                        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                        .setCustomContentView(
                                RemoteViews(
                                        packageName,
                                        R.layout.view_custom_notification
                                ).apply {
                                    setTextViewText(R.id.title, title)
                                    setTextViewText(R.id.message, message)
                                }
                        )

            }
        }
        return notificationBuilder.build()

    }

    companion object {
        //채널 이름
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel Id"
    }
}