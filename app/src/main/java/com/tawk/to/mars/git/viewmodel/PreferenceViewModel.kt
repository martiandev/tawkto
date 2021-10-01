package com.tawk.to.mars.git.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tawk.to.mars.git.model.preference.Preference
import com.tawk.to.mars.git.view.app.TawkTo

import javax.inject.Inject

class PreferenceViewModel :ViewModel{

    @Inject
    lateinit var pref: Preference
    var pageSize = MutableLiveData<Int>()

    constructor(tawkTo: TawkTo)
    {
        tawkTo.appComponent.inject(this)
    }

    fun getPageSize():Int
    {
        return pref.getPageSize()
    }
    fun setPageSize(page:Int)
    {
        pref.setPageSize(page)
    }
    fun updatePageSize()
    {
        pageSize.postValue(getPageSize())
    }


}