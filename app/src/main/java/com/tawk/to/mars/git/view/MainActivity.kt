package com.tawk.to.mars.git.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.tawk.to.mars.git.R
import com.tawk.to.mars.git.model.entity.embedded.Note
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var  nvm: NetworkViewModel
    @Inject
    lateinit var  dvm: DatabaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as TawkTo).appComponent.inject(this)

    }
}