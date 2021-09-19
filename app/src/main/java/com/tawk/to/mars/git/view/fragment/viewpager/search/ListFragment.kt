package com.tawk.to.mars.git.view.fragment.viewpager.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawk.to.mars.git.databinding.FragmentSearchBinding
import com.tawk.to.mars.git.databinding.FragmentSettingBinding
import com.tawk.to.mars.git.databinding.FragmentViewpagerBinding
import com.tawk.to.mars.git.model.entity.User

class ListFragment:Fragment() {
    private var _binding: FragmentSearchBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view =_binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding!!.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setResults(users:List<User>)
    {
        _binding!!.rvSearch.adapter = ResultAdapter(users)
    }

}