package com.tosun.ali.sohbetodam

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tosun.ali.sohbetodam.Adapter.TumSohbetOdalarıAdapter
import com.tosun.ali.sohbetodam.Model.SohbetMesaj
import com.tosun.ali.sohbetodam.Model.SohbetOdasi
import kotlinx.android.synthetic.main.tumdersler_fragment.*

class FragmentTumDersler:Fragment(){

    lateinit var tumSohbetOdalari: ArrayList<SohbetOdasi>
    var v:View? = null
    var myContext:Context?=null
    var myActivity:MainActivity?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        v = inflater.inflate(R.layout.tumdersler_fragment,container,false)

        val activity = activity as MainActivity
        myActivity =activity.getMainActivity()
        myContext = container!!.context

        //myActivity = arguments!!.getSerializable("myActivity") as MainActivity
        //Log.e("asdasd",myActivity.toString())

        init()

        return v



    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }




    fun sohbetOdasiSil(sohbetOdasiİd: String) {

        var FirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

        FirebaseDatabaseRef.child("sohbet_odasi").child(sohbetOdasiİd).removeValue()

        Toast.makeText(myContext, "Sohbet odasi silindi", Toast.LENGTH_SHORT).show()

        init()

    }


    private fun sohbetOdalariListele() {
        var mySohbetOdasiAdapter = TumSohbetOdalarıAdapter(myActivity!!, tumSohbetOdalari)
        rvTumSohbetler.adapter = mySohbetOdasiAdapter
        rvTumSohbetler.layoutManager = LinearLayoutManager(myContext, LinearLayoutManager.VERTICAL, false)

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


    fun init() {
        tumSohbetOdalariniGetir()

     /*   fabYeniSohbetOdasi.setOnClickListener {

            var dialog = YeniSohbetOdasiOlusturFragment()
            dialog.show(fragmentManager, "yenisohbetodasifragment")

        }*/


    }



}