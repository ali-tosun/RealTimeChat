package com.tosun.ali.sohbetodam

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_ders_detay_ekle.*

class DersDetayEkleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ders_detay_ekle)


        btnDokumanEkle.setOnClickListener {
            var sohbetOdasiİd = intent.getStringExtra("sohbetodasi_id")
            var intent = Intent(this,DersDetaylariDokumanActivity::class.java)
            intent.putExtra("sohbetodasi_id",sohbetOdasiİd)
            startActivity(intent)

        }

        btnOdevEkle.setOnClickListener {
            var sohbetOdasiİd = intent.getStringExtra("sohbetodasi_id")
            var intent = Intent(this,DersDetaylariOdevActivity::class.java)
            intent.putExtra("sohbetodasi_id",sohbetOdasiİd)
            startActivity(intent)

        }

        btnDersSayfasinaGit.setOnClickListener {
            var sohbetOdasiİd = intent.getStringExtra("sohbetodasi_id")
            var intent = Intent(this,DersDetaylariOgrenciActivity::class.java)
            intent.putExtra("sohbetodasi_id",sohbetOdasiİd)
            startActivity(intent)
        }

        btnDuyuruEkle.setOnClickListener {
            var sohbetOdasiİd = intent.getStringExtra("sohbetodasi_id")
            var intent = Intent(this,DersDetaylariDuyuruActivity::class.java)
            intent.putExtra("sohbetodasi_id",sohbetOdasiİd)
            startActivity(intent)
        }

    }
}
