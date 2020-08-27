package com.example.swapi_test.model

//класс для хранения данных
data class PlanetsResult(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: ArrayList<Planet>
)