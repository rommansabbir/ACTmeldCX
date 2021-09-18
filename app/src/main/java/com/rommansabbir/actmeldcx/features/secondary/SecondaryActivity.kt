package com.rommansabbir.actmeldcx.features.secondary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.rommansabbir.actmeldcx.R
import com.rommansabbir.actmeldcx.base.History
import com.rommansabbir.actmeldcx.base.extensions.doAfterTextChanged
import com.rommansabbir.actmeldcx.base.extensions.showMessage
import com.rommansabbir.actmeldcx.databinding.ActivitySecondaryBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SecondaryActivity : AppCompatActivity() {
    companion object {
        // Callback to notify regarding the browsing from the selected history
        private var onBrowseHistoryCallback: (history: History?) -> Unit =
            {}

        fun startActivity(activity: Activity, onLoadPageCallback: (history: History?) -> Unit) {
            this.onBrowseHistoryCallback = onLoadPageCallback
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

        //Handle custom back button action
        binding.asBtnBack.setOnClickListener { onBackPressed() }
        //Set the adapter to the recyclerview
        binding.asRvHistory.adapter = adapter

        //Manage the search history, if query is empty reset the adapter else show the matched history items
        binding.searchView.doAfterTextChanged { query ->
            if (query.isEmpty()) {
                actions.getHistoryList()
            } else {
                actions.searchHistory(query)
            }
        }

        //Get the history list initially
        actions.getHistoryList()
        //Handle history item delete action
        adapter.deleteCallback = { actions.deleteHistory(it) }
        //Handle browse history action
        adapter.browseCallback = { actions.browseHistory(it) }
    }

    //Manage all logical actions here
    private val actions: SecondaryActions = object : SecondaryActions {
        override fun getHistoryList() {
            vm.getHistoryList(
                { adapter.submitDataSet(it) },
                { showMessage(it.message.toString()) }
            )
        }

        override fun deleteHistory(history: History) {
            vm.deleteHistory(history,
                { adapter.submitDataSet(it.list) },
                { showMessage(it.message.toString()) })
        }

        override fun searchHistory(query: String) {
            vm.searchHistory(
                query, { adapter.submitDataSet(it) },
                { showMessage(it.message.toString()) }
            )
        }

        override fun browseHistory(history: History) {
            onBrowseHistoryCallback.invoke(history)
            onBackPressed()
        }
    }
}