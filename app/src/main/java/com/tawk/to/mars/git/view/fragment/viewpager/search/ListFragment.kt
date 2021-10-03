package com.tawk.to.mars.git.view.fragment.viewpager.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.NestedScrollView
import androidx.databinding.adapters.AbsListViewBindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawk.to.mars.git.databinding.FragmentSearchBinding
import com.tawk.to.mars.git.model.entity.User
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
        Log.i("ListFragment","onCreate")
        binding!!.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.scroll.setOnScrollChangeListener(this)
        this.adapter = ResultAdapter(listOf(),requireActivity().application as TawkTo,requireActivity())
        binding!!.rvSearch.adapter = adapter
        dvm.results.observe(requireActivity(), Observer {
            if(it.size>0)
            {
                setResults(it)
            }
            Log.i("ListFragment","dvmRecdivef")
            nvm.request(SinceRequest(adapter.getLastID()))
        })
        nvm.results.observe(requireActivity(), Observer {
            dvm.save(it)
            setResults(it)
        })
        Log.i("ListFragment","onViewCreatedNextPage")
        getNextPage()

    }

    fun setResults(users: List<User>) {
        if (binding != null) {
            if(users.size>0)
            {
                adapter.update(users)

            }
            binding!!.loading.visibility = View.GONE
        }
    }



    fun getNextPage() {

        binding.loading.visibility = View.VISIBLE
        dvm.get(adapter.getLastID())

    }

    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        if (scrollY == v!!.getChildAt(0).getMeasuredHeight() - v!!.getMeasuredHeight()) {
            Log.i("ListFragment","onScroll")

            getNextPage()

        }

    }
}