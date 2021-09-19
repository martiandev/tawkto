package com.tawk.to.mars.git.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.network.GitHubService
import com.tawk.to.mars.git.view.app.TawkTo
import okhttp3.ResponseBody
import retrofit2.Call
import javax.inject.Inject
import retrofit2.Callback
import retrofit2.Response




class NetworkViewModel : ViewModel
{
    @Inject
    lateinit var gitHubService: GitHubService
    @Inject
    lateinit var gson:Gson

    var results = MutableLiveData<List<User>>()
    var userResult = MutableLiveData<User>()

    constructor(tawkTo:TawkTo)
    {
        tawkTo.appComponent.inject(this)
    }


    fun requestUsersFrom(id:Int)
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
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.i("TEST","FAILED:"+t.localizedMessage)
                        Log.i("TEST","FAILED:"+t.stackTrace.toString())

                    }

                })
    }
    fun search(login:String)
    {
        gitHubService
            .requestUser(login)
            .enqueue(object:Callback<User>{
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {

                    userResult.postValue(response.body())

                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.i("TEST","FAILED:"+t.localizedMessage)
                    Log.i("TEST","FAILED:"+t.stackTrace.toString())

                }

            })
    }
}