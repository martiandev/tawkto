package com.tawk.to.mars.git.view.app

import android.app.Application
import com.tawk.to.mars.git.dagger.component.AppComponent
import com.tawk.to.mars.git.dagger.component.DaggerAppComponent
import com.tawk.to.mars.git.dagger.module.NetworkModule
import com.tawk.to.mars.git.dagger.module.PreferenceModule
import com.tawk.to.mars.git.dagger.module.RoomModule

class TawkTo:Application()
{
    open val appComponent: AppComponent by lazy{
        DaggerAppComponent
            .builder()
            .preferenceModule(preferenceModule)
            .roomModule(roomModule)
            .networkModule(networkModule)
            .build()
    }

    private val networkModule by lazy{
        NetworkModule(this,"https://api.github.com/")
    }
    private val roomModule by lazy{
        RoomModule(this)
    }
    private val preferenceModule by lazy{
        PreferenceModule(this)
    }
}