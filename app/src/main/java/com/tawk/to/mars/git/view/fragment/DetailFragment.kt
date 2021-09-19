package com.tawk.to.mars.git.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.tawk.to.mars.git.R
import com.tawk.to.mars.git.databinding.FragmentDetailBinding
import com.tawk.to.mars.git.databinding.FragmentSplashBinding
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import javax.inject.Inject

class DetailFragment: Fragment() {
    private var _binding: FragmentDetailBinding? = null
    @Inject
    lateinit var nvm:NetworkViewModel

    var user: User ? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = _binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as TawkTo).appComponent.inject(this)
        if(user!=null)
        {
            nvm.userResult.observe(requireActivity(), Observer {
                if(it!=null)
                {
                    updateUser(it)
                }
                else
                {
                    requireActivity().onBackPressed()
                }

            })
            nvm.search(user!!.login!!)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateUser(user:User)
    {
        this.user = user
        if(_binding!=null)
        {
            _binding!!.tvName.text=user.name
            _binding!!.tvUsername.text=user.login
            _binding!!.tvCompany.text=user.company
            _binding!!.tvRepos.text=""+user.publicRepos
            _binding!!.tvGist.text=""+user.publicGists
            _binding!!.tvFollowers.text=""+user.followers
            _binding!!.tvFollowing.text=""+user.following
            Glide.with(requireActivity())
                .load(user.avatarUrl)
                .placeholder(R.drawable.no)
                .into(_binding!!.ivAvatar);
        }

    }

}