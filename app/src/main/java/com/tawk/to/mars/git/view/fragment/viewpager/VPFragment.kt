package com.tawk.to.mars.git.view.fragment.viewpager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tawk.to.mars.git.databinding.FragmentViewpagerBinding
import com.tawk.to.mars.git.view.fragment.viewpager.search.ListFragment
import com.tawk.to.mars.git.view.fragment.viewpager.settings.SettingsFragment

class VPFragment() :Fragment(){
    val TAG_LIST = "list"
    val TAG_SELECTED = "selected"
    private var _binding: FragmentViewpagerBinding? = null
    var bottom_nav: BottomNavigationView? = null
    lateinit var adapter: HomePagerAdapter

     var search: ListFragment ? = null
     var settings: SettingsFragment? = null
    var list: ArrayList<Fragment> = ArrayList()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentViewpagerBinding.inflate(inflater, container, false)
        val view =_binding!!.root
        if(savedInstanceState!=null)
        {
            this.list = savedInstanceState.getSerializable(TAG_LIST) as ArrayList<Fragment>
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragments()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(TAG_LIST,list)
        outState.putInt(TAG_SELECTED, _binding!!.vpMain!!.currentItem)
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


        if(list.size<2)
        {
            if(search==null)
            {
                search = ListFragment()
                settings = SettingsFragment()
            }
            list.add(search!!)
            list.add(settings!!)
        }

        adapter = HomePagerAdapter(requireActivity(), list)
        _binding!!.vpMain!!.adapter = adapter
        _binding!!.vpMain!!.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottom_nav!!.selectedItemId=position
            }
        }
        )
//        if(selected>-1)
//        {
//            selectItem(selected)
//        }


    }

}