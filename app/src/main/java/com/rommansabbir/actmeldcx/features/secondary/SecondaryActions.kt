package com.rommansabbir.actmeldcx.features.secondary

import com.rommansabbir.actmeldcx.base.History

/**
 * This class represent all the business logic for the activity/fragment.
 * It's like a blueprint for all logic in a activity/fragment.
 */
interface SecondaryActions {
    fun getHistoryList()
    fun deleteHistory(history : History)
    fun searchHistory(query : String)
    fun browseHistory(history: History)
}