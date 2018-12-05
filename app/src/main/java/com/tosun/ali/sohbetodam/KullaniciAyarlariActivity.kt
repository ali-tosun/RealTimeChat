package com.tosun.ali.sohbetodam

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.tosun.ali.sohbetodam.Fragment.KullaniciProfilResmiFragment
import com.tosun.ali.sohbetodam.Model.Kullanici
import kotlinx.android.synthetic.main.activity_kullanici_ayarlari.*
import java.io.ByteArrayOutputStream


class KullaniciAyarlariActivity : AppCompatActivity(), KullaniciProfilResmiFragment.onPhotoStateListener {

    var izinVarMi = false

    var galeridenGelenFoto: Uri? = null
    var kameradanGelenFoto: Bitmap? = null

    override fun getİmagePath(imagePath: Uri) {

        galeridenGelenFoto = imagePath
        imgKullaniciProfilResmi.setImageURI(galeridenGelenFoto)


    }


    override fun getİmageBitmap(bitmap: Bitmap) {

        kameradanGelenFoto = bitmap
        imgKullaniciProfilResmi.setImageBitmap(kameradanGelenFoto)

    }


    lateinit var kullanici: FirebaseUser
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        kullanici = FirebaseAuth.getInstance().currentUser!!
        setContentView(R.layout.activity_kullanici_ayarlari)
        setKullaniciBilgileri()








        btnDegisiklikleriKaydet.setOnClickListener {


            if (!etKullaniciİsmi.text.isNullOrEmpty()) {


                if (!etKullaniciİsmi.text.toString().equals(kullanici.displayName)) {

                    var bilgileriGuncelle = UserProfileChangeRequest.Builder()
                            .setDisplayName(etKullaniciİsmi.text.toString())
                            .build()

                    kullanici.updateProfile(bilgileriGuncelle)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {

                                    FirebaseDatabase.getInstance().reference
                                            .child("kullanici")
                                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("isim")
                                            .setValue(etKullaniciİsmi.text.toString())
                                            .addOnCompleteListener {

                                            }


                                }
                            }

                }

            }


            if (!etKullaniciTelefon.text.toString().isNullOrEmpty()) {


                FirebaseDatabase.getInstance().reference
                        .child("kullanici")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("telefon")
                        .setValue(etKullaniciTelefon.text.toString())
                        .addOnCompleteListener {


                        }
            }




