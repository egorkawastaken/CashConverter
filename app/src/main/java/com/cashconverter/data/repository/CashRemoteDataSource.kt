package com.cashconverter.data.repository

import com.cashconverter.data.remote.BaseRemoteResponse
import com.cashconverter.data.remote.CashApi
import javax.inject.Inject

class CashRemoteDataSource @Inject constructor(private val cashApi: CashApi): BaseRemoteResponse() {

    suspend fun getRates(baseCurrency: String) =
        getResult { cashApi.getRates(currency = baseCurrency) }

}