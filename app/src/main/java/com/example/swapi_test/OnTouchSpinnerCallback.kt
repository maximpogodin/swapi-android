package com.example.swapi_test

import android.widget.Spinner

interface OnTouchSpinnerCallback {
    fun onTouchListener(spinner : Spinner, resource: String)
}