package com.tawk.to.mars.git.model.network

import com.tawk.to.mars.git.model.entity.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {

    //Request users with id higher than since with default page size 30
    @GET("users")
    fun requestUsers(@Query("since") since:Int, @Header("Authorization") accessToken: String): Call<ResponseBody>
    //Request users with id higher than since with user defined page size
    @GET("users")
    fun requestUsersLimit(@Query("since") since:Int,@Query("per_page") pageSize:Int, @Header("Authorization") accessToken: String): Call<ResponseBody>
    //Request specific user using login
    @GET("users/{username}")
    fun requestUser(@Path("username") username:String, @Header("Authorization") accessToken: String): Call<User>

}