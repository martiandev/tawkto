package com.tawk.to.mars.git.dagger.component

import com.tawk.to.mars.git.dagger.module.NetworkModule
import com.tawk.to.mars.git.view.MainActivity
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import dagger.Component

@Component(
    modules=[
        NetworkModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(networkViewModel: NetworkViewModel)
}