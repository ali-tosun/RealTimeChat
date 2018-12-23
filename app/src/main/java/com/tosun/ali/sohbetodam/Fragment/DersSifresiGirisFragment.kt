package com.tosun.ali.sohbetodam.Fragment


import android.os.Bundle
import android.support.v4.app.DialogFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tosun.ali.sohbetodam.Adapter.TumSohbetOdalarıAdapter

import com.tosun.ali.sohbetodam.R
import kotlinx.android.synthetic.main.fragment_ders_sifresi_giris.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DersSifresiGirisFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_ders_sifresi_giris, container, false)

        var dersParola = view.etDersParola.text.toString()


        FirebaseDatabase.getInstance().reference
                .child("kullanici")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("katildigi_dersler")
                .child(TumSohbetOdalarıAdapter.sohbet_odasi_id)
                .child("parola")
                .setValue(dersParola)


        return view
    }


}
