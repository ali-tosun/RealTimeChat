package com.tosun.ali.sohbetodam

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tosun.ali.sohbetodam.Adapter.DersDetayOgrenciAdapter
import com.tosun.ali.sohbetodam.Model.Dokuman
import kotlinx.android.synthetic.main.tumdersdokumanlari_fragment.*

class FragmentTumDersDokumanlari: Fragment() {
    lateinit var tumDokumanlar: ArrayList<Dokuman>
    var v:View? = null
    var myContext: Context?=null
    lateinit var sohbetOdasiİd:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = inflater.inflate(R.layout.tumdersdokumanlari_fragment, container, false)
        myContext = container!!.context

        var activityDersDetay = activity as DersDetaylariOgrenciActivity
        sohbetOdasiİd = activityDersDetay.getSohbetOdasiİd()
        Log.e("avx",sohbetOdasiİd)
        dokumanlarVeriKaynagiOlustur()

        return v

    }

    private fun dokumanlarVeriKaynagiOlustur() {

        tumDokumanlar = ArrayList<Dokuman>()



        var reference = FirebaseDatabase.getInstance().reference
                .child("sohbet_odasi")
                .child(sohbetOdasiİd!!)
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


    fun dokumanAdapterOlustur() {


        var myAdapter = DersDetayOgrenciAdapter(tumDokumanlar!!, myContext!!)
        Log.e("dokumans", tumDokumanlar.toString())
        rvTumDersDokumanlari.adapter = myAdapter
        rvTumDersDokumanlari.layoutManager = LinearLayoutManager(myContext!!, LinearLayout.VERTICAL, false)

    }



}