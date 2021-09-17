package com.rommansabbir.actmeldcx.base

import com.rommansabbir.storex.StoreAbleObject

data class HistoryCacheObject(val list: MutableList<History>) : StoreAbleObject()

data class History(val imagePath: String, val url: String, val dateTime: String)