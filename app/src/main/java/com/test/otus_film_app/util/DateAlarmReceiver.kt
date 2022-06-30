package com.test.otus_film_app.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.test.otus_film_app.R
import com.test.otus_film_app.util.Constants.Companion.FROM_NOTIFICATION
import com.test.otus_film_app.view.MainActivity

const val NOTIFICATION_ID = 1
const val CHANNEL_ID = "my_channel"
const val TITTLE_EXTRA = "titleExtra"
const val MESSAGE_EXTRA = "messageExtra"
const val FILM_EXTRA = "film"

class DateAlarmReceiver: BroadcastReceiver() {
    companion object {
       var isDateHadCome = false
       lateinit var notificationManager: NotificationManager
    }

    override fun onReceive(context: Context, intent: Intent) {
        val film = intent.getSerializableExtra(FILM_EXTRA)
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.apply {
            putExtra(FILM_EXTRA, film)
            putExtra(FROM_NOTIFICATION, true)
        }

        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(notificationIntent)
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(TITTLE_EXTRA))
            .setContentText(intent.getStringExtra(MESSAGE_EXTRA))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .build()

        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
        isDateHadCome = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {
            val name = "notify_channel"
            val desc = "A Description of the Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = desc
            val notificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
    }


}