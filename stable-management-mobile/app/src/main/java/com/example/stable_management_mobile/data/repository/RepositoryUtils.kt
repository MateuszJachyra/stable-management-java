package com.example.stable_management_mobile.data.repository

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try{
        Result.success(apiCall())
    }
    catch(e: Exception) {
        Result.failure(e)
    }
}