package com.rommansabbir.actmeldcx.base.extensions

import android.Manifest
import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import javax.inject.Inject


class PermissionManagerImpl @Inject constructor() : PermissionManager {
    override fun withStoragePermission(
        activity: Activity,
        onGranted: () -> Unit,
        onDenied: (msg: String) -> Unit
    ) {
        Dexter.withContext(activity)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    onGranted.invoke()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    onDenied.invoke("Storage Write permission denied!")
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }
}

interface PermissionManager {
    fun withStoragePermission(
        activity: Activity,
        onGranted: () -> Unit,
        onDenied: (msg: String) -> Unit
    )
}