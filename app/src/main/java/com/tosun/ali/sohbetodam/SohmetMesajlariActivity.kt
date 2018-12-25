package com.tosun.ali.sohbetodam

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tosun.ali.sohbetodam.Adapter.TumMesajlarAdapter
import com.tosun.ali.sohbetodam.Model.FcmModel
import com.tosun.ali.sohbetodam.Model.Kullanici
import com.tosun.ali.sohbetodam.Model.SohbetMesaj
import kotlinx.android.synthetic.main.activity_sohbet_odasi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SohmetMesajlariActivity : AppCompatActivity() {


    companion object {
        var activityAcikMi: Boolean = false

        //değişiklik

    }

    var myAuthStateListener: FirebaseAuth.AuthStateListener? = null

    var sohbetOdasiİd: String? = null

    var tumMesajlar: ArrayList<SohbetMesaj>? = null

    var myAdapter: TumMesajlarAdapter? = null

    var server_key: String? = null

    var mesajIdSet: HashSet<String>? = null

    var myThread: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sohbet_odasi)

        initMyAuthStateListener()

        sohbetOdasiniOgren()

        serverKeyOgren()

        init()


    }


    private fun serverKeyOgren() {
        var ref = FirebaseDatabase.getInstance().reference
                .child("server")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {


                var singleDataSnapShot = p0.children.iterator().next()



                server_key = singleDataSnapShot.getValue().toString()


            }


        })


    }

    private fun init() {

        etSohbetMesaj.setOnClickListener {

            recyclerViewSohbetMesajlari.smoothScrollToPosition(myAdapter!!.itemCount - 1)
        }


        imgSohbetMesajGönder.setOnClickListener {


            if (!etSohbetMesaj.text.toString().equals("")) {

                var yeniMesaj = etSohbetMesaj.text.toString()

                var sohbetMesaj = SohbetMesaj()
                sohbetMesaj.kullanici_id = FirebaseAuth.getInstance().currentUser!!.uid
                sohbetMesaj.mesaj = yeniMesaj
                sohbetMesaj.timestamp = getMesajTarih()

                var ref = FirebaseDatabase.getInstance().reference

                var referans = ref.child("sohbet_odasi")
                        .child(sohbetOdasiİd!!)
                        .child("sohbet_odasi_mesajlari")


                var yeniMesajId = referans.push().key


                referans.child(yeniMesajId!!)
                        .setValue(sohbetMesaj)


                //o an sohbet odasında bulunan herkese bildirim gönderilecektir.Oanki kullanıcı hariç
                bildirimGonder()
                etSohbetMesaj.setText("")


            }

        }

    }

    private fun bildirimGonder() {

        var ref = FirebaseDatabase.getInstance().reference
                .child("sohbet_odasi")
                .child(sohbetOdasiİd!!)
                .child("sohbet_odasi_kayitli_kisiler")
                .orderByKey()
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for (kullaniciİd in p0!!.children) {

                            var id = kullaniciİd.key


                            // kişinin kendisine bildirim göndermemesi için
                            if (!id.equals(FirebaseAuth.getInstance().currentUser!!.uid)) {

                                FirebaseDatabase.getInstance().reference
                                        .child("kullanici")
                                        .orderByKey()
                                        .equalTo(id)
                                        .addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            override fun onDataChange(p0: DataSnapshot) {


                                                var tekKullanici = p0?.children?.iterator().next()
                                                var gelenMesajToken = tekKullanici.getValue(Kullanici::class.java)!!.mesaj_token


                                                var myInterface = ApiClient.client?.create(ApiInterface::class.java)

                                                var myHeaders: HashMap<String, String> = HashMap<String, String>()
                                                myHeaders.put("Content-Type", "application/json")
                                                myHeaders.put("Authorization", "key=" + server_key)


                                                var to = gelenMesajToken
                                                var data = FcmModel.Data("Yeni mesajınız var", etSohbetMesaj.text.toString(), "canli", sohbetOdasiİd!!)
                                               // etSohbetMesaj.setText("")

                                                var myBody = FcmModel(to, data)


                                                var apiCall = myInterface?.bildirimleriGonder(myHeaders, myBody)

                                                apiCall?.enqueue(object : Callback<Response<FcmModel>> {
                                                    override fun onFailure(call: Call<Response<FcmModel>>, t: Throwable) {
                                                        Log.e("FCM", "Başarısız")

                                                    }

                                                    override fun onResponse(call: Call<Response<FcmModel>>, response: Response<Response<FcmModel>>) {
                                                        Log.e("FCM", "başarılı")

                                                    }

                                                })


                                            }

                                        })


                            }


                        }


                    }

                })


    }

    private fun getMesajTarih(): String {
        var myFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("tr"))
        return myFormat.format(Date())

    }


    override fun onStart() {
        FirebaseAuth.getInstance().addAuthStateListener(myAuthStateListener!!)
        activityAcikMi = true
        super.onStart()
    }

    override fun onStop() {

        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(myAuthStateListener!!)

        }
        activityAcikMi = false
        super.onStop()
    }

    private fun initMyAuthStateListener() {

        var kullanici = FirebaseDatabase.getInstance().reference

        myAuthStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {

                if (kullanici == null) {
                    loginSayfasinaYonlendir()
                }

            }

        }

    }


    private fun sohbetOdasiniOgren() {
        /*
        TumSohbetOdalarıAdapterden o an tıklanan sohbet odasının id bilgisini öğrenir
         */
        sohbetOdasiİd = intent.getStringExtra("sohbetodasi_id")

        /*
        Sohbet Odası öğrenildikten sonra var olan measjlar ve değişiklikler için baslatMesajListener() metodu tetiklenir.
         */
        baslatMesajListener()

    }


    /*
    Veritabanına her mesaj childi eklendiğinde onDataChange metodu çalışır.
     */
    var myValueEventListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
        }

        override fun onDataChange(p0: DataSnapshot) {

            /*
            p0 da sadece değişen veri vardır fakat , her mesaj atıldığında tüm mesajları getirmek en doğrusu olabilir.

             */
            sohbetOdasindakiMesajlariGetir(p0.children.count())
            if (activityAcikMi)
                okunanMesajSayisiniGüncelle(p0.children.count())
        }

    }

    private fun okunanMesajSayisiniGüncelle(okunanMesajSayisi: Int) {

        var ref = FirebaseDatabase.getInstance().reference
                .child("sohbet_odasi")
                .child(sohbetOdasiİd!!)
                .child("sohbet_odasi_kayitli_kisiler")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("okunan_mesaj_sayisi")
                .setValue(okunanMesajSayisi)


    }

    private fun sohbetOdasindakiMesajlariGetir(tümMesajSayisi:Int) {

        if (tumMesajlar == null) {
            //sohbet odasında hiç mesaj yoksa , yani ilk defa çalışıyorsa tumMesajlar listesini oluştur.
            tumMesajlar = ArrayList<SohbetMesaj>()


            //mevcut mesajların uniq keyleri hashSete kaydedilir ve tüm mesajlar çekildiği zaman.
            // hashsete daha önceden set edilmiş ıd ler gösterilmez
            //böylece hep yeni mesajları görürüz
            mesajIdSet = HashSet<String>()

        }


        var FirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

        var FirebaseDatabaseQuery = FirebaseDatabaseRef
                .child("sohbet_odasi")
                .child(sohbetOdasiİd!!)
                .child("sohbet_odasi_mesajlari")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        for (mesaj in p0!!.children) {

                            var geciciMesaj = SohbetMesaj()

                            var kullaniciİd = mesaj.getValue(SohbetMesaj::class.java)!!.kullanici_id
                            Log.e("denemes", kullaniciİd + " kullanici id")

                            if (!mesajIdSet!!.contains(mesaj.key)) {
                                Log.e("sohbetKey", p0.key)
                                Log.e("sohbetKeys", mesaj.key)
                                mesajIdSet!!.add(mesaj.key!!)


                                if (!kullaniciİd.isNullOrEmpty()) {

                                    geciciMesaj.mesaj = mesaj.getValue(SohbetMesaj::class.java)!!.mesaj
                                    geciciMesaj.kullanici_id = mesaj.getValue(SohbetMesaj::class.java)!!.kullanici_id
                                    geciciMesaj.timestamp = mesaj.getValue(SohbetMesaj::class.java)!!.timestamp




                                    fun bilgileriGetir() {
                                        tumMesajlar?.add(geciciMesaj)
                                        myAdapter?.notifyDataSetChanged()
                                        recyclerViewSohbetMesajlari.scrollToPosition(myAdapter!!.itemCount - 1)
                                    }


                                    var kullaniciDetaylari = FirebaseDatabase.getInstance().reference.child("kullanici").orderByKey().equalTo(kullaniciİd)

                                    kullaniciDetaylari.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onCancelled(p0: DatabaseError) {

                                        }

                                        override fun onDataChange(p0: DataSnapshot) {

                                            for (bulunanKullanici in p0.children) {
                                                geciciMesaj.profil_resmi = bulunanKullanici.getValue(Kullanici::class.java)!!.profil_resim
                                                geciciMesaj.adi = bulunanKullanici.getValue(Kullanici::class.java)!!.isim

                                                bilgileriGetir()
                                            }

                                        }


                                    })


                                } else {
                                    geciciMesaj.mesaj = mesaj.getValue(SohbetMesaj::class.java)!!.mesaj
                                    geciciMesaj.adi = "veli"
                                    geciciMesaj.timestamp = mesaj.getValue(SohbetMesaj::class.java)!!.timestamp
                                    geciciMesaj.profil_resmi = ""
                                    tumMesajlar!!.add(geciciMesaj)

                                }

                            }


                        }
                        //&& tümMesajSayisi==tumMesajlar!!.size

                    }



                })

        if (myAdapter == null) {
            initMesajlarListesi()
        }





    }

    private fun initMesajlarListesi() {


        Log.e("mesajSize", "${tumMesajlar?.size}")
        myAdapter = TumMesajlarAdapter(this, tumMesajlar!!)
        recyclerViewSohbetMesajlari.adapter = myAdapter
        recyclerViewSohbetMesajlari.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


    }




    private fun baslatMesajListener() {

        /*
        bu fonksiyonun amacı, sohbet_odasi_mesajlari childina bir veri eklenip eklenmediğini kontrol eder.
        veri eklendiyse myValueEventListener interface ni tetikler.
         */

        var FirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

        FirebaseDatabaseRef.child("sohbet_odasi").child(sohbetOdasiİd!!).child("sohbet_odasi_mesajlari")
                .addValueEventListener(myValueEventListener)
    }

    private fun loginSayfasinaYonlendir() {

        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }

}


