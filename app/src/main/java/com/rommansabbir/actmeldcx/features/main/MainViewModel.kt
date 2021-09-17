package com.rommansabbir.actmeldcx.features.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rommansabbir.actmeldcx.base.History
import com.rommansabbir.actmeldcx.base.HistoryCacheObject
import com.rommansabbir.actmeldcx.base.extensions.PermissionManager
import com.rommansabbir.actmeldcx.usecase.SaveNewHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    internal val permissionManager: PermissionManager,
    private val saveNewHistoryUseCase: SaveNewHistoryUseCase
) : ViewModel() {
    private var _ui: MutableLiveData<MainUI> = MutableLiveData(MainUI())
    val ui: LiveData<MainUI>
        get() = _ui

    fun setActionGo(value: Boolean) {
        _ui.value?.apply {
            actionGo = value
            _ui.value = this
        }
    }

    fun setActionCapture(value: Boolean) {
        _ui.value?.apply {
            actionCapture = value
            _ui.value = this
        }
    }

    fun setActionHistory(value: Boolean) {
        _ui.value?.apply {
            actionHistory = value
            _ui.value = this
        }
    }

    fun saveHistory(
        params: History,
        onSuccess: (HistoryCacheObject) -> Unit,
        onError: (Exception) -> Unit
    ) {
        SaveNewHistoryUseCase.executor(
            saveNewHistoryUseCase,
            params, onSuccess, onError
        )
    }
}