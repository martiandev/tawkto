package com.tawk.to.mars.git.view.app

import android.app.Application
import com.tawk.to.mars.git.dagger.component.AppComponent
import com.tawk.to.mars.git.dagger.component.DaggerAppComponent
import com.tawk.to.mars.git.dagger.module.NetworkModule

class TawkTo:Application()
{
    open val appComponent: AppComponent by lazy{
        DaggerAppComponent
            .builder()
            .networkModule(networkModule)
            .build()
    }

    open val networkModule by lazy{
        NetworkModule(this,"https://api.github.com/")
    }
}