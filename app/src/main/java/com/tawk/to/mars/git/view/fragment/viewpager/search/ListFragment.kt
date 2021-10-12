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

    val TAG_ADAPTER = "adapter"
    val TAG_LIST = "list"
    lateinit var binding: FragmentSearchBinding
    lateinit var nvm: NetworkViewModel
    lateinit var dvm: DatabaseViewModel
    lateinit var adapter:ResultAdapter
     var users:ArrayList<User> = ArrayList<User>()
    var isSearch = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedInstanceState!=null)
        {
            this.adapter = savedInstanceState.getSerializable(TAG_ADAPTER) as ResultAdapter
            Log.i("VP","list:"+adapter.items)
            this.users = savedInstanceState.getSerializable(TAG_LIST) as ArrayList<User>
        }
        nvm = ViewModelProvider(requireActivity()).get(NetworkViewModel::class.java)
        nvm.init(requireActivity().application as TawkTo)
        dvm = ViewModelProvider(requireActivity()).get(DatabaseViewModel::class.java)
        dvm.init(requireActivity().application as TawkTo)
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding!!.root
        return view
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(TAG_ADAPTER,adapter)
        outState.putSerializable(TAG_LIST,adapter.items)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.scroll.setOnScrollChangeListener(this)
        this.adapter = ResultAdapter(listOf(),requireActivity().application as TawkTo,requireActivity())
        binding!!.rvSearch.adapter = adapter
        dvm.results.observe(requireActivity(), Observer {
            if(isSearch)
            {
                isSearch = false
                this.adapter = ResultAdapter(listOf(),requireActivity().application as TawkTo,requireActivity())
                binding!!.rvSearch.adapter = adapter
            }
            if(it.size>0)
            {
                setResults(it)
            }
            Log.i("VP","REQUESTING:"+adapter.getLastID())
            nvm.request(SinceRequest(adapter.getLastID()))
        })
        nvm.results.observe(requireActivity(), Observer {
            dvm.save(it)
            setResults(it)
        })
        dvm.search.observe(requireActivity(), Observer {
            isSearch = true
            this.adapter = ResultAdapter(listOf(),requireActivity().application as TawkTo,requireActivity())
            binding!!.rvSearch.adapter = adapter
            if(it.size>0)
            {
                setResults(it)
            }

        })
        nvm.connection.observe(requireActivity(), Observer {
            binding!!.tvInternetWarning.visibility = when(it)
            {
                true->View.GONE
                false->View.VISIBLE
            }
            binding.loading.visibility = View.GONE

        })
        if(adapter!=null)
        {
            Log.i("VP","USE LOADED")
            binding.rvSearch.adapter = adapter
            adapter.update(users)
            if(users.size<1)
            {
                getNextPage()
            }
        }
        else
        {
            Log.i("VP","REQUEST MORE ")

            getNextPage()
        }


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
            if(!isSearch)
            {
                getNextPage()

            }

        }

    }
}