package com.tawk.to.mars.git.model.network

import com.tawk.to.mars.git.model.entity.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {

    @GET("users")
    fun requestUsers(@Query("since") since:Int): Call<ResponseBody>
    @GET("users")
    fun requestUsersLimit(@Query("since") since:Int,@Query("per_page") pageSize:Int): Call<ResponseBody>
    @GET("users/{username}")
    fun requestUser(@Path("username") username:String): Call<User>

}