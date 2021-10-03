package com.tawk.to.mars.git.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tawk.to.mars.git.R

import com.tawk.to.mars.git.databinding.ActivityMainBinding
import com.tawk.to.mars.git.model.network.request.UserRequest

import com.tawk.to.mars.git.util.Constants
import com.tawk.to.mars.git.view.app.TawkTo
import com.tawk.to.mars.git.view.fragment.DetailFragment
import com.tawk.to.mars.git.view.fragment.SplashFragment
import com.tawk.to.mars.git.view.fragment.viewpager.VPFragment
import com.tawk.to.mars.git.viewmodel.DatabaseViewModel
import com.tawk.to.mars.git.viewmodel.NetworkViewModel
import com.tawk.to.mars.git.viewmodel.SelectionViewModel

class MainActivity() : AppCompatActivity(){

    //========================================= Variable ===========================================
    //----------------------------------------- Variable -------------------------------------------
    var filter:String = ""
    //----------------------------------------------------------------------------------------------
    //------------------------------------------ Fragment ------------------------------------------
    lateinit var splash:SplashFragment
     var vp: VPFragment? =null
    lateinit var df: DetailFragment
    //----------------------------------------------------------------------------------------------
    //------------------------------------------- View ---------------------------------------------
    private lateinit var binding: ActivityMainBinding
    var searchView: SearchView? = null
    var menu: Menu? = null

    lateinit var  databaseViewModel: DatabaseViewModel
    lateinit var  selectionViewModel: SelectionViewModel
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //======================================== LifeCycle ===========================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as TawkTo).appComponent.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.appBar.visibility= View.GONE
        binding.bottomNav.visibility = View.GONE
        binding.vDivider.visibility = View.GONE
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setUpViewModel()
        setUpFragments()

        if (shouldRequestPermission())
        {
            supportFragmentManager.beginTransaction().add(R.id.fc_main,splash,"Splash").commit()
        }
        else
        {
            showNavigation()
            setBottomNavigation()
            setVP()
        }
      }
    override fun onBackPressed() {

        var fragment = supportFragmentManager.findFragmentById(R.id.fc_main)
        if(fragment!=null)
        {
            if(fragment is DetailFragment)
            {
                (fragment as DetailFragment).save()
                setVP()
            }
            else
            {
                super.onBackPressed()
            }
        }
        else
        {
            super.onBackPressed()
        }


    }
    //==============================================================================================
    //========================================= View ===============================================
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {

                true
            }
            android.R.id.home ->
            {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
    //==============================================================================================
    //======================================= View =================================================
    fun setUpViewModel()
    {
        this.databaseViewModel = ViewModelProvider(this).get(DatabaseViewModel::class.java)
        this.selectionViewModel = ViewModelProvider(this).get(SelectionViewModel::class.java)
        this.databaseViewModel.init(application as TawkTo)

        this.selectionViewModel.selected!!.observe(this, Observer {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            if(this.searchView!=null)
            {
                this.searchView!!.visibility =View.GONE
            }
            this.binding.bottomNav!!.visibility = View.GONE
            this.binding.vDivider!!.visibility = View.GONE
            if(supportFragmentManager.findFragmentById(R.id.fc_main)!=null)
            {
                supportFragmentManager.beginTransaction().remove(supportFragmentManager.findFragmentById(R.id.fc_main)!!).commit()
            }
            supportFragmentManager.beginTransaction().add(R.id.fc_main, df!!,"selection").commit()
            binding.toolbar.title = it.login
//            databaseViewModel.get(it.id)
            databaseViewModel.getUser(it.id)


        })

    }
    fun setUpFragments()
    {
        splash = SplashFragment()
        vp = VPFragment()
        df = DetailFragment()
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

        if(supportFragmentManager.findFragmentById(R.id.fc_main)!=null)
        {
            supportFragmentManager.beginTransaction().remove(supportFragmentManager.findFragmentById(R.id.fc_main)!!).commit()
        }
        supportFragmentManager.beginTransaction().add(R.id.fc_main, vp!!,"ViewPager").commit()
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        binding.toolbar.visibility= View.VISIBLE
        binding.toolbar.title = "TawkTo"
        binding.vDivider!!.visibility= View.VISIBLE
        binding.bottomNav!!.visibility= View.VISIBLE
        if(searchView!=null)
        {
            searchView!!.visibility =View.VISIBLE
        }

        invalidateOptionsMenu()
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
                    setVP()
                }
            }
        }

    }
    //==============================================================================================

}