package com.rommansabbir.actmeldcx.base

import com.rommansabbir.storex.StoreAbleObject


/**
 * Object that will be used to cache the list of [History]
 */
data class HistoryCacheObject(val list: MutableList<History>) : StoreAbleObject()


/**
 * Object that represent a complete history including image, url & timestamp
 */
data class History(val imagePath: String, val url: String, val dateTime: String)