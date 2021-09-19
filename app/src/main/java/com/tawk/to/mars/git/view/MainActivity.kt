package com.tawk.to.mars.git.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.tawk.to.mars.git.R

import com.tawk.to.mars.git.databinding.ActivityMainBinding
import com.tawk.to.mars.git.util.Constants
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.view.fragment.SplashFragment
import com.tawk.to.mars.git.view.fragment.viewpager.VPFragment
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {



    //========================================= Variable ===========================================
    //----------------------------------------- Variable -------------------------------------------
    var filter:String = ""
    //----------------------------------------------------------------------------------------------
    //------------------------------------------ Fragment ------------------------------------------
    lateinit var splash:SplashFragment
    lateinit var vp: VPFragment
    //----------------------------------------------------------------------------------------------
    //------------------------------------------- View ---------------------------------------------
    private lateinit var binding: ActivityMainBinding
    var searchView: SearchView? = null
    var menu: Menu? = null
    //----------------------------------------------------------------------------------------------
    //------------------------------------------ Dagger --------------------------------------------
    //Injected using Dagger2
    @Inject
    lateinit var  nvm: NetworkViewModel
    @Inject
    lateinit var  dvm: DatabaseViewModel
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //======================================== LifeCycle ===========================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.appBar.visibility= View.GONE
        binding.bottomNav.visibility = View.GONE
        binding.vDivider.visibility = View.GONE
        (application as TawkTo).appComponent.inject(this)

        splash = SplashFragment()
        vp = VPFragment()

        if (shouldRequestPermission())
        {
            //if not granted launch splash fragment to request for permissions
            supportFragmentManager.beginTransaction().add(R.id.fc_main,splash,"Splash").commit()
        }
        else
        {
            //if granted attach view pager and show appbar and bottom navbar
            showNavigation()
            setBottomNavigation()
            setVP()
        }
        nvm.results.observe(this, Observer {
            vp.setResults(it)
        })
        nvm.requestUsersFrom(0)

      }
    //==============================================================================================
    //========================================= View ===============================================
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.i("MENU","Menu created")
        menuInflater.inflate(R.menu.menu_main, menu)
        searchView = menu.findItem(R.id.action_search).getActionView() as SearchView
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filter = query
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        return true
    }

    fun setBottomNavigation()
    {
        menu = binding.bottomNav!!.menu
        vp!!.bottom_nav = binding.bottomNav
        binding.bottomNav!!.setOnNavigationItemSelectedListener {
            vp!!.selectItem(it.itemId)
            true
        }
        menu!!.add(Menu.NONE,0, Menu.NONE, getString(R.string.list))
            .setIcon(R.drawable.list)
        menu!!.add(Menu.NONE,1, Menu.NONE,getString(R.string.settings))
            .setIcon(R.drawable.setting)
    }

    fun showNavigation()
    {
        binding.appBar!!.visibility= View.VISIBLE
        binding.bottomNav!!.visibility = View.VISIBLE
        binding.vDivider!!.visibility = View.VISIBLE
        if(searchView!=null)
        {
            searchView!!.visibility =View.VISIBLE
        }
        binding.toolbar.title=getString(R.string.app_name)

    }
    fun setVP()
    {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        if(supportFragmentManager.findFragmentById(R.id.fc_main)!=null)
        {
            supportFragmentManager.beginTransaction().remove(supportFragmentManager.findFragmentById(R.id.fc_main)!!).commit()

        }
        supportFragmentManager.beginTransaction().add(R.id.fc_main, vp!!,"ViewPager").commit()
    }
    //==============================================================================================
    //======================================= Permission ===========================================
    //Check if permission to read and write on storage is granted returns true if app needs to request permission
    fun shouldRequestPermission():Boolean
    {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            Constants.REQUEST_PERMISSION ->{
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    supportFragmentManager.beginTransaction().remove(splash).commit()
                    showNavigation()
                    setBottomNavigation()
                }
            }
        }

    }
    //==============================================================================================
}