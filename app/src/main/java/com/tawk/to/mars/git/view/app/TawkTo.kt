package com.tawk.to.mars.git.view.app

import android.app.Application
import com.tawk.to.mars.git.dagger.component.AppComponent
import com.tawk.to.mars.git.dagger.component.DaggerAppComponent
import com.tawk.to.mars.git.dagger.module.NetworkModule
import com.tawk.to.mars.git.dagger.module.PreferenceModule
import com.tawk.to.mars.git.dagger.module.RoomModule

class TawkTo:Application()
{
    //Creates instance of App Component and set Modules
    open val appComponent: AppComponent by lazy{
        DaggerAppComponent
            .builder()
            .preferenceModule(preferenceModule)
            .roomModule(roomModule)
            .networkModule(networkModule)
            .build()
    }
    //Creates instance of Network Module
    private val networkModule by lazy{
        NetworkModule(this,"https://api.github.com/")
    }
    //Creates instance of Room Module
    private val roomModule by lazy{
        RoomModule(this)
    }
    //Creates instance of Preference Module
    private val preferenceModule by lazy{
        PreferenceModule(this)
    }
}