package com.bangkit.pricely.domain.util

sealed class Resource<T>(
    open val data: T? = null,
    open val errorCode: Int? = null,
    open val errorMessage: String? = null,
) {
    data class Success<T>(
        override val data: T?,
    ) : Resource<T>(data)

    data class Error<T>(
        override val errorCode: Int,
        override val errorMessage: String,
    ) : Resource<T>(errorCode = errorCode, errorMessage = errorMessage)

    class Loading<T> : Resource<T>()
}
