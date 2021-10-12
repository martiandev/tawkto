package com.tawk.to.mars.git.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.network.GitHubService
import com.tawk.to.mars.git.model.network.QueueManager
import com.tawk.to.mars.git.model.network.request.Request
import com.tawk.to.mars.git.model.network.request.SinceRequest
import com.tawk.to.mars.git.model.network.request.UserImageRequest
import com.tawk.to.mars.git.model.network.request.UserRequest
import com.tawk.to.mars.git.model.preference.Preference
import com.tawk.to.mars.git.util.Constants
import com.tawk.to.mars.git.view.app.TawkTo
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException

//Used to request from git hub api
class NetworkViewModel : ViewModel(),UserImageRequest.Listener {
    //========================================= Variable ===========================================
    //----------------------------------- Dependency Injection -------------------------------------
    lateinit var tawkTo: TawkTo
    @Inject
    lateinit var gitHubService: GitHubService
    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var preference: Preference
    //----------------------------------------------------------------------------------------------
    //------------------------------------------- LiveData -----------------------------------------
    var connection = MutableLiveData<Boolean>()
    var results = MutableLiveData<List<User>>()
    var userResult = MutableLiveData<User>()
    //----------------------------------------------------------------------------------------------
    //-------------------------------------------- Status ------------------------------------------
    //Blocks execution until the current request is done
    var isRequesting: Boolean = false
    //----------------------------------------------------------------------------------------------
    //-------------------------------------------- CallBack ----------------------------------------
    //Receives response on items requested
    var onItemsLoadedCallBack: Callback<ResponseBody> = object : Callback<ResponseBody> {
        override fun onResponse(
            call: Call<ResponseBody>,
            response: Response<ResponseBody>
        ) {
            if (response.code() == 200) {
                connection.postValue(true)
                val userArray: Array<User> =
                    gson.fromJson(response.body()!!.string(), Array<User>::class.java)

                if (userArray.size > 0) {
                    results.postValue(userArray.toList())
                } else {
                    results.postValue(listOf())
                }

            } else {
                connection.postValue(false)
            }
            isRequesting = false
            next()
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            if (t is UnknownHostException) {
                connection.postValue(false)
            }
            isRequesting = false
            next()

        }

    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //=========================================== Method ===========================================
    //------------------------------------------- Set-up -------------------------------------------
    //setup for dependency injection
    fun init(tawkTo:TawkTo)
    {
        this.tawkTo = tawkTo
        this.tawkTo .appComponent.inject(this)
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------- Status -------------------------------------------
    //Update network status
    fun setConnection(b: Boolean)
    {
        connection.postValue(b)
    }
    //----------------------------------------------------------------------------------------------
    //-------------------------------------------- Queue -------------------------------------------
    //Adds a request to the queue
    fun request(r: Request)
    {
        QueueManager.getInstance(tawkTo).addToQueue(r)
        next()
    }
    //Performs next request on the queue
    private fun next()
    {
        if(!isRequesting)
        {

            if(QueueManager.getInstance(tawkTo).queue.size>0)
            {
                isRequesting = true
                var request =  QueueManager.getInstance(tawkTo).getNext()
                if(request!=null)
                {

                    if(request is SinceRequest)
                    {

                        if(preference.getPageSize()>30)
                        {
                            requestUsersFrom(request.id!!,preference.getPageSize())
                        }
                        else
                        {
                            requestUsersFrom(request.id!!)
                        }

                    }
                    else if(request is UserRequest)
                    {

                        search(request.login!!)
                    }
                    else if(request is UserImageRequest)
                    {

                        request.start(this)
                    }
                    else
                    {
                        isRequesting = false
                        next()
                    }
                }
            }


        }
    }
    //----------------------------------------------------------------------------------------------
    //--------------------------------------------- API --------------------------------------------
    //Request users above the id with limit
    private fun requestUsersFrom(id:Int,limit:Int)
    {
        gitHubService
            .requestUsersLimit(id,limit,Constants.TOKEN.replace(":",""))
            .enqueue(onItemsLoadedCallBack)
    }
    //Request users above the id with 30 page size
    private fun requestUsersFrom(id:Int)
    {
        gitHubService
            .requestUsers(id,Constants.TOKEN.replace(":",""))
                .enqueue(onItemsLoadedCallBack)
    }
    //Request user matching same login
    private fun search(login:String)
    {

        gitHubService
            .requestUser(login,Constants.TOKEN.replace(":",""))
            .enqueue(object:Callback<User>{
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    if(response.code()==200)
                    {
                        connection.postValue(true)
                        userResult.postValue(response.body())

                    }
                    else
                    {
                        connection.postValue(false)
                    }
                    isRequesting = false
                    next()

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    if(t is UnknownHostException)
                    {
                        connection.postValue(false)
                    }
                    isRequesting = false
                    next()

                }

            })
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //==================================== UserImageRequestListener ================================
    //Triggered when UserImageRequest is successful
    override fun onDone() {
        isRequesting = false
        next()
    }
    //Triggered when UserImageRequest failed
    override fun onFailed() {
        isRequesting = false
        next()
    }
    //==============================================================================================


}