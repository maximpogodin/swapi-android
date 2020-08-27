package com.example.swapi_test.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi_test.OnTouchSpinnerCallback
import com.example.swapi_test.R
import com.example.swapi_test.model.Planet

class PlanetDetailsAdapter (var context: Context, var planetsList: ArrayList<Planet>,
                            val callback: OnTouchSpinnerCallback) :
    RecyclerView.Adapter<PlanetDetailsAdapter.ViewHolder>(), View.OnTouchListener {
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view)
    {
        var textName : TextView = view.findViewById(R.id.planet_name) as TextView
        var textRotationPeriod : TextView = view.findViewById(R.id.rotation_period) as TextView
        var textOrbitalPeriod : TextView = view.findViewById(R.id.orbital_period) as TextView
        var textDiameter : TextView = view.findViewById(R.id.diameter) as TextView
        var textClimate : TextView = view.findViewById(R.id.climate) as TextView
        var textGravity : TextView = view.findViewById(R.id.gravity) as TextView
        var textTerrain : TextView = view.findViewById(R.id.terrain) as TextView
        var textSurfaceWater : TextView = view.findViewById(R.id.surface_water) as TextView
        var textPopulation : TextView = view.findViewById(R.id.population) as TextView
        var filmsSpinner : Spinner = view.findViewById(R.id.films_spinner) as Spinner
        var residentsSpinner : Spinner = view.findViewById(R.id.people_spinner) as Spinner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_planet_details, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = planetsList.size

    //add or update list in film object
    fun add(position: Int, _array: ArrayList<String>, resource: String)
    {
        when (resource)
        {
            "people" -> planetsList[position].residents = _array
            "films" -> planetsList[position].films = _array
        }
    }

    //set adapter for selected spinner
    fun setSpinnerAdapter(spinner: Spinner, list: List<String>)
    {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val planet = planetsList[position]

        viewHolder.textName.text = planet.name
        viewHolder.textRotationPeriod.text = "Rotation period: " + planet.rotation_period
        viewHolder.textOrbitalPeriod.text = "Orbital period: " + planet.orbital_period
        viewHolder.textDiameter.text = "Diameter: " + planet.diameter
        viewHolder.textClimate.text = "Climate: " + planet.climate
        viewHolder.textGravity.text = "Gravity: " + planet.gravity
        viewHolder.textTerrain.text = "Terrain: " + planet.terrain
        viewHolder.textSurfaceWater.text = "Surface water: " + planet.surface_water
        viewHolder.textPopulation.text = "Population: " + planet.population

        viewHolder.filmsSpinner.setOnTouchListener(this)
        viewHolder.filmsSpinner.prompt = "List of films"
        if (planet.films.size > 0)
        {
            if (planet.films[0].contains("http"))
                setSpinnerAdapter(viewHolder.filmsSpinner, listOf("Load the list"))
            else setSpinnerAdapter(viewHolder.filmsSpinner, planet.films)
        }
        else setSpinnerAdapter(viewHolder.residentsSpinner, listOf("List is empty"))

        viewHolder.residentsSpinner.setOnTouchListener(this)
        viewHolder.residentsSpinner.prompt = "List of residents"
        if (planet.residents.size > 0)
        {
            if (planet.residents[0].contains("http"))
                setSpinnerAdapter(viewHolder.residentsSpinner, listOf("Load the list"))
            else setSpinnerAdapter(viewHolder.residentsSpinner, planet.residents)
        }
        else setSpinnerAdapter(viewHolder.residentsSpinner, listOf("List is empty"))
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val spinner = view as Spinner

        if (event.action == MotionEvent.ACTION_UP) {

            when (spinner.id) {
                R.id.people_spinner -> callback.onTouchListener(spinner, "people")
                R.id.films_spinner -> callback.onTouchListener(spinner, "films")
            }
        }

        val header = spinner.getItemAtPosition(0).toString()
        return header == "Load the list"
    }
}