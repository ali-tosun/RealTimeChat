package com.tosun.ali.sohbetodam.Fragment


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tosun.ali.sohbetodam.R
import kotlinx.android.synthetic.main.fragment_sifremi_unuttum.view.*

class SifremiUnuttumFragment : DialogFragment() {




    lateinit var myContext:FragmentActivity
    lateinit var myEmail : EditText


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_sifremi_unuttum, container, false)

        var btnGonder = view.btnDialogSifremiUnuttumGonder
        var btnİptal  = view.btnDialogSifremiUnuttumİptal
        myEmail = view.etDialogSifremiUnuttumMail
        myContext = activity!!


        btnİptal.setOnClickListener {

            dialog.dismiss()
        }

        btnGonder.setOnClickListener {

            FirebaseAuth.getInstance().sendPasswordResetEmail(myEmail.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(myContext,"Mail kutunuzu kontrol ediniz.",Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        else{
                            Toast.makeText(myContext,""+it.exception?.message,Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                    }

        }



        return view
    }


}
