package com.example.swapi_test.model

data class FilmsResult(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: ArrayList<Film>
)