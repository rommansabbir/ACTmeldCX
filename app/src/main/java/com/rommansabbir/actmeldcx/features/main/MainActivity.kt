package com.rommansabbir.actmeldcx.features.main

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.WebView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.rommansabbir.actmeldcx.R
import com.rommansabbir.actmeldcx.base.History
import com.rommansabbir.actmeldcx.base.extensions.*
import com.rommansabbir.actmeldcx.databinding.ActivityMainBinding
import com.rommansabbir.actmeldcx.features.secondary.SecondaryActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileOutputStream
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val vm: MainViewModel by viewModels()
    private val webViewClient = AppWebViewClient()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = vm
        binding.lifecycleOwner = this
        binding.amIvWeb.visibility = View.GONE
        binding.amPbLoading.visibility = View.GONE
        binding.amPbLoading.isIndeterminate = true

        //
        val settings = binding.amWebView.settings
        binding.amWebView.webViewClient = webViewClient
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        //
        observeMainUI()

        //
        webViewClient.onURLUpdated = {
            binding.amInputUrl.setText(it)
        }
        webViewClient.onPageLoaded = {
            binding.amIvWeb.visibility = View.GONE
            binding.amWebView.visibility = View.VISIBLE
        }
        webViewClient.onShowLoading = { showLoading ->
            binding.amPbLoading.visibility = if (showLoading) View.VISIBLE else View.GONE
        }
        actions.actionLoadURL(getString(R.string.url))
    }

    private fun observeMainUI() {
        vm.ui.observe(this) {
            it?.apply {
                if (actionGo) {
                    vm.setActionGo(false)
                    actions.actionLoadURL(binding.amInputUrl.text.toString())
                }
                if (actionHistory) {
                    vm.setActionHistory(false)
                    actions.actionHistory()
                }
                if (actionCapture) {
                    vm.setActionCapture(false)
                    actions.actionCapture()
                }
            }
        }
    }

    private val actions: MainActions = object : MainActions {
        override fun actionLoadURL(url: String) {
            when (url.isValidURL()) {
                true -> {
                    binding.amWebView.loadUrl(url)
                }
                else -> {
                    showMessage("Invalid URL")
                }
            }
        }

        override fun actionCapture() {
            vm.permissionManager.withStoragePermission(this@MainActivity,
                {
                    try {
                        if (webViewClient.isPageLoaded) {
                            webViewClient.webView?.let { view ->
                                mainScope {
                                    try {
                                        val image = getBitmapOfWebView(view)
                                        if (image == null) {
                                            showMessage("File null")
                                        } else {
                                            val timeStamp = Calendar.getInstance().time.time.toString()
                                            val fileExt = "${timeStamp}.png"
                                            val fileName =
                                                Environment.getExternalStorageDirectory().path + "/${fileExt}"
                                            val fos = FileOutputStream(fileName)
                                            image.compress(Bitmap.CompressFormat.JPEG, 70, fos)
                                            fos.close()

                                            //
                                            vm.saveHistory(History(
                                                fileName,
                                                webViewClient.currentURL,
                                                timeStamp
                                            ),
                                                {
                                                    showMessage("Saved to History List")
                                                },
                                                {e ->
                                                    showMessage(e.message ?: "")
                                                })
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        showMessage(e.message.toString())
                                    }
                                }
                            }
                        } else {
                            showMessage("Webpage didn't load properly yet. Try again after loaded.")
                        }
                    }
                    catch (e : Exception){
                        e.printStackTrace()
                    }
                },
                { showMessage(it) })
        }

        override fun actionHistory() {
            SecondaryActivity.startActivity(this@MainActivity) {
                if (it == null) {
                    // Ignore
                } else {
                    binding.amIvWeb.visibility = View.VISIBLE
                    binding.amIvWeb.setImagePath(it.imagePath)
                    binding.amWebView.visibility = View.GONE
                    binding.amWebView.loadUrl(it.url)
                }
            }
        }

    }

    private fun getBitmapOfWebView(webView: WebView): Bitmap? {
        val picture = webView.capturePicture()
        val b = Bitmap.createBitmap(picture.width, picture.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        picture.draw(c)
        return b
    }
}