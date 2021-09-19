package com.tawk.to.mars.git.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tawk.to.mars.git.model.database.TTDatabase
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.view.app.TawkTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DatabaseViewModel :ViewModel{

    @Inject
    lateinit var db: TTDatabase
    var results = MutableLiveData<List<User>>()
    var saved = MutableLiveData<List<User>>()

    constructor(tawkTo: TawkTo)
    {
        tawkTo.appComponent.inject(this)
    }


    fun save(data:List<User>)
    {
        db.userDao()
            .insert(data)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    saved.postValue(data)
                },
                {
                    saved.postValue(listOf())

                }
            )

    }

    fun get(since:Int,limit:Int)
    {
        db.userDao()
            .getSince(since,limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.i("Network View MODEL","Retrieved"+it.size)
                    if(!it.isEmpty())
                    {
                        results.postValue(it)
                    }
                    else
                    {
                        results.postValue(listOf())
                    }

                },
                {

                }
            )

    }

}