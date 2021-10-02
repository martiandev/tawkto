package com.tawk.to.mars.git.view.fragment.viewpager.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawk.to.mars.git.databinding.FragmentSearchBinding
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.network.request.Request
import com.tawk.to.mars.git.model.network.request.SinceRequest
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import com.tawk.to.mars.git.viewmodel.NetworkViewModel

class ListFragment():Fragment(),NestedScrollView.OnScrollChangeListener {

    lateinit var binding: FragmentSearchBinding
    lateinit var nvm: NetworkViewModel
    lateinit var dvm: DatabaseViewModel
    lateinit var adapter:ResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        nvm = ViewModelProvider(requireActivity()).get(NetworkViewModel::class.java)
        nvm.init(requireActivity().application as TawkTo)
        dvm = ViewModelProvider(requireActivity()).get(DatabaseViewModel::class.java)
        dvm.init(requireActivity().application as TawkTo)
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.scroll.setOnScrollChangeListener(this)
        this.adapter = ResultAdapter(listOf(),requireActivity().application as TawkTo,requireActivity())
        binding!!.rvSearch.adapter = adapter
        dvm.results.observe(requireActivity(), Observer {
            if(it.size<1)
            {
                Log.i("LF-Frag","No Local Data Loaded")
            }
            else
            {
                Log.i("LF-Frag","Local Data Loaded")
                setResults(it)
                binding.loading.visibility = View.GONE
            }
        })
        nvm.results.observe(requireActivity(), Observer {
            Log.i("LF-Frag","Remote Data Loaded")
                dvm.save(it)
            Log.i("LF-Frag","Loading from Local:"+adapter.getLastID())
                dvm.get(adapter.getLastID())
            //setResults(it)
            binding.loading.visibility = View.GONE
        })
        getNextPage()

    }

    fun setResults(users: List<User>) {
        if (binding != null) {
            if(users.size>0)
            {
                adapter.add(users)
            }

        }

    }


    fun getNextPage() {

        binding.loading.visibility = View.VISIBLE
        dvm.get(adapter.getLastID())
        nvm.request(SinceRequest(adapter.getLastID()))
    }

    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        if (scrollY == v!!.getChildAt(0).getMeasuredHeight() - v!!.getMeasuredHeight()) {
            Log.i("LF-Frag","REQUESTINGGGG")
            getNextPage()

        }

    }
}