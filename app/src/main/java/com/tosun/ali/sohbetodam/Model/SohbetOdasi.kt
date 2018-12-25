package com.tosun.ali.sohbetodam.Model

import com.tosun.ali.sohbetodam.Model.SohbetMesaj

data class SohbetOdasi(var olusturan_id:String=""
                       ,var seviye:String=""
                       ,var sohbetodasi_adi:String=""
                       ,var sohbetodasi_id:String=""
                       ,var sohbet_odasi_mesajlari:List<SohbetMesaj>? = null
                       ,var parola:String=""


                       )
