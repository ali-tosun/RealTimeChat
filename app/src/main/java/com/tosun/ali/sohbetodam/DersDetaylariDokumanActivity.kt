package com.tosun.ali.sohbetodam

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.tosun.ali.sohbetodam.Model.Dokuman
import kotlinx.android.synthetic.main.activity_ders_detaylari.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DersDetaylariDokumanActivity : AppCompatActivity() {


    lateinit var storage: FirebaseStorage //dosyanın kaydedileceği yer
    lateinit var database: FirebaseDatabase // dosyanın indirme linkinin bulunacağı yer.
    var pdfUri: Uri? = null
    var progressDialog:ProgressDialog? = null

    var urls:ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ders_detaylari)

        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()


        btnSelectFile.setOnClickListener {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


                selectPdf()

            } else {

                var istencekİzinler = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

                ActivityCompat.requestPermissions(this, istencekİzinler, 9)
            }


        }


        btnUploadFile.setOnClickListener {

            if (pdfUri != null) {

                uploadFile(pdfUri!!)
            }
            else{
                Toast.makeText(this,"Dosya seçin",Toast.LENGTH_SHORT).show()
            }


        }











    }

    private fun uploadFile(pdfUri: Uri) {

        progressDialog = ProgressDialog(this)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog!!.setTitle("Uploading File")
        progressDialog!!.progress =0
        progressDialog!!.show()

        var downloadUrl: Uri
        var fileName = System.currentTimeMillis().toString()
        var storageReference = FirebaseStorage.getInstance().reference

        var sohbetOdasiİd = intent.getStringExtra("sohbetodasi_id")

        var dosyanınEklenecegiYer =storageReference.child("uploads")
                .child("sohbet_odalari")
                .child("dersler")
                .child(sohbetOdasiİd)
                .child("dokumanlar")
                .child(fileName)

        var uploadTask =dosyanınEklenecegiYer.putFile(pdfUri)

        uploadTask.addOnProgressListener {

            var currentProgress = (100*it.bytesTransferred/it.totalByteCount).toInt()
            progressDialog!!.progress = currentProgress


        }

        var urlTask = uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>>{
            override fun then(p0: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                if (!p0.isSuccessful) {
                    throw p0.exception!!
                }
                return dosyanınEklenecegiYer.downloadUrl

            }

        }).addOnCompleteListener {
            if (it.isSuccessful) {

                var sohbetOdasiİd = intent.getStringExtra("sohbetodasi_id")
                downloadUrl = it.result
                var yuklenmeZamani = getOdevTarih()
                var yukleyen_id =FirebaseAuth.getInstance().currentUser!!.uid

               var referans= FirebaseDatabase.getInstance().reference
                        .child("sohbet_odasi")
                        .child(sohbetOdasiİd)
                        .child("dokumanlar")

                var yeniOdevId = referans.push().key

                var geciciDokuman = Dokuman()
                geciciDokuman.dokuman_adi = etDokumanAdi.text.toString()
                geciciDokuman.yukleyen_kisi = yukleyen_id
                geciciDokuman.download_link = downloadUrl.toString()
                geciciDokuman.olusturulma_zamani=yuklenmeZamani



                        referans.child(yeniOdevId!!)
                                .setValue(geciciDokuman)


                Log.e("indirmelink",downloadUrl.toString())

            } else {
                Toast.makeText(this@DersDetaylariDokumanActivity, it.exception!!.message, Toast.LENGTH_LONG).show()
            }
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf()
        } else {
            Toast.makeText(this, "Dosya erişim izni verin", Toast.LENGTH_SHORT).show()
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun selectPdf() {


        var intent = Intent()
        intent.setType("application/*") //application/* dene
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, 86)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 86 && resultCode == Activity.RESULT_OK && data != null) {
            pdfUri = data.data //return the uri of selected file.
            tvFileStatus.text ="Dosya seçildi: "+data.data.lastPathSegment

        } else {
            Toast.makeText(this, "Dosya seçimini yapın lütfen.", Toast.LENGTH_SHORT).show()

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getOdevTarih(): String {
        var myFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("tr"))
        return myFormat.format(Date())

    }


}
