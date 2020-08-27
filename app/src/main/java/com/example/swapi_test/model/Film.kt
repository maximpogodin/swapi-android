package com.example.swapi_test.model

data class Film(
    var characters: ArrayList<String>,
    val director: String,
    val episode_id: Int,
    var planets: ArrayList<String>,
    val producer: String,
    val release_date: String,
    var species: ArrayList<String>,
    var starships: ArrayList<String>,
    val title: String,
    var vehicles: ArrayList<String>
)