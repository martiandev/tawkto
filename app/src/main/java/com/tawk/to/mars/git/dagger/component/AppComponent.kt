package com.tawk.to.mars.git.dagger.component

import com.tawk.to.mars.git.dagger.module.NetworkModule
import com.tawk.to.mars.git.dagger.module.PreferenceModule
import com.tawk.to.mars.git.dagger.module.RoomModule
import com.tawk.to.mars.git.view.MainActivity
import com.tawk.to.mars.git.view.fragment.DetailFragment
import com.tawk.to.mars.git.view.fragment.viewpager.settings.SettingsFragment
import com.tawk.to.mars.git.view.fragment.viewpager.search.ResultAdapter
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import com.tawk.to.mars.git.viewmodel.PreferenceViewModel
import dagger.Component

@Component(
    modules=[
        NetworkModule::class,
        RoomModule::class,
        PreferenceModule::class
    ]
)
//Interface for allowing dependency inejection
interface AppComponent {
    //Allow dependency injection in MainActivity
    fun inject(mainActivity: MainActivity)
    //Allow dependency injection in the NetworkViewModel
    fun inject(networkViewModel: NetworkViewModel)
    //Allow dependency injection in the DatabaseViewModel
    fun inject(databaseViewModel: DatabaseViewModel)
    //Allow dependency injection in the PreferenceViewModel
    fun inject(preferenceViewModel: PreferenceViewModel)
    //Allow dependency injection in the DetailFragment
    fun inject(detailFragment: DetailFragment)
    //Allow dependency injection in the SettingsFragment
    fun inject(settingsFragment: SettingsFragment)
    //Allow dependency injection in the ResultAdapter
    fun inject(resultAdapter: ResultAdapter)
}