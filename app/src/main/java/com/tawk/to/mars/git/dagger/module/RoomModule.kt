package com.tawk.to.mars.git.dagger.module

import com.tawk.to.mars.git.model.database.TTDatabase
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Inject
//Module for dependency injection of Database requires reference to Application context and base url of the server
@Module
class RoomModule @Inject constructor(var app: TawkTo)
{
    //Provides TTDatabase for accessing database
    @Provides
    fun provideDatabase(): TTDatabase {
           return TTDatabase.getInstance(app)
    }

}