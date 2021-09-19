package com.tawk.to.mars.git.view.fragment.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity)
{
    var list:ArrayList<Fragment> ?=null


    constructor(fragmentActivity: FragmentActivity,list:ArrayList<Fragment>):this(fragmentActivity)
    {
        this.list=list
    }

    override fun getItemCount(): Int {
        return this.list!!.size
    }

    override fun createFragment(position: Int): Fragment {
       return this.list!!.get(position)
    }
}