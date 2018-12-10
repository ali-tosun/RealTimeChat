package com.tosun.ali.sohbetodam.Fragment


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tosun.ali.sohbetodam.Model.Kullanici
import com.tosun.ali.sohbetodam.Model.SohbetMesaj
import com.tosun.ali.sohbetodam.Model.SohbetOdasi
import com.tosun.ali.sohbetodam.R
import com.tosun.ali.sohbetodam.SohbetOdalariniGoruntuleActivity
import java.text.SimpleDateFormat
import java.util.*

class YeniSohbetOdasiOlusturFragment : DialogFragment() {

    lateinit var etSohbetOdasiAdi: EditText
    lateinit var seekBarSohbetOdasiSeviyesi: SeekBar
    lateinit var tvSohbetOdasiSeviyesi: TextView
    lateinit var btnYeniSohbetOdasiOlustur: Button
    var mProgress = 0
    var kullaniciSeviyesi = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_yeni_sohbet_odasi_olustur, container, false)

        etSohbetOdasiAdi = view.findViewById(R.id.etSohbetOdasıAdi)
        seekBarSohbetOdasiSeviyesi = view.findViewById(R.id.seekBarSohbetOdasiSeviye)
        tvSohbetOdasiSeviyesi = view.findViewById(R.id.tvSohbetOdasiSeviye)
        btnYeniSohbetOdasiOlustur = view.findViewById(R.id.btnYeniSohbetOdasiOlustur)







        kullaniciSeviyeBilgisiniGetir()

        seekBarSohbetOdasiSeviyesi.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mProgress = progress
                tvSohbetOdasiSeviyesi.setText(progress.toString())

            }

        })

        btnYeniSohbetOdasiOlustur.setOnClickListener {

            if (!etSohbetOdasiAdi.text.isNullOrEmpty()) {

                if (kullaniciSeviyesi >= mProgress) {

                    sohbetOdasiBilgileriniKaydet(mProgress)

                } else {
                    Toast.makeText(activity, "Şuanki seviyeniz ${kullaniciSeviyesi} ve altında oda oluşturabilirsiniz.", Toast.LENGTH_LONG).show()

                }


            } else {
                Toast.makeText(activity, "Sohbet Odası Adı boş bırakılamaz.", Toast.LENGTH_SHORT).show()
            }


        }





        return view
    }

    private fun sohbetOdasiBilgileriniKaydet(mProgress: Int) {

        if (FirebaseAuth.getInstance().currentUser != null) {

            var FirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

            var sohbetOdasiİd = FirebaseDatabaseRef.child("sohbet_odasi").push().key

            var veriTabanınaEklenilecekSohbetOdasi = SohbetOdasi()
            veriTabanınaEklenilecekSohbetOdasi.olusturan_id = FirebaseAuth.getInstance().currentUser!!.uid
            veriTabanınaEklenilecekSohbetOdasi.seviye = mProgress.toString()
            veriTabanınaEklenilecekSohbetOdasi.sohbetodasi_adi = etSohbetOdasiAdi.text.toString()
            veriTabanınaEklenilecekSohbetOdasi.sohbetodasi_id = sohbetOdasiİd!!

            FirebaseDatabaseRef.child("sohbet_odasi").child(sohbetOdasiİd).setValue(veriTabanınaEklenilecekSohbetOdasi)


            var mesajId = FirebaseDatabaseRef.child("sohbet_odasi").push().key

            var karsilamaMesaji = SohbetMesaj()
            karsilamaMesaji.mesaj = "Hoşgeldiniz Sohbet odasına"
            karsilamaMesaji.timestamp = getMesajTarihi()!!




            FirebaseDatabaseRef.child("sohbet_odasi")
                    .child(sohbetOdasiİd)
                    .child("sohbet_odasi_mesajlari")
                    .child(mesajId!!)
                    .setValue(karsilamaMesaji).addOnCompleteListener {


                        if (it.isSuccessful) {

                            (activity as SohbetOdalariniGoruntuleActivity).init()
                            dialog.dismiss()
                            Log.e("sohbet1", "dış")

                        }
                    }


        }


    }

    private fun kullaniciBilgileriniGetir(karsilamaMesaji: SohbetMesaj, myThread: Runnable) {

        var kullanici = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().reference.child("kullanici")
                .equalTo(kullanici)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        for (data in p0.children) {

                            var profil_resim = data.getValue(Kullanici::class.java)!!.profil_resim
                            var isim = data.getValue(Kullanici::class.java)!!.isim

                            karsilamaMesaji.profil_resmi = profil_resim
                            karsilamaMesaji.adi = isim
                            karsilamaMesaji.kullanici_id = kullanici
                            Log.e("sohbet1", "iç")

                        }
                    }

                })
    }

    private fun kullaniciSeviyeBilgisiniGetir() {

        if (FirebaseAuth.getInstance().currentUser != null) {

            var FirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

            var queryFirebaseDatabase = FirebaseDatabaseRef.child("kullanici").orderByKey().equalTo(FirebaseAuth.getInstance().currentUser!!.uid)

            queryFirebaseDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (kullanici in p0.children) {

                        kullaniciSeviyesi = kullanici.getValue(Kullanici::class.java)!!.seviye.toInt()
                    }


                }

            })


        }


    }

    private fun getMesajTarihi(): String? {

        var tarih = SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale("tr"))
        return tarih.format(Date())


    }


}
