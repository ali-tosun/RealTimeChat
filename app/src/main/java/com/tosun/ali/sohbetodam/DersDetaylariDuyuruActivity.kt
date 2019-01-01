package com.tosun.ali.sohbetodam

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tosun.ali.sohbetodam.Model.Duyuru
import kotlinx.android.synthetic.main.activity_ders_detaylari_duyuru.*

class DersDetaylariDuyuruActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ders_detaylari_duyuru)

        btnDuyuruGonder.setOnClickListener {
            duyuruyuGonder()
            duyuruyuKullanıcılaraEkle()
        }
    }

    private fun duyuruyuGonder() {
        var sohbetOdasiİd = intent.getStringExtra("sohbetodasi_id")

        var referans= FirebaseDatabase.getInstance().reference
                .child("sohbet_odasi")
                .child(sohbetOdasiİd)
                .child("duyurular")


        var geciciDuyuru = Duyuru()
        geciciDuyuru.duyuru_baslik = etDuyuruBasligi.text.toString()
        geciciDuyuru.duyuru_icerik = etDuyuruMetni.text.toString()
        geciciDuyuru.sohbet_odasi_id = sohbetOdasiİd


        var yeniDuyuruId = referans.push().key

        referans.child(yeniDuyuruId!!).
                setValue(geciciDuyuru).addOnSuccessListener {
            Toast.makeText(this,"Duyuru Paylaşıldı", Toast.LENGTH_SHORT).show()
        }







    }

    private fun duyuruyuKullanıcılaraEkle() {
        var sohbetOdasiİd = intent.getStringExtra("sohbetodasi_id")

        var ref = FirebaseDatabase.getInstance().reference
                .child("sohbet_odasi")
                .child(sohbetOdasiİd)
                .child("sohbet_odasi_kayitli_kisiler")
                .orderByKey()
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        for (data in p0!!.children){



                            var kullaniciİd = data.key
                            Log.e("ocak1",""+kullaniciİd)

                            var refKullanici = FirebaseDatabase.getInstance().reference
                                    .child("kullanici")
                                    .child(kullaniciİd!!)
                                    .child("duyurular")



                            var yeniDuyuruId = refKullanici.push().key

                            var geciciDuyuru = Duyuru()
                            geciciDuyuru.duyuru_baslik = etDuyuruBasligi.text.toString()
                            geciciDuyuru.duyuru_icerik = etDuyuruMetni.text.toString()
                            geciciDuyuru.sohbet_odasi_id =sohbetOdasiİd


                            refKullanici.child(yeniDuyuruId!!).setValue(geciciDuyuru)


                        }

                    }

                })

    }
}
