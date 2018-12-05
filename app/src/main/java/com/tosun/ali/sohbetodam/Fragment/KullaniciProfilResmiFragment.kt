package com.tosun.ali.sohbetodam.Fragment


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tosun.ali.sohbetodam.R


class KullaniciProfilResmiFragment : DialogFragment() {

    lateinit var tvGaleridenFoto: TextView
    lateinit var tvKameradanFoto: TextView
    lateinit var myPhotoStateListener: onPhotoStateListener
    var deneme = true

    interface onPhotoStateListener {

        fun getİmagePath(imagePath: Uri)
        fun getİmageBitmap(bitmap: Bitmap)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.fragment_kullanici_profil_resmi, container, false)

        tvGaleridenFoto = view.findViewById(R.id.tvGaleridenFoto)
        tvKameradanFoto = view.findViewById(R.id.tvKameradanFoto)


        tvGaleridenFoto.setOnClickListener {

            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 100)


        }


        tvKameradanFoto.setOnClickListener {

            var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 200)

        }





        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {

            var galeridenSecilenResimYolu = data.data
            myPhotoStateListener.getİmagePath(galeridenSecilenResimYolu)
            dialog.dismiss()


        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK && data != null) {
            var kameradanSecilemResimBitmap = data.extras.get("data") as Bitmap
            myPhotoStateListener.getİmageBitmap(kameradanSecilemResimBitmap)
            dialog.dismiss()


        }






        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onAttach(context: Context?) {

        myPhotoStateListener = activity as onPhotoStateListener
        super.onAttach(context)
    }

}
