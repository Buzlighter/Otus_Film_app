package com.test.otus_film_app.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.test.otus_film_app.model.Film
import com.test.otus_film_app.model.NotificationData
import com.test.otus_film_app.model.PushNotification
import com.test.otus_film_app.view.details_screen.TOPIC
import com.test.otus_film_app.viewmodel.PushServiceViewModel
import java.text.SimpleDateFormat
import java.util.*

class SendNotificationAlarmService(val context: Context, val calendar: Calendar) {

    fun dateFormat(day: Int, month: Int, year: Int): String {
        val dateString = "${day}.${month + 1}.${year}"
        val dateFormat = SimpleDateFormat("dd.mm.yyyy", Locale.getDefault())
        val date = dateFormat.parse(dateString) ?: ""

        return dateFormat.format(date)
    }


    fun send(title: String, message: String, film: Film, pushViewModel: PushServiceViewModel,
             scheduledDay: Int, scheduledMonth: Int, scheduledYear: Int,
             currentDay: Int, currentMonth: Int, currentYear: Int) {

        val alarmIntent = Intent(context.applicationContext, DateAlarmReceiver::class.java)

        alarmIntent.apply {
            putExtra(FILM_EXTRA, film)
            putExtra(TITTLE_EXTRA, title)
            putExtra(MESSAGE_EXTRA, message)
        }

        if (currentDay == scheduledDay && currentMonth == scheduledMonth && currentYear == scheduledYear) {
            val pushNotification = PushNotification(
                NotificationData(title, message, film),
                TOPIC
            )
            pushViewModel.sendNotification(pushNotification)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            NOTIFICATION_ID,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = calendar.timeInMillis
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }
}