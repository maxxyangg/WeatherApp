package com.example.weatherapp

import android.Manifest
import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.example.weatherapp.fragments.SearchFragment
import com.example.weatherapp.fragments.SearchFragmentDirections
import com.example.weatherapp.viewmodels.SearchViewModel
import com.google.android.gms.location.*
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.random.Random


class MyService : Service() {
    private val CHANNEL_ID = "channelID_1"
    private val notificationId = 101
    private lateinit var builder: Notification
    private lateinit var notificationManager: NotificationManager



    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(isMyNotificationRunning()){
            println("Stopping service")
            stopSelf()
        }else{
            println("Building Notification and starting foreground")
            var cityName = intent?.getStringExtra("CityName")
            var currentTemp = intent?.getFloatExtra("CurrentTemp", 0.0F)
            if (cityName != null) {
                if (currentTemp != null) {
                    buildNotification(cityName,currentTemp)
                }
            }
        }
        return START_STICKY
    }


    override fun onDestroy() {
        notificationManager.cancelAll()
        super.onDestroy()
    }



    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)



            println("Creating Notification Channel")

        }
    }

    private fun buildNotification(name: String, temp : Float){


        val notificationIntent = Intent(this, SearchFragment::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.sun)
            .setContentTitle(name)
            .setContentText("${temp.toInt().toString()}°")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()


        startForeground(notificationId, builder)

    }





    @RequiresApi(Build.VERSION_CODES.M)
    fun isMyNotificationRunning(): Boolean {
        var notifications = notificationManager.activeNotifications
        for(notification in notifications){
            if(notification.id == notificationId){
                return true
            }
        }

        return false
    }







}