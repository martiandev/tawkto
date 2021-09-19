package com.tawk.to.mars.git.dagger.module

import com.tawk.to.mars.git.model.Preference
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.viewmodel.PreferenceViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class PreferenceModule @Inject constructor(var app: TawkTo)
{
    @Provides
    fun providesPreference(): Preference {
           return Preference.getInstance(app)
    }

    @Provides
    fun providesPreferenceViewModel(): PreferenceViewModel{
        return PreferenceViewModel(app)
    }
}