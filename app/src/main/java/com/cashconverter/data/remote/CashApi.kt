package com.cashconverter.data.remote

import com.cashconverter.data.model.CashModel
import com.cashconverter.util.Constants.API_KEY
import com.cashconverter.util.Constants.DEFAULT_CURRENCY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface CashApi {

    @GET("{apiKey}/latest/{currency}")
    suspend fun getRates(
        @Path("apiKey")
        apiKey: String = API_KEY,
        @Path("currency")
        currency: String = DEFAULT_CURRENCY
        ): Response<CashModel>

}