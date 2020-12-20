package com.example.sosrosas.View


import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class SlidePageAdapter : FragmentStatePagerAdapter {

    private var listFragments : List<Fragment>

    constructor(fm : FragmentManager, listFragments : List<Fragment>) : super(fm) {
        this.listFragments = listFragments
    }

    override fun getItem(position: Int): Fragment {
        return listFragments.get(position)
    }

    override fun getCount(): Int {
        return listFragments.size
    }
    
    override fun saveState(): Parcelable? {
        return null
    }
}