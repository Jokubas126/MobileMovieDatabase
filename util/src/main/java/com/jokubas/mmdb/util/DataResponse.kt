package com.jokubas.mmdb.util

sealed class DataResponse {

    data class Success(
        val value: Any
    ): DataResponse()

    object Loading: DataResponse()

    data class Error(val message: String = ""): DataResponse()

    object Empty: DataResponse()
}