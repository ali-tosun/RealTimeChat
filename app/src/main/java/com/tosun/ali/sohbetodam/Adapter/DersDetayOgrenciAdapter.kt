package com.tosun.ali.sohbetodam.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tosun.ali.sohbetodam.Model.Dokuman
import com.tosun.ali.sohbetodam.R
import kotlinx.android.synthetic.main.tek_satir_ders_detaylari.view.*

class DersDetayOgrenciAdapter(tumDokumanlar:ArrayList<Dokuman>,context: Context) : RecyclerView.Adapter<DersDetayOgrenciAdapter.ViewHolder>() {


    lateinit var tumDokumanlar: ArrayList<Dokuman>
    lateinit var context: Context

    init {
        this.tumDokumanlar = tumDokumanlar
        this.context = context
    }

    override fun getItemCount(): Int {

        return  tumDokumanlar.size

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        var inflater = LayoutInflater.from(p0.context)
        var view = inflater.inflate(R.layout.tek_satir_ders_detaylari, p0, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.setData(p1)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        fun setData(position: Int) {

            itemView.setOnClickListener {

                var intent = Intent()
                intent.setType(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(tumDokumanlar.get(position).download_link))
                context.startActivity(intent)



            }


            dersİsmi.text = tumDokumanlar.get(position).olusturulma_zamani

        }

        var dersİsmi: TextView

        init {
            var view = itemView as CardView
            dersİsmi = view.tvDersİsmi

        }



    }


}