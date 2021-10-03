package com.tawk.to.mars.git.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tawk.to.mars.git.model.entity.User

class SelectionViewModel: ViewModel() {

    var selected = MutableLiveData<User>()

    fun select(user:User)
    {
        selected!!.postValue(user)
    }

}