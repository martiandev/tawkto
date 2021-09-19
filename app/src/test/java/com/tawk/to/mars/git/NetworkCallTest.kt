package com.tawk.to.mars.git

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.tawk.to.mars.git.network.GitHubService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config
import org.robolectric.Robolectric

import org.robolectric.util.ActivityController

import org.mockito.MockitoAnnotations

import org.junit.Before

import org.mockito.ArgumentCaptor

import org.mockito.Captor
import org.mockito.Mock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Config(constants = BuildConfig::class, sdk = [21], manifest = "app/src/main/AndroidManifest.xml")
@RunWith(
    RobolectricGradleTestRunner::class
)
internal class NetworkCallTest {

    var mainActivity:MainActivity? = null
    var retrofit:Retrofit? = null
    var gsonBuilder:GsonBuilder? = null
    var baseUrl = ""

    @Mock
    private val gitHubService: GitHubService? = null

    @Captor
    private val callbackArgumentCaptor: ArgumentCaptor<Callback<List<YourObject>>>? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val controller = Robolectric.buildActivity(
            MainActivity::class.java
        )
        mainActivity = controller.get()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        gsonBuilder = GsonBuilder()
        gsonBuilder!!.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder!!.build()))
            .baseUrl(baseUrl)
            .client(httpClient!!.build())
            .build()

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)


        // Then we need to swap the retrofit api impl. with a mock one
        // I usually store my Retrofit api impl as a static singleton in class RestClient, hence:
        gitHubService =
        controller.create()
    }

}
