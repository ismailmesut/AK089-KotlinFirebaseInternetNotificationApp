package com.ismailmesutmujde.kotlinfirebaseinternetnotificationapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessageService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
        val content = message.notification?.body

        Log.e("Title", title!!)
        Log.e("Content", content!!)

        createNotification(title, content)
    }

    fun createNotification(title:String?, content:String?) {
        val builder: NotificationCompat.Builder

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java)

        /* You can't get notifications with this code. Use the alternative code below!!
        val goToIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)*/

        // alternative code
        var goToIntent: PendingIntent? = null
        goToIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 1,intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 1,intent, PendingIntent.FLAG_IMMUTABLE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelId = "channelId"
            val channelName = "channelName"
            val channelPromotion = "channelPromotion"
            val channelPriority = NotificationManager.IMPORTANCE_HIGH

            var channel : NotificationChannel? = notificationManager.getNotificationChannel(channelId)

            if (channel == null) {
                channel = NotificationChannel(channelId, channelName, channelPriority)
                channel.description = channelPromotion
                notificationManager.createNotificationChannel(channel)
            }

            builder = NotificationCompat.Builder(this, channelId)
            builder.setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.image)
                .setContentIntent(goToIntent)
                .setAutoCancel(true)

        } else {

            builder = NotificationCompat.Builder(this)
            builder.setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.image)
                .setContentIntent(goToIntent)
                .setAutoCancel(true)
                .priority = Notification.PRIORITY_HIGH

        }

        notificationManager.notify(1, builder.build())
    }

}