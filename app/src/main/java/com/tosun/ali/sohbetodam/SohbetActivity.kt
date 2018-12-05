package com.tosun.ali.sohbetodam

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tosun.ali.sohbetodam.Adapter.TumSohbetOdalarıAdapter
import com.tosun.ali.sohbetodam.Fragment.YeniSohbetOdasiOlusturFragment
import com.tosun.ali.sohbetodam.Model.SohbetMesaj
import com.tosun.ali.sohbetodam.Model.SohbetOdasi
import kotlinx.android.synthetic.main.activity_sohbet.*

class SohbetActivity : AppCompatActivity() {

    lateinit var tumSohbetOdalari: ArrayList<SohbetOdasi>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sohbet)
        init()


    }

    fun init() {
        tumSohbetOdalariniGetir()

        fabYeniSohbetOdasi.setOnClickListener {

            var dialog = YeniSohbetOdasiOlusturFragment()
            dialog.show(supportFragmentManager, "yenisohbetodasifragment")

        }


    }

    private fun tumSohbetOdalariniGetir() {

        tumSohbetOdalari = ArrayList<SohbetOdasi>()

        var FirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

        var FirebaseDatabaseQuery = FirebaseDatabaseRef.child("sohbet_odasi")

        FirebaseDatabaseQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                for (data in p0.children) {


                    var oAnkiSohbetOdasi = SohbetOdasi()

                    // HashMap kullanılmasının nedeni, sohbet_odasi childinin içinde bir ArrayList Var
                    // klasik yöntemler ile arrayList atamasını yapamayız.

                    var nesneMap = data.getValue() as HashMap<String, Object>

                    oAnkiSohbetOdasi.sohbetodasi_id = nesneMap.get("sohbetodasi_id").toString()
                    oAnkiSohbetOdasi.sohbetodasi_adi = nesneMap.get("sohbetodasi_adi").toString()
                    oAnkiSohbetOdasi.seviye = nesneMap.get("seviye").toString()
                    oAnkiSohbetOdasi.olusturan_id = nesneMap.get("olusturan_id").toString()


                    var tumMesajlar = ArrayList<SohbetMesaj>()

                    for (sohbetMesajlar in data.child("sohbet_odasi_mesajlari").children!!) {

                        var eklenecekSohbetMesaj = sohbetMesajlar.getValue(SohbetMesaj::class.java)

                        tumMesajlar.add(eklenecekSohbetMesaj!!)

                    }




                    oAnkiSohbetOdasi.sohbet_odasi_mesajlari = tumMesajlar

                    tumSohbetOdalari.add(oAnkiSohbetOdasi)


                    Log.e("deneme", oAnkiSohbetOdasi.toString())


                    /* var oAnkiSohbetOdasi = data.getValue(SohbetOdasi::class.java)
                     tumSohbetOdalari.add(oAnkiSohbetOdasi!!)
                     Log.e("deneme",oAnkiSohbetOdasi.toString()) -> HashMap hatası..*/
                }
                sohbetOdalariListele()


            }

        })
    }

    private fun sohbetOdalariListele() {
        var mySohbetOdasiAdapter = TumSohbetOdalarıAdapter(this@SohbetActivity, tumSohbetOdalari)
        rvSohbetOdalari.adapter = mySohbetOdasiAdapter
        rvSohbetOdalari.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    fun sohbetOdasiSil(sohbetOdasiİd: String) {

        var FirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

        FirebaseDatabaseRef.child("sohbet_odasi").child(sohbetOdasiİd).removeValue()

        Toast.makeText(this@SohbetActivity, "Sohbet odasi silindi", Toast.LENGTH_SHORT).show()

        init()

    }

}


