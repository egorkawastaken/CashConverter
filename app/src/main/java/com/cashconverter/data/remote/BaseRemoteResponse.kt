package com.cashconverter.data.remote

import com.cashconverter.util.Constants.UNKNOWN_ERROR
import com.cashconverter.util.Resource
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response

abstract class BaseRemoteResponse {

    suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {

        try {

            val response = call()

            return if (response.isSuccessful) {
                val result = response.body()
                if (result != null) {
                    Resource.Success(result)
                } else {
                    error(response.errorBody()?.toGenericResponse()?.message ?: UNKNOWN_ERROR)
                }
            } else {
                Resource.Error(UNKNOWN_ERROR)
            }

        } catch (e: Exception) {
            return Resource.Error(e.message ?: UNKNOWN_ERROR)
        }

    }

    private fun error(message: String) = Resource.Error(message)

}

fun ResponseBody.toGenericResponse(): GenericResponse? {
    return try {
        Gson().fromJson(this.charStream(), GenericResponse::class.java)
    } catch (e: java.lang.Exception) {
        null
    }
}

class GenericResponse(val code: Int, val message: String)