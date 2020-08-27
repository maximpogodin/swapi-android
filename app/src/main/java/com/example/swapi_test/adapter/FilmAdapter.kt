package com.example.swapi_test.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi_test.OnTouchSpinnerCallback
import com.example.swapi_test.R
import com.example.swapi_test.model.Film
import kotlin.collections.ArrayList

class FilmAdapter (var context: Context, var filmsList: ArrayList<Film>, val callback: OnTouchSpinnerCallback) : RecyclerView.Adapter<FilmAdapter.ViewHolder>(),
    View.OnTouchListener {
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view)
    {
        var textTitleAndId: TextView = view.findViewById(R.id.title_and_id) as TextView
        var textDirector: TextView = view.findViewById(R.id.director) as TextView
        var textProducer: TextView = view.findViewById(R.id.producer) as TextView
        var textReleaseDate: TextView = view.findViewById(R.id.release_date) as TextView
        var spinnerCharacters: Spinner = view.findViewById(R.id.characters_spinner) as Spinner
        var spinnerPlanets: Spinner = view.findViewById(R.id.planets_spinner) as Spinner
        var spinnerSpecies: Spinner = view.findViewById(R.id.species_spinner) as Spinner
        var spinnerStarships: Spinner = view.findViewById(R.id.starships_spinner) as Spinner
        var spinnerVehicles: Spinner = view.findViewById(R.id.vehicles_spinner) as Spinner
    }

    override fun getItemCount() = filmsList.size

    //add or update list in film object
    fun add(position: Int, _array: ArrayList<String>, resource: String)
    {
        when (resource)
        {
            "people" -> filmsList[position].characters = _array
            "planets" -> filmsList[position].planets = _array
            "species" -> filmsList[position].species = _array
            "starships" -> filmsList[position].starships = _array
            "vehicles" -> filmsList[position].vehicles = _array
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val film = filmsList[position]

        viewHolder.textTitleAndId.text = "Episode " + film.episode_id + ": " + film.title
        viewHolder.textDirector.text = "Director: " + film.director
        viewHolder.textProducer.text = "Producer: " + film.producer
        viewHolder.textReleaseDate.text = "Release date: " + film.release_date

        //Spinners
        //Planets
        viewHolder.spinnerPlanets.setOnTouchListener(this)
        viewHolder.spinnerPlanets.tag = position
        viewHolder.spinnerPlanets.prompt = "List of planets"
        if (film.planets.size > 0)
        {
            if (film.planets[0].contains("http"))
                setSpinnerAdapter(viewHolder.spinnerPlanets, listOf("Load the list"))
            else
                setSpinnerAdapter(viewHolder.spinnerPlanets, film.planets)
        }
        else setSpinnerAdapter(viewHolder.spinnerPlanets, listOf("List is empty"))

        //People
        viewHolder.spinnerCharacters.setOnTouchListener(this)
        viewHolder.spinnerCharacters.tag = position
        viewHolder.spinnerCharacters.prompt = "List of characters"
        if (film.characters.size > 0)
        {
            if (film.characters[0].contains("http"))
                setSpinnerAdapter(viewHolder.spinnerCharacters, listOf("Load the list"))
            else
                setSpinnerAdapter(viewHolder.spinnerCharacters, film.characters)
        }
        else setSpinnerAdapter(viewHolder.spinnerCharacters, listOf("List is empty"))

        //Starships
        viewHolder.spinnerStarships.setOnTouchListener(this)
        viewHolder.spinnerStarships.tag = position
        viewHolder.spinnerStarships.prompt = "List of starships"
        if (film.starships.size > 0)
        {
            if (film.starships[0].contains("http"))
                setSpinnerAdapter(viewHolder.spinnerStarships, listOf("Load the list"))
            else
                setSpinnerAdapter(viewHolder.spinnerStarships, film.starships)
        }
        else setSpinnerAdapter(viewHolder.spinnerStarships, listOf("List is empty"))

        //Species
        viewHolder.spinnerSpecies.setOnTouchListener(this)
        viewHolder.spinnerSpecies.tag = position
        viewHolder.spinnerSpecies.prompt = "List of species"
        if (film.species.size > 0)
        {
            if (film.species[0].contains("http"))
                setSpinnerAdapter(viewHolder.spinnerSpecies, listOf("Load the list"))
            else
                setSpinnerAdapter(viewHolder.spinnerSpecies, film.species)
        }
        else setSpinnerAdapter(viewHolder.spinnerSpecies, listOf("List is empty"))

        //Vehicles
        viewHolder.spinnerVehicles.setOnTouchListener(this)
        viewHolder.spinnerVehicles.tag = position
        viewHolder.spinnerVehicles.prompt = "List of vehicles"
        if (film.vehicles.size > 0)
        {
            if (film.vehicles[0].contains("http"))
                setSpinnerAdapter(viewHolder.spinnerVehicles, listOf("Load the list"))
            else
                setSpinnerAdapter(viewHolder.spinnerVehicles, film.vehicles)
        }
        else setSpinnerAdapter(viewHolder.spinnerVehicles, listOf("List is empty"))
    }

    //set adapter for selected spinner
    fun setSpinnerAdapter(spinner: Spinner, list: List<String>) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val spinner = view as Spinner

        if (event.action == MotionEvent.ACTION_UP) {
            when (spinner.id) {
                R.id.characters_spinner -> callback.onTouchListener(spinner, "people")
                R.id.planets_spinner -> callback.onTouchListener(spinner, "planets")
                R.id.species_spinner -> callback.onTouchListener(spinner, "species")
                R.id.starships_spinner -> callback.onTouchListener(spinner, "starships")
                R.id.vehicles_spinner -> callback.onTouchListener(spinner, "vehicles")
            }
        }

        val header = spinner.getItemAtPosition(0).toString()
        return header == "Load the list"
    }
}