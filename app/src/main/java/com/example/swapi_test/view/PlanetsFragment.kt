package com.example.swapi_test.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.swapi_test.PlanetDetailsFragment
import com.example.swapi_test.R
import com.example.swapi_test.VolleyCallback
import com.example.swapi_test.adapter.PlanetAdapter
import com.example.swapi_test.model.Planet
import com.example.swapi_test.model.PlanetsResult
import com.google.gson.Gson

class PlanetsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var swipeRefreshPlanets : SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    lateinit var mainActivity: MainActivity
    private var gson = Gson()

    //pagination
    private var planetId : Int = 1
    private var offset : Int = 10
    private var limit : Int = 11
    private var totalItemsCount : Int = 0
    private var loading : Boolean = true

    private lateinit var planetAdapter : PlanetAdapter
    private var planetsArray = ArrayList<Planet>()

    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_planets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = (activity as MainActivity)
        getPlanetsCount()

        progressBar = view.findViewById(R.id.load_more_progress_bar)
        progressBar.visibility = View.GONE

        //Config swipe refresh layout
        swipeRefreshPlanets = view.findViewById(R.id.swipe_refresh_planets)
        swipeRefreshPlanets.setOnRefreshListener(this)

        recyclerView = view.findViewById(R.id.planets_list)
        addScrollListener()

        mainActivity.showProgressBar()

        refresh()
    }

    private fun getPlanetsCount()
    {
        mainActivity.addRequestToRequestQueue("planets/", object: VolleyCallback
        {
            override fun onSuccess(response: String) {
                val planets = gson.fromJson(response, PlanetsResult::class.java)
                totalItemsCount = planets.count
            }
            override fun onFailure(error: String) {
                recyclerView.adapter = null
            }
        })
    }

    private fun fetchPlanets() {
        val lastState = recyclerView.layoutManager!!.onSaveInstanceState()

        for (id in planetId until limit) {
            //get response from request
            mainActivity.addRequestToRequestQueue("planets/$id/", object : VolleyCallback {
                override fun onSuccess(response: String) {
                    val planet = gson.fromJson(response, Planet::class.java)
                    planetsArray.add(planet)

                    planetAdapter =
                        PlanetAdapter(planetsArray, object : PlanetAdapter.Callback {
                            override fun onItemClicked(planet: Planet) {
                                mainActivity.openFragment(
                                    PlanetDetailsFragment(planet.name), "details", true)
                            }
                        })

                    mainActivity.runOnUiThread {
                        recyclerView.adapter = planetAdapter
                        planetAdapter.notifyDataSetChanged()
                    }

                    recyclerView.layoutManager!!.onRestoreInstanceState(lastState)

                    mainActivity.hideProgressBar()
                    mainActivity.hideProgressResultText()

                    mainActivity.disableRefreshingOnSwipe(swipeRefreshPlanets)

                    loading = true
                    progressBar.visibility = View.GONE
                }

                override fun onFailure(error: String) {
                    recyclerView.adapter = null

                    mainActivity.disableRefreshingOnSwipe(swipeRefreshPlanets)
                }
            })
        }

        planetId = limit
        limit += offset
    }

    private fun addScrollListener()
    {
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    if (loading && (planetId <= totalItemsCount)) {
                        loading = false
                        progressBar.visibility = View.VISIBLE
                        fetchPlanets()
                    }
                }
            }
        }

        recyclerView.addOnScrollListener(scrollListener)
    }

    private fun refresh()
    {
        planetId = 1
        limit = 11

        planetsArray.clear()
        recyclerView.adapter = null

        planetId = 1
        limit = 11

        mainActivity.showProgressBar()
        mainActivity.hideProgressResultText()

        fetchPlanets()
    }

    override fun onRefresh() {
        refresh()
    }
}