package com.tosun.ali.sohbetodam

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.tosun.ali.sohbetodam.Model.Duyuru
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity() : AppCompatActivity(), Serializable {


    lateinit var myAuthStateListener: FirebaseAuth.AuthStateListener

    var adapter: ViewPagerAdapter? = null
    lateinit var tumDuyurular: ArrayList<Duyuru>

    @Transient
    var myActivity: MainActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMyAuthStateListener()
        initFCM()


        adapter = ViewPagerAdapter(supportFragmentManager)



        adapter!!.AddFragment(FragmentTumDersler(), "Tüm Dersler")
        adapter!!.AddFragment(FragmentTumDuyurular(), "Duyurular")
        adapter!!.AddFragment(FragmentDersProgrami(),"Ders Programi")






        viewPagers.adapter = adapter
        tabLayouts.setupWithViewPager(viewPagers)


        tabLayouts.getTabAt(0)!!.setIcon(R.drawable.ic_assignment_ind_black_24dp)
        tabLayouts.getTabAt(1)!!.setIcon(R.drawable.ic_content_cut_black_24dp)






    }

    fun getMainActivity(): MainActivity {

        return this@MainActivity
    }


    fun sohbetOdasiSil(sohbetOdasiİd: String) {

        var FirebaseDatabaseRef = FirebaseDatabase.getInstance().reference

        FirebaseDatabaseRef.child("sohbet_odasi").child(sohbetOdasiİd).removeValue()

        Toast.makeText(this, "Sohbet odasi silindi", Toast.LENGTH_SHORT).show()


    }

    /*  private fun veriKaynagiOlustur() {
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
                                                  tumDuyurular.add(geciciDuyuru)

                                                  duyuruAdapterOlustur()


                                              }

                                          }

                                          override fun onCancelled(p0: DatabaseError) {
                                          }

                                      })


                          }


                      }

                  })


      }*/

    /*  private fun duyuruAdapterOlustur() {

          //compareto metodu ile zamana göre sıralancak
          var odevAdapter = DersDuyuruAdapter(tumDuyurular, this)
          Log.e("ocaks1", tumDuyurular.toString())
          rvDuyurular.adapter = odevAdapter
          rvDuyurular.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
      }*/

    private fun initFCM() {

        var token = FirebaseInstanceId.getInstance().token
        tokeniVeriTabanınaKaydet(token)

    }


    private fun tokeniVeriTabanınaKaydet(token: String?) {

        FirebaseDatabase.getInstance().reference
                .child("kullanici")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("mesaj_token")
                .setValue(token)

    }


    private fun setKullaniciBilgileri() {

        var kullanici = FirebaseAuth.getInstance().currentUser
/*
        if (kullanici != null) {
            tvKullaniciİsmi.text = if (kullanici.displayName.isNullOrEmpty()) "Tanımlanmadı" else kullanici.displayName.toString()
            tvKullaniciMail.text = kullanici.email
            tvKullaniciUUİD.text = kullanici.uid

        }*/


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
          /*  R.id.menuSohbet -> {
                var intent = Intent(this, SohbetOdalariniGoruntuleActivity::class.java)
                startActivity(intent)

            }*/

            /*    R.id.fragmentDeneme -> {
                    var intent = Intent(this, DenemeActivity::class.java)
                    startActivity(intent)

                }*/


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
        //setKullaniciBilgileri()
        //veriKaynagiOlustur()
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
