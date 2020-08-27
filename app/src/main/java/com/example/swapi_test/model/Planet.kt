package com.example.swapi_test.model

import java.io.Serializable

//класс для хранения данных
data class Planet(
    val climate: String,
    val created: String,
    val diameter: String,
    val edited: String,
    var films: ArrayList<String>,
    val gravity: String,
    val name: String,
    val orbital_period: String,
    val population: String,
    var residents: ArrayList<String>,
    val rotation_period: String,
    val surface_water: String,
    val terrain: String,
    val url: String
) : Serializable