            if (kameradanGelenFoto != null) {

                fotografCompressed(kameradanGelenFoto)

            } else if (galeridenGelenFoto != null) {

                var myBitmap = MediaStore.Images.Media.getBitmap(this@KullaniciAyarlariActivity.contentResolver, galeridenGelenFoto)
                fotografCompressed(myBitmap)

            }


        }

        btnParolaVeyaMailDegistir.setOnClickListener {


            if (!etKullaniciParola.text.toString().isNullOrEmpty()) {


                var credential = EmailAuthProvider.getCredential(kullanici.email.toString(), etKullaniciParola.text.toString())




                kullanici.reauthenticate(credential)
                        .addOnCompleteListener {

                            if (it.isSuccessful) {
                                csLayoutKullanıcıMailVeyeParolaDegistir.visibility = View.VISIBLE

                                btnMailDegistir.setOnClickListener {

                                    mailAdresiniDegistir()

                                }

                                btnParolaDegistir.setOnClickListener {

                                    parolaBilgisiniDegistir()


                                }


                            } else {
                                csLayoutKullanıcıMailVeyeParolaDegistir.visibility = View.INVISIBLE
                                Toast.makeText(this, "" + it.exception?.message, Toast.LENGTH_SHORT).show()

                            }


                        }

            } else {
                Toast.makeText(this, "Parola alanı boş bırakılamaz", Toast.LENGTH_SHORT).show()
            }


        }


        imgKullaniciProfilResmi.setOnClickListener {


            if (izinVarMi) {
                var dialog = KullaniciProfilResmiFragment()
                dialog.show(supportFragmentManager, "profil")
            } else {
                kameraVeGaleriİzinleriniİste()
            }

        }


    }

    private fun fotografCompressed(kullanicidanGelenBitmap: Bitmap?) {

        var compresserd = BackGroundİmageCompress()
        compresserd.execute(kullanicidanGelenBitmap)

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun kameraVeGaleriİzinleriniİste() {

        var izinler = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA)

        //tüm izinler verildi ise
        if (ContextCompat.checkSelfPermission(this, izinler[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, izinler[1]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, izinler[2]) == PackageManager.PERMISSION_GRANTED) {
            izinVarMi = true

        } else {
            //izinler verilmediyse git izin iste.

            requestPermissions(izinler, 231)
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // ilk defa izin istenme durumu.
        if (requestCode == 231 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED) {
            var dialog = KullaniciProfilResmiFragment()
            dialog.show(supportFragmentManager, "profil")
        } else {
            Toast.makeText(this, "İzinleri veriniz lütfen", Toast.LENGTH_LONG).show()
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun loginSayfasinaYonlendir() {

        var intent = Intent(this@KullaniciAyarlariActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }

    private fun parolaBilgisiniDegistir() {
        var kullanici = FirebaseAuth.getInstance().currentUser

        if (kullanici != null) {

            kullanici.updatePassword(etYeniParola.text.toString())
                    .addOnCompleteListener {

                        if (it.isSuccessful) {
                            Toast.makeText(this, "Parola bilgisi değiştirildi", Toast.LENGTH_SHORT).show()
                            FirebaseAuth.getInstance().signOut()
                            loginSayfasinaYonlendir()

                        } else {
                            Toast.makeText(this, "" + it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
        }

    }

    private fun mailAdresiniDegistir() {
        var kullanici = FirebaseAuth.getInstance().currentUser




        if (kullanici != null) {

            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(etYeniMail.text.toString())
                    .addOnCompleteListener {

                        if (it.isSuccessful) {

                            if (it.result.signInMethods?.size == 1) {

                                Toast.makeText(this, "Bu mail adresi kullanımdadır.", Toast.LENGTH_SHORT).show()

                            } else {
                                kullanici.updateEmail(etYeniMail.text.toString())
                                        .addOnCompleteListener {

                                            if (it.isSuccessful) {
                                                Toast.makeText(this, "Mail adresi değiştirildi", Toast.LENGTH_SHORT).show()
                                                FirebaseAuth.getInstance().signOut()
                                                loginSayfasinaYonlendir()


                                            } else {
                                                Toast.makeText(this, "" + it.exception?.message, Toast.LENGTH_SHORT).show()
                                            }
                                        }
                            }

                        }


                    }


        }


    }

    private fun setKullaniciBilgileri() {

        var kullanici = FirebaseAuth.getInstance().currentUser


        var reference = FirebaseDatabase.getInstance().reference

        var sorgu = reference.child("kullanici")
                .orderByKey()
                .equalTo(kullanici!!.uid)


        sorgu.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                for (singleSnapShot in p0.children) {

                    var okunanKullanici = singleSnapShot.getValue(Kullanici::class.java)
                    etKullaniciİsmi.setText(okunanKullanici!!.isim.toString())
                    etKullaniciTelefon.setText(okunanKullanici!!.telefon.toString())

                    if(!okunanKullanici.profil_resim.isNullOrEmpty()){
                        Picasso.get().load(okunanKullanici.profil_resim).resize(100,100).into(imgKullaniciProfilResmi)
                    }



                }


            }

        })


    }

    inner class BackGroundİmageCompress : AsyncTask<Bitmap?, Void, ByteArray>() {

        override fun onPreExecute() {


            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Bitmap?): ByteArray {


            var myBitmap: Bitmap = params[0]!!

            Log.e("TOSUN", myBitmap!!.byteCount.toString())
            var myByteArray: ByteArray? = null

            for (i in 1..5) {
                myByteArray = imageCompressed(myBitmap, 100 / i)
            }

            return myByteArray!!
        }

        private fun imageCompressed(myBitmap: Bitmap, i: Int): ByteArray? {

            var stream = ByteArrayOutputStream()

            myBitmap.compress(Bitmap.CompressFormat.JPEG, i, stream)

            return stream.toByteArray()


        }


        override fun onPostExecute(result: ByteArray?) {

            uploadResimToFireBase(result)
            super.onPostExecute(result)
        }


    }


    private fun uploadResimToFireBase(result: ByteArray?) {

        var fireBaseDataBaseReference = FirebaseStorage.getInstance().reference

        var fotografinEklenilecegiYer = fireBaseDataBaseReference.child("images/users/" + FirebaseAuth.getInstance().currentUser!!.uid + "/profile_resim")


        var uploadTask = fotografinEklenilecegiYer.putBytes(result!!)
        var downloadUrl: Uri


        var urlTask = uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
            override fun then(p0: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                if (!p0.isSuccessful) {
                    throw p0.exception!!
                }
                return fotografinEklenilecegiYer.downloadUrl
            }

        }).addOnCompleteListener {

            if (it.isSuccessful) {

                downloadUrl = it.result

                FirebaseDatabase.getInstance().reference
                        .child("kullanici")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("profil_resim")
                        .setValue(downloadUrl.toString())

            } else {
                Toast.makeText(this@KullaniciAyarlariActivity, it.exception!!.message, Toast.LENGTH_LONG).show()
            }

        }


    }

}

/*
Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
    @Override
    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
        if (!task.isSuccessful()) {
            throw task.getException();
        }

        // Continue with the task to get the download URL
        return ref.getDownloadUrl();
    }
}).addOnCompleteListener(new OnCompleteListener<Uri>() {
    @Override
    public void onComplete(@NonNull Task<Uri> task) {
        if (task.isSuccessful()) {
            Uri downloadUri = task.getResult();
        } else {
            // Handle failures
            // ...
        }
    }
});
 */



