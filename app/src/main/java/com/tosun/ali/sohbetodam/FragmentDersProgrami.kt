package com.tosun.ali.sohbetodam

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dersprogrami_fragment.*
import kotlinx.android.synthetic.main.dersprogrami_fragment.view.*
import java.util.zip.Inflater

class FragmentDersProgrami: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.dersprogrami_fragment,container,false)

        view.radioGrup.setOnCheckedChangeListener { group, checkedId ->

            when(checkedId){

                R.id.rdBtnNormal -> {
                    var pdfView = view.pdfView
                    pdfView.fromAsset("namkders.pdf").load()
                }

                R.id.rdBtnİkinci ->{
                    var pdfView = view.pdfView
                    pdfView.fromAsset("namkders.pdf").load()
                }

            }

        }




        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}