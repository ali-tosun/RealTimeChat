package com.tosun.ali.sohbetodam.Model
import java.util.Comparator;

data class SohbetMesaj(var mesaj:String="",var kullanici_id:String="",var timestamp:String="",var profil_resmi:String="",var adi:String=""){


    companion object {

        var TimeStampComparator= object:Comparator<SohbetMesaj>{
            override fun compare(o1: SohbetMesaj?, o2: SohbetMesaj?): Int {

                var t1=o1!!.timestamp
                var t2=o2!!.timestamp

                return t1.compareTo(t2)

            }


        }

    }


}
