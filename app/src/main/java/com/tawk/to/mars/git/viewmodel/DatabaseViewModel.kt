package com.tawk.to.mars.git.viewmodel

import android.app.DownloadManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawk.to.mars.git.model.preference.Preference
import com.tawk.to.mars.git.model.database.TTDatabase
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.entity.UserUpdate
import com.tawk.to.mars.git.model.entity.UserUpdateNote
import com.tawk.to.mars.git.model.entity.UserUpdateProfile
import com.tawk.to.mars.git.view.app.TawkTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.math.sin

class DatabaseViewModel : ViewModel() {

    @Inject
    lateinit var db: TTDatabase

    @Inject
    lateinit var preference: Preference
    var results = MutableLiveData<List<User>>()
    var search = MutableLiveData<List<User>>()
    var userResult = MutableLiveData<User>()
    var savedNote = MutableLiveData<User>()
    var saved = MutableLiveData<List<User>>()
    lateinit var tawkTo: TawkTo

    fun init(tawkTo: TawkTo) {
        this.tawkTo = tawkTo
        tawkTo.appComponent.inject(this)
    }

    fun getUser(id: Int) {
        CoroutineScope(Dispatchers.IO).async {
            var user = db.userDao().get(id)
            Log.i("DF", "VM ID:" + id)

            withContext(Dispatchers.Main)
            {
                if (user != null) {
                    Log.i("DF", "VM USER:" + user.id)
                    Log.i("DF", "VM USER:" + user.login)

                    userResult.postValue(user)
                }

            }
        }
    }

    fun saveNote(id: Int, note: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            db.userDao().update(UserUpdateNote(id,note))
            var u = db.userDao().get(id)
            withContext(Dispatchers.Main)
            {
                savedNote.postValue(u!!)
            }
        }
    }
    fun saveUser (data:User)
    {
        CoroutineScope(Dispatchers.IO).launch {
            db.userDao().updateProfile(UserUpdateProfile(data))


        }

    }

    fun save(data:List<User>)
    {
        CoroutineScope(Dispatchers.IO).launch{
            if(data.size>0) {

                for(user in data)
                {
                    if(db.userDao().insert(user)==(-1).toLong())
                    {
                        db.userDao().update(UserUpdate(user))
                    }
                }
            }

        }


    }

    fun get(since:Int)
    {
        CoroutineScope(Dispatchers.IO).launch{
            val users = db.userDao().getSince(since,preference.getPageSize())
            withContext(Dispatchers.Main)
            {
                results.postValue(users)
            }
        }

    }
    fun search(query:String)
    {
        CoroutineScope(Dispatchers.IO).launch{
            val users = db.userDao().search(query)
            withContext(Dispatchers.Main)
            {
                search.postValue(users)
            }
        }

    }

}