package com.example.swapi_test.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.swapi_test.R
import com.example.swapi_test.VolleyCallback
import com.example.swapi_test.api.Api
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {
    private lateinit var progressBar: ProgressBar
    private lateinit var progressResultText: TextView
    lateinit var requestQueue: RequestQueue

    lateinit var fragmentPlanets : PlanetsFragment
    lateinit var fragmentStatistics : StatisticsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Config progress bar and text progress result
        progressBar = findViewById(R.id.progress_bar)
        progressResultText = findViewById(R.id.progress_result_text)

        hideProgressBar()
        hideProgressResultText()

        fragmentPlanets = PlanetsFragment()
        fragmentStatistics = StatisticsFragment()

        requestQueue = Volley.newRequestQueue(applicationContext)

        if (savedInstanceState == null) {
            openFragment(fragmentPlanets, "planets", false)
        }

        //Add request finished listener to request queue
        requestQueue.addRequestFinishedListener(RequestQueue.RequestFinishedListener<String?>
        {
            requestQueue.cache.clear()
        })

        //Config bottom navigation view
        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener (onNavigationItemSelectedListener)
        supportFragmentManager.addOnBackStackChangedListener (this)
    }

    override fun onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    private fun shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        val canGoBack = supportFragmentManager.backStackEntryCount > 0
        supportActionBar!!.setDisplayHomeAsUpEnabled(canGoBack)
    }

    override fun onSupportNavigateUp(): Boolean {
        //This method is called when the up button is pressed. Just the pop back stack
        supportFragmentManager.popBackStack()
        return true
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()

        when (menuItem.itemId) {
            R.id.action_planets -> {
                openFragment(fragmentPlanets, "planets", false)
                hideFragment(fragmentStatistics)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_stats -> {
                hideFragment(fragmentPlanets)
                openFragment(fragmentStatistics, "stats", false)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun hideFragment(fragment : Fragment)
    {
        if (fragment.isVisible) {
                supportFragmentManager.beginTransaction().hide(fragment).commit()
            }
    }

    fun openFragment(fragment : Fragment, tag: String, is_to_back_stack : Boolean) {
        cancelAllRequestsWithTag("cancelable")

        //if fragment need to place to back stack
        if (is_to_back_stack) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_layout, fragment).addToBackStack(null).
                setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .show(fragment).commit()
        }
        else
        {//if fragment already added to fragment manager then open it
            if (supportFragmentManager.findFragmentByTag(tag) != null) {
                if (supportFragmentManager.findFragmentByTag(tag)!!.isAdded) {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .show(fragment).commit()
                }
            }
            else {//else add fragment to fragment manager and open it
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_layout, fragment, tag).
                    setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .show(fragment).commit()
            }
        }
    }

    fun showProgressBar()
    {
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
        }
    }

    fun hideProgressBar()
    {
        runOnUiThread {
            progressBar.visibility = View.GONE
        }
    }

    fun showProgressResultText(text: String)
    {
        runOnUiThread {
            progressResultText.visibility = View.VISIBLE
            progressResultText.text = text
        }
    }

    fun hideProgressResultText()
    {
        runOnUiThread {
            progressResultText.visibility = View.GONE
        }
    }

    fun addRequestToRequestQueue(resource : String, volleyCallback: VolleyCallback)
    {
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, Api.BASE_URL + resource,
            Response.Listener<String> {response ->
                volleyCallback.onSuccess(response)
            },
            Response.ErrorListener {
                showProgressResultText("Network error!")
                hideProgressBar()

                volleyCallback.onFailure(it.message.toString())
            })

        // Add the request to the RequestQueue.
        stringRequest.tag = "cancelable"
        requestQueue.add(stringRequest)
    }

    fun cancelAllRequestsWithTag(tag: String)
    {
        requestQueue.cancelAll(tag)
    }

    fun disableRefreshingOnSwipe(swipeRefreshLayout: SwipeRefreshLayout)
    {
        runOnUiThread {
            if (swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
