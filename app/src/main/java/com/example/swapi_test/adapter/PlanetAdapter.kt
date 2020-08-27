package com.example.swapi_test.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi_test.R
import com.example.swapi_test.model.Planet

class PlanetAdapter (var planetsList: ArrayList<Planet>, val callback: Callback) : RecyclerView.Adapter<PlanetAdapter.ViewHolder>() {
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view)
    {
        var textName: TextView = view.findViewById(R.id.planet_name_text) as TextView
        var cardView: CardView = view.findViewById(R.id.card_view) as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_planet, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount() = planetsList.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textName.text = planetsList[position].name

        viewHolder.cardView.setOnClickListener { callback.onItemClicked(planetsList[position]) }
    }

    interface Callback {
        fun onItemClicked(planet : Planet)
    }
}