package com.jokubas.mmdb.util

sealed class DataResponse {

    data class Success<T>(
        val value: T
    ): DataResponse()

    object Loading: DataResponse()

    data class Error(val message: String = "Something went wrong"): DataResponse()

    object Empty: DataResponse()
}