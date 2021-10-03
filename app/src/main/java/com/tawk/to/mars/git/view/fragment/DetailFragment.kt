package com.tawk.to.mars.git.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.adapters.TextViewBindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tawk.to.mars.git.databinding.FragmentDetailBinding
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.network.request.UserImageRequest
import com.tawk.to.mars.git.model.network.request.UserRequest
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import java.lang.ref.WeakReference

class DetailFragment: Fragment() {
    private var binding: FragmentDetailBinding? = null
    lateinit var networkViewModel:NetworkViewModel
    lateinit var databaseViewModel:DatabaseViewModel
    var user:User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        networkViewModel = ViewModelProvider(requireActivity()).get(NetworkViewModel::class.java)
        databaseViewModel = ViewModelProvider(requireActivity()).get(DatabaseViewModel::class.java)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkViewModel.userResult.observe(requireActivity(), Observer {
            it.note = this.user!!.note
            updateUser(it)
            databaseViewModel.saveUser(it!!)
        })
        databaseViewModel.userResult.observe(requireActivity(), Observer {
            updateUser(it)
            networkViewModel.request(UserRequest(it.login!!))
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun updateUser(user:User)
    {
        this.user = user
        if(binding!=null)
        {
            binding!!.tvName.text=this.user!!.name
            binding!!.tvUsername.text=this.user!!.login
            binding!!.tvCompany.text=this.user!!.company
            binding!!.tvRepos.text=""+this.user!!.publicRepos
            binding!!.tvGist.text=""+this.user!!.publicGists
            binding!!.tvFollowers.text=""+this.user!!.followers
            binding!!.tvFollowing.text=""+this.user!!.following
            binding!!.etNote.setText(when(this.user!!.note!=null){
                true->user!!.note!!
                false->""
            })
            var request = UserImageRequest(this.user!!.id,
                    requireContext(),
                this.user!!.avatarUrl!!,
                    WeakReference(binding!!.ivAvatar),
                -1
            )
            networkViewModel.request(request)
            binding!!.etNote.addTextChangedListener (object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(s: Editable?) {

                        user!!.note = when(s!=null){
                            true -> s.toString()
                            false -> ""
                        }



                }

            })

        }

    }
    fun save()
    {
        if(databaseViewModel!=null&&this.user!=null&&this.user!!.note!=null)
        {
            this.databaseViewModel.saveNote(this.user!!.id,this.user!!.note!!)
        }

    }
}
