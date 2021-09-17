package com.rommansabbir.actmeldcx.features.secondary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.rommansabbir.actmeldcx.R
import com.rommansabbir.actmeldcx.base.History
import com.rommansabbir.actmeldcx.base.extensions.customize
import com.rommansabbir.actmeldcx.base.extensions.doAfterTextChanged
import com.rommansabbir.actmeldcx.base.extensions.showMessage
import com.rommansabbir.actmeldcx.databinding.ActivitySecondaryBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SecondaryActivity : AppCompatActivity() {
    companion object {
        private var onLoadPageCallback: (history: History?) -> Unit =
            {}

        fun startActivity(activity: Activity, onLoadPageCallback: (history: History?) -> Unit) {
            this.onLoadPageCallback = onLoadPageCallback
            activity.startActivity(Intent(activity, SecondaryActivity::class.java))
        }
    }

    private lateinit var binding: ActivitySecondaryBinding
    private val vm: SecondaryViewModel by viewModels()

    @Inject
    lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_secondary)
        binding.lifecycleOwner = this

        //
        binding.asBtnBack.setOnClickListener { onBackPressed() }
        binding.asRvHistory.adapter = adapter

        //
        binding.searchView.customize()
        binding.searchView.doAfterTextChanged { query ->
            if (query.isEmpty()) {
                getHistory()
            } else {
                vm.searchHistory(
                    query,
                    {
                        adapter.submitDataSet(it)
                    },
                    {
                        showMessage(it.message.toString())
                    }
                )
            }
        }

        //
        getHistory()
        adapter.deleteCallback = { history ->
            vm.deleteHistory(history,
                {
                    adapter.submitDataSet(it.list)
                },
                {
                    showMessage(it.message.toString())
                })
        }
        adapter.browseCallback = {
            onLoadPageCallback.invoke(it)
            onBackPressed()
        }
    }

    private fun getHistory() {
        vm.getHistoryList(
            {
                adapter.submitDataSet(it)
            },
            {
                showMessage(it.message.toString())
            }
        )
    }
}