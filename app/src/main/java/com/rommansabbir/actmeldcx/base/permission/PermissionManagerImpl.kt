package com.rommansabbir.actmeldcx.base.permission

import android.Manifest
import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
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


/**
 * [PermissionManager] is to manage runtime permission across the application which uses
 * [Dexter] under the hood to manage all permissions stuffs.
 *
 */
interface PermissionManager {
    /**
     * Ask for storage permissions from the user and notify using callbacks
     *
     * @param activity, [Activity]
     * @param onGranted, Callback for granted state
     * @param onDenied, Callback for denied state
     */
    fun withStoragePermission(
        activity: Activity,
        onGranted: () -> Unit,
        onDenied: (msg: String) -> Unit
    )
}