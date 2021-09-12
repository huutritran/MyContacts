package com.example.mycontacts.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    val error: LiveData<String?>
        get() = _error
    private val _error = MutableLiveData<String?>()

    val loading: LiveData<Boolean>
        get() = _loading
    protected val _loading = MutableLiveData<Boolean>()

    protected fun onFailure(throwable: Throwable) {
        _error.value = throwable.message.toString()
    }

    protected fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    protected fun clearError() {
        _error.value = null
    }
}