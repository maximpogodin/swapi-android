package com.example.swapi_test.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.swapi_test.OnTouchSpinnerCallback
import com.example.swapi_test.R
import com.example.swapi_test.VolleyCallback
import com.example.swapi_test.adapter.FilmAdapter
import com.example.swapi_test.model.*
import com.google.gson.Gson

class StatisticsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var swipeRefreshStats : SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    lateinit var mainActivity: MainActivity
    private var gson = Gson()
    private var filmsArray = ArrayList<Film>()
    private lateinit var filmAdapter : FilmAdapter

    //pagination
    private var filmId : Int = 1
    private var offset : Int = 1
    private var limit : Int = 2
    private var totalItemsCount : Int = 0
    private var loading : Boolean = true

    lateinit var progressBar: ProgressBar

       override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.statistics_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = (activity as MainActivity)
        getFilmsCount()

        //Config swipe refresh layout
        swipeRefreshStats = view.findViewById(R.id.swipe_refresh_stats)
        swipeRefreshStats.setOnRefreshListener(this)

        progressBar = view.findViewById(R.id.load_more_progress_bar)
        progressBar.visibility = View.GONE

        recyclerView = view.findViewById(R.id.films_list)
        addScrollListener()

        refresh()
    }

    private fun getFilmsCount()
    {
        mainActivity.addRequestToRequestQueue("films/", object: VolleyCallback
        {
            override fun onSuccess(response: String) {
                val films = gson.fromJson(response, FilmsResult::class.java)
                totalItemsCount = films.count
            }
            override fun onFailure(error: String) {
                recyclerView.adapter = null
            }
        })
    }

    //region fetching
    fun fetchStats(id : Int) {
        val lastState = recyclerView.layoutManager!!.onSaveInstanceState()
        val context: Context = this.context ?: return

        //get response from request
        mainActivity.addRequestToRequestQueue("films/${id}/", object : VolleyCallback {
            override fun onSuccess(response: String) {
                val film = gson.fromJson(response, Film::class.java)
                filmsArray.add(film)

                filmAdapter = FilmAdapter(context, filmsArray, object : OnTouchSpinnerCallback {
                    override fun onTouchListener(spinner: Spinner, resource: String) {
                        val tag = spinner.tag.toString()
                        val position = tag.toInt()

                        if (spinner.getItemAtPosition(0) == "Load the list") {
                            when (resource) {
                                "people" -> fetchFromResource(filmsArray[position].characters, position, resource, spinner)
                                "planets" -> fetchFromResource(filmsArray[position].planets, position, resource, spinner)
                                "species" -> fetchFromResource(filmsArray[position].species, position, resource, spinner)
                                "starships" -> fetchFromResource(filmsArray[position].starships, position, resource, spinner)
                                "vehicles" -> fetchFromResource(filmsArray[position].vehicles, position, resource, spinner)
                            }
                        }
                    }
                })

                mainActivity.runOnUiThread {
                    recyclerView.adapter = filmAdapter
                    filmAdapter.notifyDataSetChanged()
                }

                recyclerView.layoutManager!!.onRestoreInstanceState(lastState)

                mainActivity.hideProgressBar()
                mainActivity.hideProgressResultText()

                mainActivity.disableRefreshingOnSwipe(swipeRefreshStats)

                loading = true
                progressBar.visibility = View.GONE
            }

            override fun onFailure(error: String) {
                recyclerView.adapter = null

                mainActivity.disableRefreshingOnSwipe(swipeRefreshStats)
            }
        })

        filmId++
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
                            "vehicles" -> {
                                val vehicle = gson.fromJson(response, Vehicle::class.java)
                                _array.add(vehicle.name)
                            }
                        }

                        filmAdapter.add(id, _array, resource)

                        mainActivity.runOnUiThread {
                            filmAdapter.setSpinnerAdapter(spinner, _array)
                            filmAdapter.notifyDataSetChanged()
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

    private fun addScrollListener()
    {
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    if (loading && (filmId <= totalItemsCount)) {
                        loading = false
                        progressBar.visibility = View.VISIBLE
                        fetchStats(filmId)
                    }
                }
            }
        }

        recyclerView.addOnScrollListener(scrollListener)
    }

    fun refresh()
    {
        filmId = 1
        limit = 2

        filmsArray.clear()
        recyclerView.adapter = null

        mainActivity.showProgressBar()
        mainActivity.hideProgressResultText()

        fetchStats(filmId)
    }

    override fun onRefresh() {
        refresh()
    }
}