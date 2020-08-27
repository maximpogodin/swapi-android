package com.example.swapi_test

interface VolleyCallback {
    fun onSuccess(result: String)
    fun onFailure(error: String)
}