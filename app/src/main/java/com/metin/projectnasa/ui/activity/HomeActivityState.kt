package com.metin.projectnasa.ui.activity

sealed class HomeActivityState<T>(var data: T? = null, val message: String? = null) {

    class Success<T>(data: T): HomeActivityState<T>(data)
    class Loading<T>(data: T? = null): HomeActivityState<T>(data)
    class Error<T>(message: String, data: T? = null): HomeActivityState<T>(data, message)
}