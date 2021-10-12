package com.tawk.to.mars.git.view

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
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
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkInfo

import android.net.ConnectivityManager
import android.os.PersistableBundle
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import com.tawk.to.mars.git.model.entity.User
import com.tawk.to.mars.git.model.network.request.SinceRequest


class MainActivity() : AppCompatActivity(){

    //========================================= Variable ===========================================
    //---------------------------------------- BroadcastReceiver -----------------------------------
    var receiver: BroadcastReceiver? = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val cm = context!!.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            if(networkViewModel!=null)
            {
                if(netInfo==null)
                {
                    networkViewModel.setConnection(false)
                }
                else
                {
                    networkViewModel.setConnection(netInfo.isConnected)
                    if(netInfo.isConnected)
                    {
                        networkViewModel.request(SinceRequest(0))
                    }
                }
            }

        }
    }
    //----------------------------------------------------------------------------------------------
    //----------------------------------------- Variable -------------------------------------------
    var filter:String = ""
    val TAG_FILTER = "FILTER"
    val TAG_SPLASH = "Splash"
    val TAG_VP = "VP"
    val TAG_DF = "Details"
    //----------------------------------------------------------------------------------------------
    //------------------------------------------ Fragment ------------------------------------------
     var splash:SplashFragment? = null
     var vp: VPFragment? =null
     var df: DetailFragment? = null
    //----------------------------------------------------------------------------------------------
    //------------------------------------------- View ---------------------------------------------
    private lateinit var binding: ActivityMainBinding
    var searchView: SearchView? = null
    var menu: Menu? = null
    lateinit var  databaseViewModel: DatabaseViewModel
    lateinit var  networkViewModel: NetworkViewModel
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
        reloadInstance(savedInstanceState)
        setUpFragments(savedInstanceState)
        setUpViewModel()
        displayInitial()
      }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TAG_FILTER,filter)

    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(receiver, filter)
    }
    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }
    override fun onBackPressed() {

        if(supportFragmentManager.findFragmentById(R.id.fc_main) is DetailFragment)
        {
            df!!.save()
            selectionViewModel.select(null)
            selectionViewModel.selected.removeObservers(this)
            selectionViewModel.selected.observe(this, Observer {
                if(it!=null)
                {
                    setDetailFragment(it)
                }
            })

            setVP()

        }
        else
        {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

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
        if(filter.length>1) { searchView!!.setQuery(filter,false)
            searchView!!.isIconified=false
        }

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filter = query
                databaseViewModel.search(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                filter = newText
                return true
            }
        })
        searchView!!.setOnCloseListener {
            databaseViewModel.get(0)
            false
        }

        return true
    }

    //==============================================================================================
    //======================================= View =================================================
    fun setUpViewModel()
    {
        this.databaseViewModel = ViewModelProvider(this).get(DatabaseViewModel::class.java)
        this.selectionViewModel = ViewModelProvider(this).get(SelectionViewModel::class.java)
        this.networkViewModel = ViewModelProvider(this).get(NetworkViewModel::class.java)
        this.networkViewModel.init(application as TawkTo)
        this.databaseViewModel.init(application as TawkTo)
        this.selectionViewModel.selected!!.observe(this, Observer {
                if(it!=null)
                {
                    setDetailFragment(it)
                }


             }
        )
    }


    fun displayInitial()
    {
        if (shouldRequestPermission())
        {
            supportFragmentManager.beginTransaction().replace(R.id.fc_main,splash!!,TAG_SPLASH).commit()
        }
        else
        {
            showNavigation()
            setBottomNavigation()
            setVP()
        }
    }
    fun reloadInstance(savedInstanceState:Bundle?)
    {
        if(savedInstanceState!=null)
        {
            this.filter = when(savedInstanceState!!.getString(TAG_FILTER)==null)
            {
                true->""
                false->savedInstanceState!!.getString(TAG_FILTER)!!
            }
        }
    }
    fun setUpFragments(savedInstanceState:Bundle?)
    {
        if(savedInstanceState==null)
        {
            if(splash==null)
            {
                splash = SplashFragment()
            }
            if(vp == null)
            {
                vp = VPFragment()
            }
            if(df == null)
            {
                df = DetailFragment()
            }

        }
        else
        {
            if(getSupportFragmentManager()!!.findFragmentByTag(TAG_SPLASH)!=null)
            {
                splash = getSupportFragmentManager()!!.findFragmentByTag(TAG_SPLASH) as SplashFragment
            }
            else
            {
                splash = SplashFragment()
            }
            if(getSupportFragmentManager()!!.findFragmentByTag(TAG_VP)!=null)
            {
                vp = getSupportFragmentManager()!!.findFragmentByTag(TAG_VP) as VPFragment
            }
            else
            {
                vp = VPFragment()
            }
            if(getSupportFragmentManager()!!.findFragmentByTag(TAG_DF)!=null)
            {
                df = getSupportFragmentManager()!!.findFragmentByTag(TAG_DF) as DetailFragment
            }
            else
            {
                df = DetailFragment()
            }
        }



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
    fun setBottomNavigation()
    {
        menu = binding.bottomNav!!.menu
        vp!!.bottom_nav = binding.bottomNav
        binding.bottomNav!!.setOnNavigationItemSelectedListener {
            vp!!.selectItem(it.itemId)
            true
        }
        menu!!.add(Menu.NONE,0, Menu.NONE,"")
            .setIcon(R.drawable.list)
        menu!!.add(Menu.NONE,1, Menu.NONE,"")
            .setIcon(R.drawable.setting)
    }
    //==============================================================================================
    //====================================Display Fragment =========================================


    fun setDetailFragment(user: User)
    {

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if(this.searchView!=null)
        {
            this.searchView!!.visibility =View.GONE
        }

        df!!.updateUser(user)
        if(supportFragmentManager.findFragmentByTag(TAG_DF)==null)
        {
            supportFragmentManager.beginTransaction().add(R.id.fc_main, df!!,TAG_DF).commit()
        }
        else
        {
            supportFragmentManager.beginTransaction().show(df!!).commit()
            supportFragmentManager.beginTransaction().hide(vp!!).commit()
        }
        binding.toolbar.title = user.login
        databaseViewModel.getUser(user.id)
        this.binding.bottomNav!!.visibility = View.GONE
        this.binding.vDivider!!.visibility = View.GONE
    }

    fun setVP()
    {
        if(supportFragmentManager.findFragmentByTag(TAG_VP)==null)
        {
            supportFragmentManager.beginTransaction().add(R.id.fc_main, vp!!,TAG_VP).commit()
        }
        if(supportFragmentManager.findFragmentById(R.id.fc_main) is DetailFragment)
        {
            supportFragmentManager.beginTransaction().hide(df!!).commit()
            supportFragmentManager.beginTransaction().show(vp!!).commit()
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        binding.toolbar.visibility= View.VISIBLE
        binding.toolbar.title = "TawkTo"
        binding.vDivider!!.visibility= View.VISIBLE
        binding.bottomNav!!.visibility= View.VISIBLE
        if(searchView!=null)
        {
            searchView!!.visibility =View.VISIBLE
            if(filter!=null)
            {
                if(filter.length>0)
                {
                    searchView!!.setQuery(filter,false)
                }
            }

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
                    supportFragmentManager.beginTransaction().remove(splash!!).commit()
                    showNavigation()
                    setBottomNavigation()
                    setVP()
                }
            }
        }

    }
    //==============================================================================================

}