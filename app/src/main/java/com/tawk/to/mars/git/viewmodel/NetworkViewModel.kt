package com.tawk.to.mars.git.viewmodel

import android.util.Log
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
import com.tawk.to.mars.git.view.app.TawkTo
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException


class NetworkViewModel : ViewModel(),UserImageRequest.Listener
{
    @Inject
    lateinit var gitHubService: GitHubService
    @Inject
    lateinit var gson:Gson
    @Inject
    lateinit var preference: Preference

    var connection = MutableLiveData<Boolean>()
    var results = MutableLiveData<List<User>>()
    var userResult = MutableLiveData<User>()
    var isRequesting:Boolean = false
    lateinit var tawkTo:TawkTo
    var onItemsLoadedCallBack:Callback<ResponseBody> = object :Callback<ResponseBody>{
        override fun onResponse(
            call: Call<ResponseBody>,
            response: Response<ResponseBody>
        ) {
            connection.postValue(true)
            val userArray: Array<User> =
                gson.fromJson(response.body()!!.string(), Array<User>::class.java)

            if(userArray.size>0)
            {
                results.postValue(userArray.toList())
            }
            else
            {
                results.postValue(listOf())
            }
            isRequesting = false
            next()
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            if(t is UnknownHostException)
            {
                connection.postValue(false)
            }
            isRequesting = false
            next()

        }

    }


    fun init(tawkTo:TawkTo)
    {
        this.tawkTo = tawkTo
        this.tawkTo .appComponent.inject(this)
    }

    fun request(r: Request)
    {
        QueueManager.getInstance(tawkTo).addToQueue(r)
        next()
    }

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
                        search(request.login!!,request)
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
            else
            {
                Log.i("QUEUE","Queue is empty")
            }

        }
    }
    private fun requestUsersFrom(id:Int,limit:Int)
    {
        gitHubService
            .requestUsersLimit(id,limit)
            .enqueue(onItemsLoadedCallBack)
    }

    private fun requestUsersFrom(id:Int)
    {
        gitHubService
            .requestUsers(id)
                .enqueue(onItemsLoadedCallBack)
    }
    private fun search(login:String,r: Request)
    {
        gitHubService
            .requestUser(login)
            .enqueue(object:Callback<User>{
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {


                    connection.postValue(true)
                    userResult.postValue(response.body())
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

    override fun onDone() {
        isRequesting = false
        next()
    }

    override fun onFailed() {
        isRequesting = false
        next()
    }


}