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
import com.tawk.to.mars.git.view.app.TawkTo
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject
import retrofit2.Callback
import retrofit2.Response


class NetworkViewModel : ViewModel(),UserImageRequest.Listener
{
    @Inject
    lateinit var gitHubService: GitHubService
    @Inject
    lateinit var gson:Gson

    var results = MutableLiveData<List<User>>()
    var userResult = MutableLiveData<User>()
    var isRequesting:Boolean = false
    lateinit var tawkTo:TawkTo

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
                if(request is SinceRequest)
                {
                    requestUsersFrom(request.id!!,request)
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
            else
            {
                Log.i("QUEUE","Queue is empty")
            }

        }
    }

    private fun requestUsersFrom(id:Int,r: Request)
    {
        gitHubService
            .requestUsers(id)
                .enqueue(object:Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
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
                        isRequesting = false
                        next()

                    }

                })
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

                    Log.d("NVM","SUCCESS")
                    userResult.postValue(response.body())
                    isRequesting = false
                    next()

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
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