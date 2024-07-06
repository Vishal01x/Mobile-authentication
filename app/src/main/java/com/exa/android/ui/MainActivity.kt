package com.exa.android.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.exa.android.ui.Utils.Constants
import com.exa.android.ui.Utils.Constants.currentFragment
import com.exa.android.ui.databinding.ActivityMainBinding
import com.exa.android.ui.fragment.signIn

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isInternet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check internet availability on launch
        if (isInternetAvailable(this)) {
            binding.noInternetImg.visibility = View.GONE
            if (savedInstanceState == null) {
                loadFragment(signIn())
            }
        } else {
            showLoader()
        }

        // Register network callback to monitor internet availability
        registerNetworkCallback()
    }

    private fun registerNetworkCallback() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    runOnUiThread {
                        if (!isInternet) {
                            isInternet = true
                            hideLoader()

                        }
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    runOnUiThread {
                        if (isInternet) {
                            isInternet = false
                            showLoader()
                        }
                    }
                }
            })
        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        // Save the current fragment tag or any state
//        currentFragmentTag = supportFragmentManager.findFragmentById(binding.container.id)?.javaClass?.name
//        outState.putString("currentFragmentTag", currentFragmentTag)
//        Log.d("MainActivity", "onSaveInstanceState: $currentFragmentTag")
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        // Restore the current fragment tag or any state
//        currentFragmentTag = savedInstanceState.getString("currentFragmentTag")
//        if (currentFragmentTag != null) {
//            val fragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, currentFragmentTag!!)
//            supportFragmentManager.beginTransaction()
//                .replace(binding.container.id, fragment)
//                .commit()
//        }
//        Log.d("MainActivity", "onRestoreInstanceState: $currentFragmentTag")
//    }

    private fun showLoader() {
        binding.container.visibility = View.GONE
        binding.noInternetImg.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.container.visibility = View.VISIBLE
        binding.noInternetImg.visibility = View.GONE
        val fragment = supportFragmentManager.findFragmentByTag(currentFragment)
        if (fragment != null) {
            loadFragment(fragment)
        }
    }

//    private fun isInternetAvailable(context: Context): Boolean {
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = connectivityManager.activeNetwork
//        return activeNetwork != null
//    }

    private fun loadFragment(fragment: Fragment) {

        Constants.currentFragment = fragment.javaClass.name
        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, fragment,Constants.currentFragment)
            .commit()
    }

//    private fun restoreCurrentFragment() {
//        currentFragmentTag?.let { tag ->
//            val fragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, tag)
//            supportFragmentManager.beginTransaction()
//                .replace(binding.container.id, fragment, tag)
//                .commit()
//        }
//    }
}
