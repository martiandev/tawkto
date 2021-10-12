package com.tawk.to.mars.git.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tawk.to.mars.git.databinding.FragmentDetailBinding
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.network.request.UserImageRequest
import com.tawk.to.mars.git.model.network.request.UserRequest
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import com.tawk.to.mars.git.viewmodel.LocalImageViewModel
import com.tawk.to.mars.git.viewmodel.NetworkViewModel

class DetailFragment(val user:User): Fragment() {

    companion object
    {
        @JvmStatic
        @BindingAdapter("imageUrl","id","lIVM")
        fun loadImage(@NonNull ivIcon: ImageView, imageUrl:String,id:Int, lIVM:LocalImageViewModel) {
            if (imageUrl.length > 1)
            {
                lIVM.load(id,ivIcon.context,imageUrl,ivIcon,-1)
            }

        }
    }

    private var binding: FragmentDetailBinding? = null
    lateinit var networkViewModel:NetworkViewModel
    lateinit var databaseViewModel:DatabaseViewModel
    lateinit var loadLocalImageViewModel: LocalImageViewModel
    var isDirty = false

    var originalNote:String? = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding = FragmentDetailBinding.inflate(inflater, container, false)
        this.networkViewModel = ViewModelProvider(requireActivity()).get(NetworkViewModel::class.java)
        this.databaseViewModel = ViewModelProvider(requireActivity()).get(DatabaseViewModel::class.java)
        this.loadLocalImageViewModel = ViewModelProvider(requireActivity()).get(LocalImageViewModel::class.java)
        val view = binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding!!.user = this.user
        this.originalNote = this.user.note
        this.binding!!.etNote.setText(this.originalNote)
        this.binding!!.localImageVM = this.loadLocalImageViewModel
        this.binding!!.fragment = this
        this.networkViewModel.userResult.observe(requireActivity(), Observer {

            if( this.binding!=null)
            {
                var update = false
                if(this.user.updated_at!=null)
                {

                    if(this.user.updated_at!!.before(it.updated_at))
                    {
                        update = true
                    }
                }
                else
                {
                    update  = true
                }
                if(update)
                {
                    if(it.id == user.id)
                    {
                        isDirty = true
                        this.binding!!.user = it
                        this.databaseViewModel.saveProfile(it!!)
                        this.networkViewModel.request(UserImageRequest(it.id,requireActivity(),it.avatarUrl!!,binding!!.ivAvatar,-1))
                    }

                }

            }
        })

        this.networkViewModel.request(UserRequest(this.user.login!!))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.binding = null
        this.networkViewModel.userResult.removeObservers(requireActivity())
    }


    fun save()
    {
        var save = false
        if(this.originalNote!=null)
        {
            if(!this.originalNote!!.equals(this.binding!!.etNote.text))
            {
                save = true
            }
        }

        if(isDirty)
        {
            save = true
        }
        if(save)
        {
            this.databaseViewModel.saveNote(this.user!!.id,this.binding!!.etNote!!.text!!.toString())


        }
        this.binding!!.etNote.setText("")
        this.databaseViewModel.resetUser()
    }
}
