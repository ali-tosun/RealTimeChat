package com.tosun.ali.sohbetodam.Adapter

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
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
import com.squareup.picasso.Picasso
import com.tosun.ali.sohbetodam.Fragment.DersSifresiGirisFragment
import com.tosun.ali.sohbetodam.Fragment.YeniSohbetOdasiOlusturFragment
import com.tosun.ali.sohbetodam.Model.Kullanici
import com.tosun.ali.sohbetodam.Model.SohbetOdasi
import com.tosun.ali.sohbetodam.R
import com.tosun.ali.sohbetodam.SohbetOdalariniGoruntuleActivity
import com.tosun.ali.sohbetodam.SohmetMesajlariActivity
import kotlinx.android.synthetic.main.tek_satir_sohbet_odasi_layout.view.*

class TumSohbetOdalarıAdapter(mActivity: Activity, tumSohbetOdalari: ArrayList<SohbetOdasi>) : RecyclerView.Adapter<TumSohbetOdalarıAdapter.SohbetOdasiViewHolder>() {


    companion object {
        var sohbet_odasi_id: String = ""
    }


    var tumSohbetOdalari: ArrayList<SohbetOdasi>
    var mySohbetOdasiActivity: Activity

    init {
        this.tumSohbetOdalari = tumSohbetOdalari
        mySohbetOdasiActivity = mActivity
    }


    override fun getItemCount(): Int {
        val size = tumSohbetOdalari.size

        return size

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SohbetOdasiViewHolder {

        var inflater = LayoutInflater.from(p0.context)

        var tekSatirSohbetOdasi = inflater.inflate(R.layout.tek_satir_sohbet_odasi_layout, p0, false)

        return SohbetOdasiViewHolder(tekSatirSohbetOdasi)

    }

    inner class SohbetOdasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var tıklandıMı: Boolean = false

        var myActivity = (mySohbetOdasiActivity as SohbetOdalariniGoruntuleActivity)


        var btnKayitOl: Button = itemView.btnDersKayitOl
        var etParola: EditText = itemView.etSohbetParola
        var imgSohbetOdasiOlusturanProfilResmi: ImageView = itemView.imgSohbetOdasiOlusturanProfilResmi
        var tvSohbetOdasiAdi: TextView = itemView.tvSohbetOdasiAdi
        var imgSohbetOdasiSil: ImageView = itemView.imgSohbetOdasiSil
        var tvSohbetOdasiOlusturanKisiAdi: TextView = itemView.tvSohbetOdasiOlusturanKisiAdi
        var tvSohbetOdasiMesajSayisi: TextView = itemView.tvSohbetOdasiMesajSayisi


        fun setData(oAnkiSohbetOdasi: SohbetOdasi, position: Int) {

            kullaniciBilgileriniGetir(oAnkiSohbetOdasi!!)


            //sohbet odasına tıklandığı zaman gerçekleşecek olaylar.
            itemView.setOnClickListener {


                kullaniciyiSohbetOdasinaKaydet(oAnkiSohbetOdasi)

                /*  if (dersSifresi.equals("")) {
                      kullaniciParolasınıKaydet(oAnkiSohbetOdasi)
                  }*/

                var odaninParolasi = sohbetOdasiParolasiniGetir(oAnkiSohbetOdasi)
                Log.e("odaparola", odaninParolasi)


                var ref = FirebaseDatabase.getInstance().reference
                        .child("kullanici")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("katildigi_dersler")
                        .child(oAnkiSohbetOdasi.sohbetodasi_id)
                        .orderByKey()

                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                var myParola: String = ""

                                Log.e("p0", p0.toString())
                                //derse kayıt olmadıysa
                                if (p0.getValue() != null) {

                                    var singleDataSnapShot = p0.children.iterator().next()
                                    myParola = singleDataSnapShot.getValue().toString()
                                    Log.e("p0", "my parola $myParola")

                                    //parolası başarılı ise.
                                    if (myParola.equals(odaninParolasi)) {
                                        var intent = Intent(itemView.context, SohmetMesajlariActivity::class.java)

                                        intent.putExtra("sohbetodasi_id", oAnkiSohbetOdasi.sohbetodasi_id)

                                        myActivity.startActivity(intent)
                                    } else {
                                        Toast.makeText(itemView.context, "şifre yanlış", Toast.LENGTH_SHORT).show()


                                    }
                                    Log.e("p0", p0.toString())
                                } else {
                                    Toast.makeText(itemView.context, "derse kayıt olunuz", Toast.LENGTH_SHORT).show()
                                }
                                Log.e("p0", p0.toString())


                            }

                        })


