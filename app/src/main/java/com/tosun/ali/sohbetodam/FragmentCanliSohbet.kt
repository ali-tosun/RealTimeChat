package com.tosun.ali.sohbetodam

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tosun.ali.sohbetodam.Model.Odev
import kotlinx.android.synthetic.main.canlisohbet_fragment.*
import kotlinx.android.synthetic.main.canlisohbet_fragment.view.*

class FragmentCanliSohbet: Fragment() {
    lateinit var tumOdevler: ArrayList<Odev>
    var v:View? = null
    var myContext: Context?=null
    var myActivity:Activity?=null
    var sohbetOdasiİd:String?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        v = inflater.inflate(R.layout.canlisohbet_fragment, container, false)
        myContext = container!!.context

        var activityDersDetay = activity as DersDetaylariOgrenciActivity
        myActivity = activityDersDetay.getActivity() as DersDetaylariOgrenciActivity
        sohbetOdasiİd = activityDersDetay.getSohbetOdasiİd()



        v!!.btnCanliSohbet.setOnClickListener {
            activityDersDetay.canliSohbeteGit()
        }





        return v
    }


















}