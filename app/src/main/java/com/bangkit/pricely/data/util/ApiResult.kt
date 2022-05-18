package com.bangkit.pricely.data.util

sealed class ApiResult<T>(
    val result: T? = null,
    val errorCode: Int? = null,
    val errorMessage: String? = null,
) {
    class Success<T>(
        result: T?,
    ) : ApiResult<T>(result)

    class Error<T>(
        errorCode: Int?,
        errorMessage: String,
    ) : ApiResult<T>(errorCode = errorCode, errorMessage = errorMessage)
}
