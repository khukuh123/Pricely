package com.bangkit.pricely.data.util

import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import retrofit2.Response

private fun <T, U> Flow<ApiResult<T>>.mapToDomain(mapper: (T) -> U): Flow<Resource<U>> =
    this.map {
        when (it) {
            is ApiResult.Success -> {
                Resource.Success(mapper.invoke(it.result!!))
            }
            else -> {
                Resource.Error(it.errorCode ?: 999, it.errorMessage ?: "")
            }
        }
    }

private fun <T> Response<T>.call(): Flow<ApiResult<T>> =
    flow<ApiResult<T>> {
        try {
            val response = this@call
            if (response.isSuccessful) {
                emit(ApiResult.Success(response.body()!!))
            } else {
                with(response) {
                    errorBody()?.string()?.let { value ->
                        val message = JSONObject(value).getString("message")
                        emit(ApiResult.Error(code(), message))
                    }
                }
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(999, e.message.orEmpty()))
        }
    }.flowOn(Dispatchers.IO)