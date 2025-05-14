package com.art.domain.store

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class HttpError(val code: Int) : ApiResult<Nothing>
    data class Error(val exception: Throwable) : ApiResult<Nothing>
}
