package com.tawk.to.mars.git.view.fragment.viewpager.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawk.to.mars.git.databinding.FragmentSearchBinding
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.network.request.SinceRequest
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.view.fragment.viewpager.ClickListener
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import javax.inject.Inject

class ListFragment():Fragment() {
    private var _binding: FragmentSearchBinding? = null
    lateinit var nvm:NetworkViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view =_binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as TawkTo).appComponent.inject(this)
        _binding!!.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
        nvm = ViewModelProvider(requireActivity()).get(NetworkViewModel::class.java)
        nvm.init(requireActivity().application as TawkTo)
        nvm.results.observe(requireActivity(), Observer {
            setResults(it)
        })
        nvm.request(SinceRequest(0))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setResults(users:List<User>)
    {
        if(_binding!=null)
        {
            _binding!!.rvSearch.adapter = ResultAdapter(users,requireActivity().application as TawkTo,requireActivity())
        }

    }

}