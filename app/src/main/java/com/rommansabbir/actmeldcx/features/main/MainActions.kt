package com.rommansabbir.actmeldcx.features.main

/**
 * This class represent all the business logic for the activity/fragment.
 * It's like a blueprint for all logic in a activity/fragment.
 */
interface MainActions{
    fun actionLoadURL(url : String)
    fun actionCapture()
    fun actionHistory()
}