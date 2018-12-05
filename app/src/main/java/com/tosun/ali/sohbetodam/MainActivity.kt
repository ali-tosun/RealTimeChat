package com.tosun.ali.sohbetodam

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.tosun.ali.sohbetodam.Model.Kullanici
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var myAuthStateListener: FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMyAuthStateListener()
        initFCM()
    }

    private fun initFCM() {

        var token = FirebaseInstanceId.getInstance().token
        tokeniVeriTabanınaKaydet(token)

    }



    private fun tokeniVeriTabanınaKaydet(token:String?) {

        FirebaseDatabase.getInstance().reference
                .child("kullanici")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("mesaj_token")
                .setValue(token)

    }


    private fun setKullaniciBilgileri() {

        var kullanici = FirebaseAuth.getInstance().currentUser

        if (kullanici != null) {
            tvKullaniciİsmi.text = if (kullanici.displayName.isNullOrEmpty()) "Tanımlanmadı" else kullanici.displayName.toString()
            tvKullaniciMail.text = kullanici.email
            tvKullaniciUUİD.text = kullanici.uid

        }


    }

    private fun initMyAuthStateListener() {
        myAuthStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {

                if (p0.currentUser == null) {
                    var intent = Intent(this@MainActivity, LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()

                } else {

                }

            }

        }


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        var menuId = item?.itemId

        when (menuId) {
            R.id.menuCikis -> {
                FirebaseAuth.getInstance().signOut()
            }

            R.id.menuAyarYap -> {

                var intent = Intent(this, KullaniciAyarlariActivity::class.java)
                startActivity(intent)

            }
            R.id.menuSohbet -> {
                var intent = Intent(this, SohbetActivity::class.java)
                startActivity(intent)

            }


        }



        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.anamenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStop() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(myAuthStateListener)
        }
        super.onStop()
    }

    override fun onStart() {
        FirebaseAuth.getInstance().addAuthStateListener(myAuthStateListener)
        super.onStart()
    }

    override fun onResume() {
        kullaniciyiKontrolEt()
        setKullaniciBilgileri()
        super.onResume()
    }

    private fun kullaniciyiKontrolEt() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            var intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()

        }
    }

}
