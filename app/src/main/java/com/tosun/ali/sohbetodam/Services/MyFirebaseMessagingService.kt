package com.tosun.ali.sohbetodam.Services

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tosun.ali.sohbetodam.Model.SohbetOdasi
import com.tosun.ali.sohbetodam.R
import com.tosun.ali.sohbetodam.SohmetMesajlariActivity
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    var okunmayiBekleyenMesajSayisi = 0

    override fun onMessageReceived(p0: RemoteMessage?) {

        // activity açık değilse
        if (!SohmetMesajlariActivity.activityAcikMi) {


            var bildirimBaslik = p0?.notification?.title

            var bildirimBody = p0?.notification?.body

            var data = p0?.data


            var baslik = p0?.data?.get("baslik")
            var icerik = p0?.data?.get("icerik")
            var sohbet_odasi_id = p0?.data!!.get("sohbet_odasi_id")
            var bildirim_turu = p0?.data.get("bildirim_turu")


            //bildirimde gönderilecek mesaj sayiları ve sohbetodasi bilgilerinin alınması.
            var ref = FirebaseDatabase.getInstance().reference
                    .child("sohbet_odasi")
                    .orderByKey()
                    .equalTo(sohbet_odasi_id)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        @SuppressLint("NewApi")
                        @TargetApi(Build.VERSION_CODES.M)
                        @RequiresApi(Build.VERSION_CODES.M)
                        override fun onDataChange(p0: DataSnapshot) {

                            var tekSohbetOdasiSnapShot = p0.children.iterator().next()

                            var tekSohbetOdasi = tekSohbetOdasiSnapShot.getValue() as HashMap<String, Objects>

                            var oAnkiSohbetOdasi = SohbetOdasi()

                            oAnkiSohbetOdasi.olusturan_id = tekSohbetOdasi.get("olusturan_id").toString()
                            oAnkiSohbetOdasi.sohbetodasi_adi = tekSohbetOdasi.get("sohbetodasi_adi").toString()
                            oAnkiSohbetOdasi.sohbetodasi_id = tekSohbetOdasi.get("sohbetodasi_id").toString()
                            oAnkiSohbetOdasi.seviye = tekSohbetOdasi.get("seviye").toString()


                            var okunanMesajSayisi = tekSohbetOdasiSnapShot.child("sohbet_odasi_kayitli_kisiler")
                                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .child("okunan_mesaj_sayisi")
                                    .getValue().toString().toInt()


                            var toplamMesajSayisi = tekSohbetOdasiSnapShot?.child("sohbet_odasi_mesajlari")!!.childrenCount.toInt()

                            okunmayiBekleyenMesajSayisi = toplamMesajSayisi - okunanMesajSayisi


                            //bildirimGonder(baslik,icerik,oAnkiSohbetOdasi)

                            createNotification("aMessage", icerik.toString(), oAnkiSohbetOdasi, this@MyFirebaseMessagingService)

                        }

                    })

            Log.e("FCM", "bildirim baslik $baslik bildirim icerk \n $icerik \n sohbet odasi id $sohbet_odasi_id \n bildirim türü $bildirim_turu")
        }


    }


    @TargetApi(Build.VERSION_CODES.M)
    fun createNotification(aMessage: String, icerik: String, oAnkiSohbetOdasi: SohbetOdasi, context: Context) {
        val NOTIFY_ID = notificationIdOlustur() // ID of notification
        var notifManager: NotificationManager? = null

        val id = "default channel id" // default_channel_id
        val title = "default channel" // Default Channel
        //val intent: Intent
        //val pendingIntent: PendingIntent
        val builder: NotificationCompat.Builder
        if (notifManager == null) {
            notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel = notifManager!!.getNotificationChannel(id)
            if (mChannel == null) {
                mChannel = NotificationChannel(id, title, importance)
                mChannel!!.enableVibration(true)
                mChannel!!.setVibrationPattern(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                notifManager!!.createNotificationChannel(mChannel)
            }
            builder = NotificationCompat.Builder(context, id)
            // intent = Intent(context, MainActivity::class.java)
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            //pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            builder.setSmallIcon(R.drawable.ic_notifications_active)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_active))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentTitle(oAnkiSohbetOdasi.sohbetodasi_adi + " odasından" + " yeni mesajınız var")
                    .setContentText("" + icerik)
                    .setColor(getColor(R.color.colorAccent))
                    .setAutoCancel(true)
                    .setSubText("" + okunmayiBekleyenMesajSayisi + " yeni mesaj")
                    .setStyle(NotificationCompat.BigTextStyle().bigText(icerik))
                    .setNumber(NOTIFY_ID)
                    .setOnlyAlertOnce(true)
                    .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
        } else {
            builder = NotificationCompat.Builder(context, id)
            // intent = Intent(context, MainActivity::class.java)
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            //pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            builder.setSmallIcon(R.drawable.ic_notifications_active)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_active))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentTitle(oAnkiSohbetOdasi.sohbetodasi_adi + "odasından" + "yeni mesajınız var")
                    .setContentText("" + icerik)
                    .setColor(getColor(R.color.colorAccent))
                    .setAutoCancel(true)
                    .setSubText("" + okunmayiBekleyenMesajSayisi + " yeni mesaj")
                    .setStyle(NotificationCompat.BigTextStyle().bigText(icerik))
                    .setNumber(NOTIFY_ID)
                    .setOnlyAlertOnce(true)
                    .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
        }

        val notification = builder.build()
        notifManager!!.notify(NOTIFY_ID, notification)
    }


    @RequiresApi(Build.VERSION_CODES.M)


    private fun notificationIdOlustur(): Int {

        var sayi = (100..999).random() + (100..999).random() + (100..999).random()

        return sayi


    }

    fun IntRange.random() =
            Random().nextInt((endInclusive + 1) - start) + start
}



