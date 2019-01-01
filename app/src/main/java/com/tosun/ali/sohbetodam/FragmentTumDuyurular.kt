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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tosun.ali.sohbetodam.Adapter.DersDuyuruAdapter
import com.tosun.ali.sohbetodam.Model.Duyuru
import kotlinx.android.synthetic.main.duyurular_fragment.*

class FragmentTumDuyurular : Fragment() {

    var v: View? = null
    var tumDuyurular: ArrayList<Duyuru>? = null
    var myContext:Context?=null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        v = inflater.inflate(R.layout.duyurular_fragment, container, false)
        myContext = container!!.context
        veriKaynagiOlustur()

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private fun veriKaynagiOlustur() {
        tumDuyurular = ArrayList<Duyuru>()

        var reference = FirebaseDatabase.getInstance().reference
                .child("kullanici")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("katildigi_dersler")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        for (data in p0!!.children) {


                            var sohbetOdasiİd = data.key

                            var refSohbet = FirebaseDatabase.getInstance().reference
                                    .child("sohbet_odasi")
                                    .child(sohbetOdasiİd!!)
                                    .child("duyurular")
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(p0: DataSnapshot) {

                                            for (data in p0!!.children) {

                                                var geciciDuyuru = Duyuru()


                                                geciciDuyuru.sohbet_odasi_id = data.getValue(Duyuru::class.java)!!.sohbet_odasi_id
                                                geciciDuyuru.duyuru_icerik = data.getValue(Duyuru::class.java)!!.duyuru_icerik
                                                geciciDuyuru.duyuru_baslik = data.getValue(Duyuru::class.java)!!.duyuru_baslik
                                                Log.e("ocaks", geciciDuyuru.toString())
                                                tumDuyurular!!.add(geciciDuyuru)

                                                duyuruAdapterOlustur()


                                            }

                                        }

                                        override fun onCancelled(p0: DatabaseError) {
                                        }

                                    })


                        }


                    }

                })


    }

    private fun duyuruAdapterOlustur() {

        //compareto metodu ile zamana göre sıralancak
        var odevAdapter = DersDuyuruAdapter(tumDuyurular!!,myContext!!)
        Log.e("ocaks1", tumDuyurular.toString())
        rvFragmentContact.adapter = odevAdapter
        rvFragmentContact.layoutManager = LinearLayoutManager(myContext!!, LinearLayout.VERTICAL, false)
    }


}