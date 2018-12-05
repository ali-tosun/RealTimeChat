package com.tosun.ali.sohbetodam

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tosun.ali.sohbetodam.Model.Kullanici
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        btnKayitOl.setOnClickListener {

            if (etMail.text.isNullOrEmpty() && etParola.text.isNullOrEmpty() && etParolaTekrar.text.isNullOrEmpty()) {

                Toast.makeText(this, "Boş alanları doldurunuz.", Toast.LENGTH_SHORT).show()


            } else {

                if (etParola.text.toString().equals(etParolaTekrar.text.toString())) {

                    yeniUyeKayit(etMail.text.toString(), etParola.text.toString())

                } else {
                    Toast.makeText(this, "Parolalar uyuşmuyor.", Toast.LENGTH_SHORT).show()
                }

            }


        }


    }

    private fun yeniUyeKayit(mail: String, parola: String) {

        progressBarGoster()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, parola)
                .addOnCompleteListener {

                    if (it.isSuccessful) {
                        Toast.makeText(this, "Hoşgeldin  " + FirebaseAuth.getInstance().currentUser?.email, Toast.LENGTH_SHORT).show()
                        onayMailiGonder()

                        var veriTabanınaEklenecekKullanici = Kullanici()

                        veriTabanınaEklenecekKullanici.isim = mail.substring(0, mail.indexOf("@"))
                        // a.tosun.420@gmail.com ->> a.tosun.420
                        veriTabanınaEklenecekKullanici.kullanici_id = FirebaseAuth.getInstance().currentUser!!.uid
                        veriTabanınaEklenecekKullanici.profil_resim =""
                        veriTabanınaEklenecekKullanici.seviye = (0 until 5).random().toString()
                        veriTabanınaEklenecekKullanici.telefon = (0 until 9).random().toString() + (0 until 9).random().toString()


                        FirebaseDatabase.getInstance().reference
                                .child("kullanici")
                                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .setValue(veriTabanınaEklenecekKullanici)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {

                                        FirebaseAuth.getInstance().signOut()
                                        loginSayfasinaYonlendir()


                                    }
                                }

                    } else {
                        Toast.makeText(this, "Bir hata oluştu" + it.exception?.message, Toast.LENGTH_SHORT).show()
                    }

                }

        progressBarGizle()


    }

    private fun onayMailiGonder() {

        var kullanici = FirebaseAuth.getInstance().currentUser

        if (kullanici != null) {

            kullanici.sendEmailVerification()
                    .addOnCompleteListener {

                        if (it.isSuccessful) {
                            Toast.makeText(this, "Onaylama maili , mail kutunuza gönderildi.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Mail gönderiminde bir hata oluştu" + it.exception?.message, Toast.LENGTH_SHORT).show()
                        }

                    }

        } else {
            //kullanıcı yok..
        }


    }

    private fun progressBarGoster() {
        prgBarÜyeKayit.visibility = View.VISIBLE
    }

    private fun progressBarGizle() {
        prgBarÜyeKayit.visibility = View.INVISIBLE
    }

    fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start

    fun loginSayfasinaYonlendir() {

        var intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }

}
