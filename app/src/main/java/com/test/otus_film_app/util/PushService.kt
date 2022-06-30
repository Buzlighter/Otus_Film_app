package com.test.otus_film_app.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.test.otus_film_app.R
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.view.MainActivity
import kotlin.properties.Delegates
import kotlin.random.Random


class PushService: FirebaseMessagingService() {
    companion object {
        const val NEW_TOKEN = "NEW_TOKEN"
        const val CHANNEL_ID = "push_channel"
        lateinit var notificationManager: NotificationManager
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(NEW_TOKEN, "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val pushIntent = Intent(this, MainActivity::class.java)
        val film = Gson().fromJson(message.data["film"], Film::class.java)
        pushIntent.apply {
            putExtra(FILM_EXTRA, film)
            putExtra(Constants.FROM_NOTIFICATION, true)
        }

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, pushIntent, FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_push_notification)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "My channel description"
        }
        notificationManager.createNotificationChannel(channel)
    }

}