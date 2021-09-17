package com.rommansabbir.actmeldcx.base.extensions

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

fun String.isValidURL(): Boolean {
    return Patterns.WEB_URL.matcher(this).matches()
}

fun Activity.showMessage(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun ioScope(ioScope: (CoroutineScope) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        ioScope.invoke(this)
    }
}

fun mainScope(mainScope: (CoroutineScope) -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        mainScope.invoke(this)
    }
}

var handlerDelayTimer: Timer = Timer()

inline fun handlerPost(crossinline onSuccess: () -> Unit) {
    Handler(Looper.getMainLooper()).post {
        onSuccess.invoke()
    }
}

inline fun handlerPostDelayed(delay: Long, crossinline onSuccess: () -> Unit) {
    handlerDelayTimer.cancel()
    handlerDelayTimer = Timer()
    handlerDelayTimer.schedule(object : TimerTask() {
        override fun run() {
            Handler(Looper.getMainLooper()).post {
                onSuccess.invoke()
            }
        }
    }, delay)
}