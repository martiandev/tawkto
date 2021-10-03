package com.tawk.to.mars.git.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tawk.to.mars.git.R
import com.tawk.to.mars.git.databinding.FragmentDetailBinding
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.network.request.Request
import com.tawk.to.mars.git.model.network.request.UserImageRequest
import com.tawk.to.mars.git.model.network.request.UserRequest
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class DetailFragment: Fragment() {
    private var _binding: FragmentDetailBinding? = null
    lateinit var nvm:NetworkViewModel
    lateinit var dvm:DatabaseViewModel

    var user: User ? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        nvm = ViewModelProvider(requireActivity()).get(NetworkViewModel::class.java)
        dvm = ViewModelProvider(requireActivity()).get(DatabaseViewModel::class.java)
        val view = _binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            nvm.userResult.observe(requireActivity(), Observer {
                Log.i("DF","UPDATE FROM REMOTE:"+it.login)
                updateUser(it)
                dvm.saveUser(it!!)
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun clear()
    {
        this.user = null
    }
    fun updateUser(user:User)
    {

        Log.i("DF","display:"+user)
        if(_binding!=null)
        {
            Log.i("DF","Binded!")
            _binding!!.tvName.text=user.name
            _binding!!.tvUsername.text=user.login
            _binding!!.tvCompany.text=user.company
            _binding!!.tvRepos.text=""+user.publicRepos
            _binding!!.tvGist.text=""+user.publicGists
            _binding!!.tvFollowers.text=""+user.followers
            _binding!!.tvFollowing.text=""+user.following
            var request = UserImageRequest(user.id,
                requireContext(),
                user.avatarUrl!!,
                WeakReference(_binding!!.ivAvatar)
            )
            nvm.request(request)
        }
    }

}