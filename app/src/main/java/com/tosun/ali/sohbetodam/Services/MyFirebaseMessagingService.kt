package com.tosun.ali.sohbetodam.Services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tosun.ali.sohbetodam.Model.SohbetOdasi
import com.tosun.ali.sohbetodam.SohmetMesajlariActivity
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

var okunmayiBekleyenMesajSayisi = 0

    override fun onMessageReceived(p0: RemoteMessage?) {

        // activity açık değilse
        if(!SohmetMesajlariActivity.activityAcikMi){



        var bildirimBaslik = p0?.notification?.title

        var bildirimBody = p0?.notification?.body

        var data = p0?.data


        var baslik  = p0?.data?.get("baslik")
        var icerik =p0?.data?.get("icerik")
        var sohbet_odasi_id =p0?.data!!.get("sohbet_odasi_id")
        var bildirim_turu = p0?.data.get("bildirim_turu")



        //bildirimde gönderilecek mesaj sayiları ve sohbetodasi bilgilerinin alınması.
        var ref = FirebaseDatabase.getInstance().reference
                .child("sohbet_odasi")
                .orderByKey()
                .equalTo(sohbet_odasi_id)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        var tekSohbetOdasiSnapShot = p0.children.iterator().next()

                        var tekSohbetOdasi = tekSohbetOdasiSnapShot.getValue() as HashMap<String,Objects>

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


                        bildirimGonder(baslik,icerik,oAnkiSohbetOdasi)


                    }

                })

            Log.e("FCM","bildirim baslik $baslik bildirim icerk \n $icerik \n sohbet odasi id $sohbet_odasi_id \n bildirim türü $bildirim_turu")
        }






    }

    private fun bildirimGonder(baslik: String?, icerik: String?, oAnkiSohbetOdasi: SohbetOdasi) {

        var notificationSayisiOlustur = notificationIdOlustur(oAnkiSohbetOdasi.sohbetodasi_id)
        Log.e("FCM","SAYI DENEMESİ $notificationSayisiOlustur")

    }

    private fun notificationIdOlustur(sohbetOdasiİd:String):Int{
        var sayi=0
        for(i in 0..10){
            sayi = sayi+sohbetOdasiİd[i].toInt()

        }

        return sayi


    }

}