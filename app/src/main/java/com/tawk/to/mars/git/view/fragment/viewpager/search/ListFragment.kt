package com.tawk.to.mars.git.view.fragment.viewpager.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
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

class ListFragment():Fragment(),NestedScrollView.OnScrollChangeListener {

    lateinit var binding: FragmentSearchBinding
    lateinit var nvm: NetworkViewModel
    var latest = 0
    lateinit var adapter:ResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as TawkTo).appComponent.inject(this)
        binding!!.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.rvSearch.layoutManager = LinearLayoutManager(requireActivity())
        binding!!.scroll.setOnScrollChangeListener(this)
        nvm = ViewModelProvider(requireActivity()).get(NetworkViewModel::class.java)
        nvm.init(requireActivity().application as TawkTo)
        nvm.results.observe(requireActivity(), Observer {
            setResults(it)
            binding.loading.visibility = View.GONE
        })
        nvm.connection.observe(requireActivity(), Observer {
            binding!!.tvInternetWarning.visibility = when (it) {
                true -> View.GONE
                else -> View.VISIBLE
            }
        })
        this.adapter = ResultAdapter(listOf(),requireActivity().application as TawkTo,requireActivity())
        binding!!.rvSearch.adapter = adapter
        nvm.request(SinceRequest(latest))
    }

    fun setResults(users: List<User>) {
        if (binding != null) {
            if(users.size>0)
            {
                latest = users[users.lastIndex].id
                adapter.add(users)
            }

        }

    }


    fun getNextPage() {

    }

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        if (scrollY == v!!.getChildAt(0).getMeasuredHeight() - v!!.getMeasuredHeight()) {

            // on below line we are making our progress bar visible.
            binding.loading.visibility = View.VISIBLE
            nvm.request(SinceRequest(latest))
        }

    }
}