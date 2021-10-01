package com.tawk.to.mars.git.view.fragment.viewpager

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tawk.to.mars.git.databinding.FragmentViewpagerBinding
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.view.fragment.viewpager.search.ListFragment

class VPFragment() :Fragment(){
    private var _binding: FragmentViewpagerBinding? = null
    var bottom_nav: BottomNavigationView? = null
    lateinit var adapter: HomePagerAdapter

     var search: ListFragment ? = null
     var settings:SettingsFragment ? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentViewpagerBinding.inflate(inflater, container, false)
        val view =_binding!!.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragments()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun selectItem(i: Int)
    {
        _binding!!.vpMain!!.currentItem = i
    }
    fun setFragments() {
        var list: ArrayList<Fragment> = ArrayList()
        if(search==null)
        {
            search = ListFragment()
            settings = SettingsFragment()
        }


        list.add(search!!)
        list.add(settings!!)

        adapter = HomePagerAdapter(requireActivity(), list)
        _binding!!.vpMain!!.adapter = adapter
        _binding!!.vpMain!!.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottom_nav!!.selectedItemId=position
            }
        }
        )

    }

}