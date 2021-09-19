package com.tawk.to.mars.git.dagger.component

import com.tawk.to.mars.git.dagger.module.NetworkModule
import com.tawk.to.mars.git.dagger.module.RoomModule
import com.tawk.to.mars.git.view.MainActivity
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import dagger.Component

@Component(
    modules=[
        NetworkModule::class,
        RoomModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(networkViewModel: NetworkViewModel)
    fun inject(databaseViewModel: DatabaseViewModel)
}