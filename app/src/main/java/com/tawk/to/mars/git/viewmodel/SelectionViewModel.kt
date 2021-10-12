package com.tawk.to.mars.git.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tawk.to.mars.git.model.entity.User
//Used to monitor selected user from a list
class SelectionViewModel: ViewModel() {

    var selected = MutableLiveData<User>()

    fun select(user:User?)
    {
        if(user==null)
        {
            selected = MutableLiveData<User>()
        }
        else
        {
            selected!!.postValue(user!!)

        }


    }

}