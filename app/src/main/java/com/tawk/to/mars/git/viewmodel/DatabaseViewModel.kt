package com.tawk.to.mars.git.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tawk.to.mars.git.model.preference.Preference
import com.tawk.to.mars.git.model.database.TTDatabase
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.entity.UserUpdate
import com.tawk.to.mars.git.model.entity.UserUpdateNote
import com.tawk.to.mars.git.model.entity.UserUpdateProfile
import com.tawk.to.mars.git.view.app.TawkTo
import kotlinx.coroutines.*
import javax.inject.Inject


//ViewModel for interacting with the database
class DatabaseViewModel : ViewModel() {

    //======================================== Variable ============================================
    //----------------------------------- Dependency Injection -------------------------------------
    //Required for dependency injection
    lateinit var tawkTo: TawkTo
    //Access Room Database
    @Inject
    lateinit var db: TTDatabase
    //Access Shared Preference
    @Inject
    lateinit var preference: Preference
    //----------------------------------------------------------------------------------------------
    //---------------------------------------- LiveData --------------------------------------------
    //Observe the results of the get(since) method
    var results = MutableLiveData<List<User>>()
    //Observe the results of the search(query) method
    var search = MutableLiveData<List<User>>()
    //Observe the results of the getUser(id) method
    var userResult = MutableLiveData<User>()
    //Observe the user whose note was updated using saveNote(user) method
    var savedNote = MutableLiveData<User>()
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //========================================= Method =============================================
    //----------------------------------------- Setup ----------------------------------------------
    //set-up dependency injection
    fun init(tawkTo: TawkTo) {
        this.tawkTo = tawkTo
        tawkTo.appComponent.inject(this)
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------ GET -----------------------------------------------
    //Retrieve user matching the id provided results can be observed using userResult live data
    fun getUser(id: Int) {
        CoroutineScope(Dispatchers.IO).async {
            var user = db.userDao().get(id)
            withContext(Dispatchers.Main)
            {
                if (user != null) {
                    userResult.postValue(user)
                }

            }
        }

    }
    /*
    Retrieve a list of users starting from the user whose id matches since variable limited to the page size
    set in the preference. Results can be observed through results livedata
     */
    fun get(since:Int) {
        CoroutineScope(Dispatchers.IO).launch{
            val users = db.userDao().getSince(since,preference.getPageSize())
            withContext(Dispatchers.Main)
            {
                results.postValue(users)
            }
        }

    }
    /*
    Retrieve a list of users starting from the user who contains the query string in either the note
    or login. Results can be observed through search livedata
     */
    fun search(query:String) {
        CoroutineScope(Dispatchers.IO).launch{
            val users = db.userDao().search(query)
            withContext(Dispatchers.Main)
            {
                search.postValue(users)
            }
        }

    }
    //----------------------------------------------------------------------------------------------
    //----------------------------------------- UPDATE ---------------------------------------------
    //Resets the value of userResult live data
    fun resetUser() {
        userResult =  MutableLiveData<User>()
    }
    //Saves a note for the user whose ID matches id
    fun saveNote(id: Int, note: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db.userDao().update(UserUpdateNote(id,note))
            var u = db.userDao().get(id)
            withContext(Dispatchers.Main)
            {
                savedNote.postValue(u!!)
            }
        }
    }
    //Saves updates to the User's profile fetched from /user/{username} api
    fun saveProfile(user:User) {
        CoroutineScope(Dispatchers.IO).launch {
            db.userDao().update(UserUpdateProfile(user))
        }
    }
    //Save a list of users to the database
    fun save(data:List<User>) {
        CoroutineScope(Dispatchers.IO).launch{
            if(data.size>0) {

                for(user in data)
                {
                    //try to insert user failed insert will result to -1 return
                    if(db.userDao().insert(user)==(-1).toLong())
                    {
                        //update the user if insertion failed
                        db.userDao().update(UserUpdate(user))
                    }
                }
            }

        }


    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================


}