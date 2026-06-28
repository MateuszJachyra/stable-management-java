package com.example.stable_management_mobile.data.repository

import org.json.JSONObject
import retrofit2.HttpException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try{
        Result.success(apiCall())
    }
    catch(e: Exception) {
        if(e is HttpException) {
            try {
                val errorBody = e.response()?.errorBody()?.string() ?: ""
                val jsonObject = JSONObject(errorBody)

                if(jsonObject.has("error")) {
                    val errorMessage = jsonObject.getString("error")
                    return Result.failure(Exception(errorMessage))
                }
            }
            catch (e: Exception) { }
        }
        Result.failure(e)
    }
}