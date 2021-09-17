package com.rommansabbir.actmeldcx.features.secondary

import androidx.lifecycle.ViewModel
import com.rommansabbir.actmeldcx.base.History
import com.rommansabbir.actmeldcx.base.HistoryCacheObject
import com.rommansabbir.actmeldcx.usecase.DeleteNewHistoryUseCase
import com.rommansabbir.actmeldcx.usecase.GetHistoryUseCase
import com.rommansabbir.actmeldcx.usecase.SearchHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SecondaryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val deleteNewHistoryUseCase: DeleteNewHistoryUseCase,
    private val searchHistoryUseCase: SearchHistoryUseCase,
) :
    ViewModel() {
    fun getHistoryList(
        onSuccess: (MutableList<History>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        GetHistoryUseCase.executor(getHistoryUseCase, onSuccess, onError)
    }

    fun deleteHistory(
        params: History,
        onSuccess: (HistoryCacheObject) -> Unit,
        onError: (Exception) -> Unit
    ) {
        DeleteNewHistoryUseCase.executor(
            deleteNewHistoryUseCase,
            params, onSuccess, onError
        )
    }

    fun searchHistory(
        query: String,
        onSuccess: (MutableList<History>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        SearchHistoryUseCase.executor(
            searchHistoryUseCase,
            query, onSuccess, onError
        )
    }
}