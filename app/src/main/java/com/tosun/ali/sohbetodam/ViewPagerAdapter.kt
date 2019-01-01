package com.tosun.ali.sohbetodam

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var lstFragment: ArrayList<Fragment> = ArrayList<Fragment>()
    var lstTitles:ArrayList<String> = ArrayList<String>()

    override fun getItem(p0: Int): Fragment {
        return lstFragment.get(p0)
    }

    override fun getCount(): Int {

        return lstFragment.count()
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return lstTitles.get(position)
    }


    public fun AddFragment(fragment: Fragment,title: String){

        lstFragment.add(fragment)
        lstTitles.add(title)
    }


}