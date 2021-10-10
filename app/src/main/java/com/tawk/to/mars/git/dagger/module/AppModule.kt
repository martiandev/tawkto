package com.tawk.to.mars.git.dagger.module

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
//Module for dependency injection of Application
@Module
class AppModule {

    private  lateinit var app: Application;
    constructor(app: Application)
    {
        this.app = app
    }
    @Provides
    @Singleton
    fun provideApplication(): Application
    {
        return app
    }
}