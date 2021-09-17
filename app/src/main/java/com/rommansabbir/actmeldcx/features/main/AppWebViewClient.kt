package com.rommansabbir.actmeldcx.features.main

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient

class AppWebViewClient :
    WebViewClient() {
    internal var onURLUpdated: (url: String) -> Unit = {}
    internal var onPageLoaded: () -> Unit = {}
    internal var isPageLoaded: Boolean = false
    internal var webView: WebView? = null
    internal var currentURL: String = ""

    internal var onShowLoading: (showLoading: Boolean) -> Unit = {}

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        this.webView = view
        onURLUpdated.invoke(url)
        this.currentURL = url
        this.isPageLoaded = false
        return true
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        this.webView = view
        this.currentURL = url
        this.isPageLoaded = false
        onURLUpdated.invoke(url)
        this.onShowLoading.invoke(true)
    }

    override fun onPageFinished(view: WebView?, url: String) {
        super.onPageFinished(view, url)
        this.webView = view
        this.currentURL = url
        this.isPageLoaded = true
        this.onPageLoaded.invoke()
        onURLUpdated.invoke(url)
        this.onShowLoading.invoke(false)
    }
}