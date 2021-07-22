package com.jokubas.mmdb.util

import android.util.Log
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.lang.Exception

sealed class DataResponse<T> {

    val isSuccessful: Boolean
        get() = this is Success<T>

    fun body(): T? {
        return when (this) {
            is Success<T> -> value
            else -> null
        }
    }

    data class Success<T>(
        val value: T
    ) : DataResponse<T>()

    class Loading<T> : DataResponse<T>()

    data class Error<T>(val message: String = "Something went wrong") : DataResponse<T>()

    class Empty<T> : DataResponse<T>()

}

fun <T, R> DataResponse<T>.map(transform: (DataResponse<T>) -> DataResponse<R>): DataResponse<R> =
    transform.invoke(this)

fun <T, R> DataResponse<T>.defaultRemap(valueTransform: (T) -> R): DataResponse<R> = map {
    when(this){
        is DataResponse.Success -> DataResponse.Success(valueTransform.invoke(value))
        is DataResponse.Error -> DataResponse.Error(message)
        is DataResponse.Empty -> DataResponse.Empty()
        is DataResponse.Loading -> DataResponse.Loading()
    }
}

suspend fun <T> dataResponseFlow(provideData: suspend () -> Response<T>): Flow<DataResponse<T>> =
    flow<DataResponse<T>> {
        emit(DataResponse.Loading())

        try {
            emit(provideData.invoke().toDataResponse())
        } catch (e: Exception) {
            emit(DataResponse.Error(e.message ?: e.localizedMessage ?: e.toString()))
            Log.e("DataResponse", e.stackTraceToString())
        }
    }

suspend fun <T, R> dataResponseFlow(
    observableValueFlow: StateFlow<R>,
    provideData: suspend (R) -> Response<T>
): Flow<DataResponse<T>> = flow<DataResponse<T>> {

    observableValueFlow.collect {
        emit(DataResponse.Loading())
        try {
            emit(provideData.invoke(it).toDataResponse())
        } catch (e: Exception) {
            emit(DataResponse.Error())
            Log.e("DataResponse", e.stackTraceToString())
        }
    }


}

fun <T> T.toDataResponse(errorMessage: String = "Something went wrong"): DataResponse<T> {
    return this?.let {
        DataResponse.Success(it)
    } ?: DataResponse.Error(errorMessage)
}

fun <T> Flow<Response<T>>.toDataResponseFlow(): Flow<DataResponse<T>> = map {
    it.toDataResponse()
}

fun <T> Response<T>.toDataResponse(): DataResponse<T> {
    return body()?.let { body ->
        when {
            isSuccessful -> DataResponse.Success(body)
            !isSuccessful && errorBody() != null -> DataResponse.Error(raw().message())
            !isSuccessful -> DataResponse.Error()
            else -> DataResponse.Loading()
        }
    } ?: DataResponse.Error("Data type is not set")
}


