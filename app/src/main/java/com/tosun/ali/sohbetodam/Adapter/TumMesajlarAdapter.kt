package com.tosun.ali.sohbetodam.Adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.tosun.ali.sohbetodam.Model.SohbetMesaj
import com.tosun.ali.sohbetodam.R
import kotlinx.android.synthetic.main.tek_satir_mesaj_layout.view.*
import java.util.*


class TumMesajlarAdapter(context: Context, tumMesajlar: ArrayList<SohbetMesaj>) : RecyclerView.Adapter<TumMesajlarAdapter.SohbetMesajlarViewHolder>() {

    var myContext = context
    var tumSohbetMesajlari = tumMesajlar
    init{

        var myHashMap: HashMap<String, SohbetMesaj> = HashMap<String, SohbetMesaj>()

        for (eleman in tumMesajlar!!) {

            myHashMap.put(eleman.timestamp.toString(), eleman)

        }

        var myMap: TreeMap<String, SohbetMesaj> = TreeMap<String, SohbetMesaj>(myHashMap)
        printMap(myMap)

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SohbetMesajlarViewHolder {

        var inflater = LayoutInflater.from(myContext)

        var  view:View? = null

        if(p1 == 2){
            view = inflater.inflate(R.layout.tek_satir_mesaj_layout2, p0, false)

        }
        else{
            view = inflater.inflate(R.layout.tek_satir_mesaj_layout, p0, false)
        }



        return SohbetMesajlarViewHolder(view!!)


    }

     override fun getItemViewType(position: Int): Int {

         //mesajı biz yolladıysak..
         if(tumSohbetMesajlari.get(position).kullanici_id.equals(FirebaseAuth.getInstance().currentUser!!.uid)){
             return 2
         }
         else{
             return 1
         }


    }

    override fun onBindViewHolder(p0: SohbetMesajlarViewHolder, p1: Int) {

        var oAnkiMesaj = tumSohbetMesajlari.get(p1)
        p0.setData(oAnkiMesaj, p1)

    }

    override fun getItemCount(): Int {

        return tumSohbetMesajlari.size
    }


    inner class SohbetMesajlarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tekSatirLayout = itemView as ConstraintLayout

        var profilResmi = tekSatirLayout.imgMesajProfilResim
        var mesaj = tekSatirLayout.tvMesaj
        var yazar = tekSatirLayout.tvMesajUserAd
        var mesajTarih = tekSatirLayout.tvMesajTarih


        fun setData(oAnkiMesaj: SohbetMesaj, position: Int) {

            mesaj.text = oAnkiMesaj.mesaj
            mesajTarih.text = oAnkiMesaj.timestamp



            //bi kullanıcı mesajı ise...
            //kullanıcı profil fotoğrafı koymaz ise sohbet odasında yazar ismi gözükmez.bu problemin çözülmesi lazım!.
            if (!oAnkiMesaj.profil_resmi.isNullOrEmpty()) {
                Picasso.get().load(oAnkiMesaj.profil_resmi).into(profilResmi)
                yazar.text = oAnkiMesaj.adi

            }


        }

    }


    private fun printMap(map: TreeMap<String, SohbetMesaj>) {


        var s = map.entries
        var it = s.iterator()

        while (it.hasNext()) {

            var entry = it.next()
            var key = entry.key
            var value = entry.value
            Log.e("hashdeneme", "key $key")
            //  Log.e("hashdeneme", "value $value")

        }


    }
}