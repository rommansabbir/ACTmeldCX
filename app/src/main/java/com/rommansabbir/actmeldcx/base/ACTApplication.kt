package com.rommansabbir.actmeldcx.base

import android.app.Application
import com.rommansabbir.storex.StoreXCore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ACTApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        StoreXCore.init(this, "ACTApp").setEncryptionKey("~!@#$%^%^&&**()_++_()")
    }
}