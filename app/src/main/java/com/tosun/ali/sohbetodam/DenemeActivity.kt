package com.tosun.ali.sohbetodam

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_deneme.*

class DenemeActivity : AppCompatActivity() {


    var adapter: ViewPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deneme)


        adapter = ViewPagerAdapter(supportFragmentManager)


        adapter!!.AddFragment(FragmentTumDersler(), "Call")
        adapter!!.AddFragment(FragmentTumDuyurular(), "Contact")






        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_assignment_ind_black_24dp)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_content_cut_black_24dp)


        var actionBar = supportActionBar

       actionBar!!.elevation = 0F



    }
}
