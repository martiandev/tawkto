package com.tawk.to.mars.git.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.tawk.to.mars.git.R
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
        nvm.requestUsersFrom(0)
//        nvm.search("mojomboosanasd")


        dvm.results.observe(this, Observer {
            for(u in it)
            {
                Log.i("USERS","LOGIN:"+u.login+"("+u.id+")")
            }
        })

        dvm.saved.observe(this, Observer {
            if(it.size>0)
            {
                dvm.get(19,10)
            }
        })

        nvm.results.observe(this, Observer {
            dvm.save(it)

        })



    }
}