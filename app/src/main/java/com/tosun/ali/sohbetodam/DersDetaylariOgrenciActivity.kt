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
import com.tosun.ali.sohbetodam.Adapter.DersOdevAdapter
import com.tosun.ali.sohbetodam.Model.Dokuman
import com.tosun.ali.sohbetodam.Model.Odev
import kotlinx.android.synthetic.main.activity_ders_detaylari_ogrenci.*

class DersDetaylariOgrenciActivity : AppCompatActivity() {

    lateinit var tumDokumanlar: ArrayList<Dokuman>
    lateinit var tumOdevler: ArrayList<Odev>
    var dokumanIdSet: HashSet<String>? = null
    var myAdapter: DersDetayOgrenciAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ders_detaylari_ogrenci)

        dokumanlarVeriKaynagiOlustur()
        odevlerVeriKaynagiOlustur()


    }

    private fun odevlerVeriKaynagiOlustur() {


        tumOdevler = ArrayList<Odev>()


        var sohbetOdasiİd = intent.getStringExtra("sohbetodasi_id")
        var reference = FirebaseDatabase.getInstance().reference
                .child("sohbet_odasi")
                .child(sohbetOdasiİd)
                .child("odevler")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        for (data in p0.children) {

                            var geciciOdev = Odev()

                            geciciOdev.odev_ismi = data.getValue(Odev::class.java)!!.odev_ismi
                            geciciOdev.odev_teslim_tarihi =data.getValue(Odev::class.java)!!.odev_teslim_tarihi
                            geciciOdev.odev_teslim_saati=data.getValue(Odev::class.java)!!.odev_teslim_saati
                            geciciOdev.odev_teslim_yeri = data.getValue(Odev::class.java)!!.odev_teslim_yeri
                            geciciOdev.odev_aciklamasi = data.getValue(Odev::class.java)!!.odev_aciklamasi
                            geciciOdev.dosya_ismi = data.getValue(Odev::class.java)!!.dosya_ismi
                            geciciOdev.yukleyen_kisi = data.getValue(Odev::class.java)!!.yukleyen_kisi
                            geciciOdev.olusturulma_zamani = data.getValue(Odev::class.java)!!.olusturulma_zamani
                            geciciOdev.download_url = data.getValue(Odev::class.java)!!.download_url


                            tumOdevler.add(geciciOdev)


                        }

                        odevAdapterOlustur()


                    }

                })



    }

    private fun odevAdapterOlustur() {


        var odevAdapter=DersOdevAdapter(tumOdevler,this)
        rvOdevler.adapter = odevAdapter
        rvOdevler.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


    }

    fun dokumanAdapterOlustur() {


        myAdapter = DersDetayOgrenciAdapter(tumDokumanlar!!, this)
        Log.e("dokumans", tumDokumanlar.toString())
        rvDersDetaylariOgrenci.adapter = myAdapter
        rvDersDetaylariOgrenci.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

    }

    private fun dokumanlarVeriKaynagiOlustur() {

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
                            geciciDokuman.dokuman_adi = data.getValue(Dokuman::class.java)!!.dokuman_adi


                            Log.e("dokuman", geciciDokuman.toString())
                            tumDokumanlar.add(geciciDokuman)


                        }

                        dokumanAdapterOlustur()


                    }

                })


    }
}
