package com.tosun.ali.sohbetodam

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tosun.ali.sohbetodam.Fragment.OnayMailiTekrarGonderFragment
import com.tosun.ali.sohbetodam.Fragment.SifremiUnuttumFragment
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initMyAuthStateListener()

        btnLoginGirisYap.setOnClickListener {

            if (etLoginEmail.text.isNullOrEmpty() and etLoginParola.text.isNullOrEmpty()) {
                Toast.makeText(this, "Gerekli alanları doldurunuz.", Toast.LENGTH_SHORT).show()
            } else {
                uyeGirisiYap(etLoginEmail.text.toString(), etLoginParola.text.toString())
            }


        }

        tvLoginKayitOl.setOnClickListener {
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        tvOnayMailiTekrarGonder.setOnClickListener {

            var dialogFragment = OnayMailiTekrarGonderFragment()

            dialogFragment.show(supportFragmentManager,"gonder")


        }

        tvSifremiUnuttum.setOnClickListener {

            var sifremiUnuttumFragment = SifremiUnuttumFragment()
            sifremiUnuttumFragment.show(supportFragmentManager,"sifre")

        }


    }

    private fun initMyAuthStateListener() {
        mAuthStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {

                var kullanici = p0.currentUser
                if (kullanici != null) {

                    if (kullanici.isEmailVerified) {
                        var intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this@LoginActivity, "E-Mail adresinizi onaylayınız.", Toast.LENGTH_SHORT).show()
                    }

                }

            }

        }


    }

    private fun uyeGirisiYap(mail: String, parola: String) {


        FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, parola)
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        if (!it.result.user.isEmailVerified) {
                            FirebaseAuth.getInstance().signOut()
                        }


                    } else {
                        Toast.makeText(this, "Bir hata oluştu. " + it.exception?.message, Toast.LENGTH_SHORT).show()
                    }

                }

    }

    override fun onStart() {

        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener)
        super.onStart()
    }

    override fun onStop() {
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener)
        super.onStop()
    }
}
