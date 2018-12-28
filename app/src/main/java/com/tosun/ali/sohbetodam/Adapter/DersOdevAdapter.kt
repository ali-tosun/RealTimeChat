package com.tosun.ali.sohbetodam.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tosun.ali.sohbetodam.Model.Odev
import com.tosun.ali.sohbetodam.R
import kotlinx.android.synthetic.main.tek_satir_odev.view.*


class DersOdevAdapter(tumOdevler: ArrayList<Odev>, context: Context) : RecyclerView.Adapter<DersOdevAdapter.ViewHolder>() {

    var tumOdevler: ArrayList<Odev>
    var context: Context

    init {
        this.tumOdevler = tumOdevler
        this.context = context
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        var inflater = LayoutInflater.from(context)
        var view = inflater.inflate(R.layout.tek_satir_odev,p0,false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return tumOdevler.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.setData(p1)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(position: Int) {

            var oankiOdev = tumOdevler.get(position)

            odev_ismi.text = oankiOdev.odev_ismi
            odev_aciklamasi.text = oankiOdev.odev_aciklamasi.toString()
            odev_teslim_saati.text = oankiOdev.odev_teslim_saati
            odev_teslim_yeri.text = oankiOdev.odev_teslim_yeri
            odev_teslim_tarihi.text = oankiOdev.odev_teslim_tarihi



            itemView.setOnClickListener {

                if(!oankiOdev.download_url.isNullOrEmpty()){
                    var intent = Intent()
                    intent.setType(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(oankiOdev.download_url))
                    context.startActivity(intent)
                }

            }


        }



        var odev_ismi = itemView.tvOdevİsmi
        var odev_teslim_tarihi = itemView.tvTeslimTarih
        var odev_teslim_saati = itemView.tvTeslimSaat
        var odev_teslim_yeri= itemView.tvTeslimYer
        var odev_aciklamasi = itemView.tvOdevAciklama

    }

}