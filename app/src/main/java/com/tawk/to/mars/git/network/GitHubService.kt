package com.tawk.to.mars.git.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {

    @GET("users/{since}")
    fun requestUsers(@Path("since") since:Int): Call<ResponseBody>
    @GET("users/{username}")
    fun requestUser(@Path("username") username:String): Call<ResponseBody>

}