/*private fun sohbetOdasiMesajlariniGetir() {

        tumMesajlar = ArrayList<SohbetMesaj>()

        var FirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

        var FirebaseDatabaseQuery = FirebaseDatabaseRef.child("sohbet_odasi")
                .child(sohbetodasi_id)
                .child("sohbet_odasi_mesajlari")

        FirebaseDatabaseQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                for (mesaj in p0.children) {


                    var eklenecekMesaj = SohbetMesaj()

                    var kullaniciİd = mesaj.getValue(SohbetMesaj::class.java)?.kullanici_id

                    //karşılama mesajı değilse.
                    if (!kullaniciİd.isNullOrEmpty()) {

                        eklenecekMesaj.mesaj = mesaj.getValue(SohbetMesaj::class.java)!!.mesaj
                        eklenecekMesaj.timestamp = mesaj.getValue(SohbetMesaj::class.java)!!.timestamp
                        eklenecekMesaj.kullanici_id = mesaj.getValue(SohbetMesaj::class.java)!!.kullanici_id
                        eklenecekMesaj.profil_resmi = mesaj.getValue(SohbetMesaj::class.java)!!.profil_resmi

                    } else {
                        eklenecekMesaj.mesaj = mesaj.getValue(SohbetMesaj::class.java)!!.mesaj
                        eklenecekMesaj.timestamp = mesaj.getValue(SohbetMesaj::class.java)!!.timestamp
                    }

                    tumMesajlar.add(eklenecekMesaj)

                    Log.e("deneme",sohbetodasi_id.toString())
                }



            }

        })


    }*/