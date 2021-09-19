package com.tawk.to.mars.git.view.fragment.viewpager.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawk.to.mars.git.databinding.FragmentSearchBinding
import com.tawk.to.mars.git.databinding.FragmentSettingBinding
import com.tawk.to.mars.git.databinding.FragmentViewpagerBinding
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.view.fragment.viewpager.ClickListener
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import javax.inject.Inject

class ListFragment():Fragment() {
    private var _binding: FragmentSearchBinding? = null
    lateinit var cl:ClickListener
    @Inject
    lateinit var nvm:NetworkViewModel

    constructor(cl:ClickListener):this()
    {
        this.cl = cl
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view =_binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as TawkTo).appComponent.inject(this)
        _binding!!.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
        nvm.requestUsersFrom(0)
        nvm.results.observe(requireActivity(), Observer {
            setResults(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setResults(users:List<User>)
    {
        if(_binding!=null)
        {
            _binding!!.rvSearch.adapter = ResultAdapter(users,cl)
        }

    }

}