package com.tosun.ali.sohbetodam.Services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {


        var refreshedToken:String? = FirebaseInstanceId.getInstance().token
        // token firebase tarafından yenilenirse yeni token değerini veri tabanına kaydet.
        tokeniVeriTabanınaKaydet(refreshedToken)


        super.onTokenRefresh()
    }

    private fun tokeniVeriTabanınaKaydet(refreshedToken:String?) {


        var kullanici = FirebaseAuth.getInstance().currentUser


        if(kullanici != null){


        FirebaseDatabase.getInstance().reference
                .child("kullanici")
                .child(kullanici.uid)
                .child("mesaj_token")
                .setValue(refreshedToken)

        }

    }


}