                //sohbet odasına tıklayan kişinin kullanıcı idsini ve toplam kaç mesaj okuduğunu firebase e kaydeder.


            }



            btnKayitOl.setOnClickListener {

                var dersSifresi = etParola.text.toString()

                if (!dersSifresi.isNullOrEmpty()) {


                    FirebaseDatabase.getInstance().reference
                            .child("kullanici")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .child("katildigi_dersler")
                            .child(oAnkiSohbetOdasi.sohbetodasi_id)
                            .child("parola")
                            .setValue(dersSifresi)

                }


            }


            imgSohbetOdasiSil.setOnClickListener {


                if (FirebaseAuth.getInstance().currentUser!!.uid.equals(oAnkiSohbetOdasi.olusturan_id)) {

                    var alertDialog = AlertDialog.Builder(itemView.context)

                    alertDialog.setTitle("Sohbet Odası Sil!!")
                    alertDialog.setMessage("Sohbet odası silinecektir.Emin misiniz ?")
                    alertDialog.setCancelable(true)

                    alertDialog.setPositiveButton("Evet", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {

                            (mySohbetOdasiActivity as SohbetOdalariniGoruntuleActivity).sohbetOdasiSil(oAnkiSohbetOdasi.sohbetodasi_id)

                        }

                    })

                    alertDialog.setNegativeButton("Hayır", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {

                            dialog!!.dismiss()

                        }

                    })
                    alertDialog.show()


                } else {
                    Toast.makeText(itemView.context, "Sadece odayı oluşturan kişiler silebilir", Toast.LENGTH_SHORT).show()

                }

            }


        }

        private fun kullaniciyiSohbetOdasinaKaydet(oAnkiSohbetOdasi: SohbetOdasi) {

            var ref = FirebaseDatabase.getInstance().reference
                    .child("sohbet_odasi")
                    .child(oAnkiSohbetOdasi.sohbetodasi_id)
                    .child("sohbet_odasi_kayitli_kisiler")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .child("okunan_mesaj_sayisi")
                    .setValue(oAnkiSohbetOdasi.sohbet_odasi_mesajlari!!.size.toString())

            sohbet_odasi_id = oAnkiSohbetOdasi.sohbetodasi_id


        }

        private fun sohbetOdasiParolasiniGetir(oAnkiSohbetOdasi: SohbetOdasi): String {

            Log.e("tyty", "tyty")
            var parola: String = ""
            var seviye:String = ""

            var FirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

            var FirebaseDatabaseQuery = FirebaseDatabaseRef.child("sohbet_odasi")
                    .orderByValue()
                    .equalTo(oAnkiSohbetOdasi.sohbetodasi_id)

            FirebaseDatabaseQuery
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            for (data in p0.children) {

                              var nesneMap = data.getValue() as HashMap<String, Object>

                                parola = nesneMap.get("parola").toString()
                                seviye = nesneMap.get("seviye").toString()
                                Log.e("asd","$parola asddd")

                                parola = data.child("parola").getValue().toString()

                                Log.e("asd","$parola asddd")


                            }
                        }

                    })

            Log.e("sohbetparola", "$parola asd")
            Log.e("sohbetseviye", "$seviye asd")

            return parola
        }


        private fun kullaniciBilgileriniGetir(oAnkiSohbetOdasi: SohbetOdasi) {
            FirebaseDatabase.getInstance().reference
                    .child("kullanici")
                    .orderByKey()
                    .equalTo(oAnkiSohbetOdasi.olusturan_id)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            for (data in p0.children) {

                                var profilResim = data.getValue(Kullanici::class.java)!!.profil_resim
                                var kullaniciAdi = data.getValue(Kullanici::class.java)!!.isim
                                var kullaniciİd = data.getValue(Kullanici::class.java)!!.kullanici_id

                                Log.e("sohbetBug", kullaniciAdi)


                                tvSohbetOdasiAdi.text = oAnkiSohbetOdasi.sohbetodasi_adi
                                tvSohbetOdasiMesajSayisi.text = oAnkiSohbetOdasi.sohbet_odasi_mesajlari!!.size.toString()


                                if (!kullaniciİd.equals("")) {
                                    Picasso.get().load(profilResim).into(imgSohbetOdasiOlusturanProfilResmi)
                                    tvSohbetOdasiOlusturanKisiAdi.setText(kullaniciAdi)
                                }

                            }


                        }

                    })
        }

    }


    override fun onBindViewHolder(p0: SohbetOdasiViewHolder, position: Int) {

        var oAnkiSohbetOdasi = tumSohbetOdalari.get(position)
        p0.setData(oAnkiSohbetOdasi, position)

    }


}