package com.tawk.to.mars.git.dagger.module

import com.tawk.to.mars.git.model.database.TTDatabase
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class RoomModule @Inject constructor(var app: TawkTo)
{
    @Provides
    fun provideDatabase(): TTDatabase {
           return TTDatabase.getInstance(app)
    }

    @Provides
    fun providesDatabaseViewModel():DatabaseViewModel{
        return DatabaseViewModel(app)
    }


}