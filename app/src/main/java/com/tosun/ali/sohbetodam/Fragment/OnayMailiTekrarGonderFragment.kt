package com.tosun.ali.sohbetodam.Fragment


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tosun.ali.sohbetodam.R
import kotlinx.android.synthetic.main.fragment_onay_maili_tekrar_gonder.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class OnayMailiTekrarGonderFragment : DialogFragment() {

    lateinit var myContext: FragmentActivity

    lateinit var gelenMail: EditText
    lateinit var gelenParola: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        var view = inflater.inflate(R.layout.fragment_onay_maili_tekrar_gonder, container, false)

        gelenMail = view.findViewById(R.id.etDialogMail)
        gelenParola = view.findViewById(R.id.etDialogParola)
        var btnGonder = view.btnDialogGonder
        var btnİptal = view.btnDialogİptal
        myContext = activity!!




        btnİptal.setOnClickListener {

            dialog.dismiss()

        }

        btnGonder.setOnClickListener {

            if (gelenMail.text.toString().isNullOrEmpty() && gelenParola.text.toString().isNullOrEmpty()) {
                Toast.makeText(myContext, "Boş alanları doldurun", Toast.LENGTH_SHORT).show()

            } else {
                girisYapVeOnayMailiniTekrarGonder(gelenMail.text.toString(), gelenParola.text.toString())
            }

        }






        return view
    }

    private fun girisYapVeOnayMailiniTekrarGonder(gelenMail: String, gelenParola: String) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(gelenMail, gelenParola)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // kullanıcı sisteme giriş yaptı.Ve login activityde bulunan myAuthStateListener interface i itetiklendi.
                        onayMailiGonder()
                    } else {
                        Toast.makeText(myContext, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }


                }


    }

    private fun onayMailiGonder() {

        var kullanici = FirebaseAuth.getInstance().currentUser

        if (kullanici != null) {

            kullanici.sendEmailVerification()
                    .addOnCompleteListener {

                        if (it.isSuccessful) {
                            Toast.makeText(myContext, "Onay Maili Gönderildi.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(myContext, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }

                    }

        }


    }


}
