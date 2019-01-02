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
import com.tosun.ali.sohbetodam.Adapter.DersOdevAdapter
import com.tosun.ali.sohbetodam.Model.Odev
import com.tosun.ali.sohbetodam.Model.SohbetOdasi
import kotlinx.android.synthetic.main.tumodevler_fragment.*

class FragmentTumOdevler: Fragment() {

    lateinit var tumOdevler: ArrayList<Odev>
    var v:View? = null
    var myContext: Context?=null
    var myActivity:MainActivity?=null
    var sohbetOdasiİd:String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        v = inflater.inflate(R.layout.tumodevler_fragment, container, false)
        myContext = container!!.context

        var activityDersDetay = activity as DersDetaylariOgrenciActivity
        sohbetOdasiİd = activityDersDetay.getSohbetOdasiİd()
        Log.e("alxc",sohbetOdasiİd)

        odevlerVeriKaynagiOlustur()

        return v
    }

    private fun odevlerVeriKaynagiOlustur() {


        tumOdevler = ArrayList<Odev>()



        var reference = FirebaseDatabase.getInstance().reference
                .child("sohbet_odasi")
                .child(sohbetOdasiİd!!)
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


        var odevAdapter= DersOdevAdapter(tumOdevler,myContext!!)
        rvTumOdevler.adapter = odevAdapter
        rvTumOdevler.layoutManager = LinearLayoutManager(myContext!!, LinearLayout.VERTICAL, false)


    }

}