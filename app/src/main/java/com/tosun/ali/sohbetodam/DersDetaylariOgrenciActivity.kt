package com.tosun.ali.sohbetodam

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tosun.ali.sohbetodam.Adapter.DersDetayOgrenciAdapter
import com.tosun.ali.sohbetodam.Model.Dokuman
import kotlinx.android.synthetic.main.activity_ders_detaylari_ogrenci.*

class DersDetaylariOgrenciActivity : AppCompatActivity() {

    lateinit var tumDokumanlar: ArrayList<Dokuman>
    var dokumanIdSet: HashSet<String>? = null
    var myAdapter: DersDetayOgrenciAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ders_detaylari_ogrenci)

        veriKaynagiOlustur()


    }

    fun adapterOlustur() {


        myAdapter = DersDetayOgrenciAdapter(tumDokumanlar!!, this)
        Log.e("dokumans", tumDokumanlar.toString())
        rvDersDetaylariOgrenci.adapter = myAdapter
        rvDersDetaylariOgrenci.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

    }

    private fun veriKaynagiOlustur() {

        tumDokumanlar = ArrayList<Dokuman>()


        var sohbetOdasiİd = intent.getStringExtra("sohbetodasi_id")
        var reference = FirebaseDatabase.getInstance().reference
                .child("sohbet_odasi")
                .child(sohbetOdasiİd)
                .child("dokumanlar")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        for (data in p0.children) {

                            var geciciDokuman = Dokuman()

                            geciciDokuman.download_link = data.getValue(Dokuman::class.java)!!.download_link
                            geciciDokuman.olusturulma_zamani = data.getValue(Dokuman::class.java)!!.olusturulma_zamani
                            geciciDokuman.yukleyen_kisi = data.getValue(Dokuman::class.java)!!.yukleyen_kisi


                            Log.e("dokuman", geciciDokuman.toString())
                            tumDokumanlar.add(geciciDokuman)


                        }

                        adapterOlustur()


                    }

                })


    }
}
