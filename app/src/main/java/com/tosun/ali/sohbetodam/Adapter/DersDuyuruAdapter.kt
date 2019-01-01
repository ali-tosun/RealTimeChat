package com.tosun.ali.sohbetodam.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tosun.ali.sohbetodam.Model.Duyuru
import com.tosun.ali.sohbetodam.R
import kotlinx.android.synthetic.main.tek_satir_duyuru.view.*

class DersDuyuruAdapter(tumDuyurular:ArrayList<Duyuru>,context:Context):RecyclerView.Adapter<DersDuyuruAdapter.ViewHolder>() {

    var tumDuyurular:ArrayList<Duyuru>
    var context:Context

    init {
        this.tumDuyurular = tumDuyurular
        this.context = context

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        var inflater = LayoutInflater.from(context)
        var view = inflater.inflate(R.layout.tek_satir_duyuru,p0,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {

        return tumDuyurular.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.setData(p1)

    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun setData(position: Int) {
            tvDuyuruBaslik.text = tumDuyurular.get(position).duyuru_baslik
            tvDuyuruİcerik.text = tumDuyurular.get(position).duyuru_icerik

        }

        var tvDuyuruBaslik = itemView.tvDuyuruBaslik
        var tvDuyuruİcerik = itemView.tvDuyuruİcerik

    }
}