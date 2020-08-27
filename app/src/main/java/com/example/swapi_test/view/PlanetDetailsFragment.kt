package com.example.swapi_test

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.swapi_test.adapter.PlanetDetailsAdapter
import com.example.swapi_test.model.*
import com.example.swapi_test.view.MainActivity
import com.google.gson.Gson

class PlanetDetailsFragment constructor (_planetName: String) : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    lateinit var recyclerView: RecyclerView
    lateinit var mainActivity: MainActivity

    private var gson = Gson()

    private val planetName: String = _planetName
    private var planetList = ArrayList<Planet>()
    private lateinit var planetDetailsAdapter : PlanetDetailsAdapter

    private lateinit var swipeRefreshDetails : SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_planet_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = (activity as MainActivity)

        recyclerView = view.findViewById(R.id.planet_details_list)

        //Config swipe refresh layout
        swipeRefreshDetails = view.findViewById(R.id.swipe_refresh_details)
        swipeRefreshDetails.setOnRefreshListener (this)

        refresh()
    }

    //region fetching
    fun fetchPlanetDetails()
    {
        val context: Context = this.context ?: return

        //get response from request
        mainActivity.addRequestToRequestQueue("planets/?search=" + planetName, object: VolleyCallback {
            override fun onSuccess(response: String)
            {
                val planetResultSearch = gson.fromJson(response, PlanetsResult::class.java)

                //get planet from search result
                val planet = planetResultSearch.results[0]
                planetList.add(planet)

                planetDetailsAdapter = PlanetDetailsAdapter(context, planetList, object : OnTouchSpinnerCallback {
                    override fun onTouchListener(spinner: Spinner, resource: String) {
                        if (spinner.getItemAtPosition(0) == "Load the list") {
                            when (resource) {
                                "people" -> fetchFromResource(planetList[0].residents, 0, resource, spinner)
                                "films" -> fetchFromResource(planetList[0].films, 0, resource, spinner)
                            }
                        }
                    }
                })

                mainActivity.runOnUiThread {
                    recyclerView.adapter = planetDetailsAdapter
                    planetDetailsAdapter.notifyDataSetChanged()
                }

                mainActivity.hideProgressBar()
                mainActivity.hideProgressResultText()

                mainActivity.disableRefreshingOnSwipe(swipeRefreshDetails)
            }

            override fun onFailure(error: String) {
                recyclerView.adapter = null

                mainActivity.disableRefreshingOnSwipe(swipeRefreshDetails)
            }
        })
    }

    fun fetchFromResource(_arrayURL : ArrayList<String>, id : Int, resource: String, spinner: Spinner)
    {
        val handler = Handler()
        mainActivity.showProgressBar()
        val _array = ArrayList<String>()

        for (_url in _arrayURL) {
            var url = _url
            url = url.removeRange(0, url.indexOf(resource))

            mainActivity.addRequestToRequestQueue(
                url,
                object : VolleyCallback {
                    override fun onSuccess(response: String) {
                        when (resource)
                        {
                            "planets" -> {
                                val planet = gson.fromJson(response, Planet::class.java)
                                _array.add(planet.name)
                            }
                            "people" -> {
                                val character = gson.fromJson(response, People::class.java)
                                _array.add(character.name)
                            }
                            "starships" -> {
                                val starship = gson.fromJson(response, Starship::class.java)
                                _array.add(starship.name)
                            }
                            "species" -> {
                                val spec = gson.fromJson(response, Spec::class.java)
                                _array.add(spec.name)
                            }
                            "films" -> {
                                val film = gson.fromJson(response, Film::class.java)
                                _array.add(film.title)
                            }
                        }

                        planetDetailsAdapter.add(id, _array, resource)

                        mainActivity.runOnUiThread {
                            planetDetailsAdapter.setSpinnerAdapter(spinner, _array)
                            planetDetailsAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onFailure(error: String) {
                        recyclerView.adapter = null
                    }
                })
        }

        handler.postDelayed(
            {
                if (isVisible)
                    spinner.performClick()
                else
                    handler.removeCallbacksAndMessages(null)

                mainActivity.hideProgressBar()
            },
            1000 // value in milliseconds
        )
    }

    private fun refresh()
    {
        recyclerView.adapter = null
        planetList.clear()

        mainActivity.showProgressBar()
        mainActivity.hideProgressResultText()

        fetchPlanetDetails()
    }

    override fun onRefresh() {
        refresh()
    }
}