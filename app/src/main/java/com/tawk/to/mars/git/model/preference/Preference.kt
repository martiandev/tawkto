package com.tawk.to.mars.git.model.preference

import android.content.Context
import android.content.SharedPreferences
import com.tawk.to.mars.git.util.Constants
import com.tawk.to.mars.git.util.SingletonHolder
import com.tawk.to.mars.git.view.app.TawkTo
import javax.inject.Inject

class Preference @Inject constructor(var app:TawkTo)
{
    private var sp:SharedPreferences = app.baseContext.getSharedPreferences(Constants.PREFERNCE_TAG, Context.MODE_PRIVATE)
    var editor:SharedPreferences.Editor = sp.edit()

    companion object : SingletonHolder<Preference, TawkTo>({
        Preference(it)
    })

    fun getPageSize():Int{
        return  sp.getInt(Constants.PREF_PAGE_SIZE,Constants.DEFAULT_PAGE_SIZE)
    }

    fun setPageSize(pageSize:Int)
    {
        editor.putInt(Constants.PREF_PAGE_SIZE,pageSize).commit()
    }









}