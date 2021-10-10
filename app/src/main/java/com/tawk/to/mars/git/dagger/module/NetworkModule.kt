package com.tawk.to.mars.git.dagger.module

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tawk.to.mars.git.model.network.GitHubService
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
//Module for dependency injection of Network requires reference to Application context and base url of the server
@Module
class NetworkModule @Inject constructor(var app: TawkTo,var baseUrl:String)
{
    //Provides Gson used for parsing JSON t POJOs
    @Provides
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    //Provides retrofit to access git hub API
    @Provides
    fun provideRetrofit(gson: Gson?): Retrofit {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging);
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .client(httpClient!!.build())
            .build()
    }
    //Provides Instance of GitHubService to access git APIs
    @Provides
    fun provideGitService(retrofit: Retrofit): GitHubService
    {
        return  retrofit.create(GitHubService::class.java)
    }


}