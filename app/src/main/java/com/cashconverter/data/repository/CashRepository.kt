package com.cashconverter.data.repository

import javax.inject.Inject

class CashRepository @Inject constructor(
    private val remoteDataSource: CashRemoteDataSource
){

    suspend fun getRates(baseCurrency: String) = remoteDataSource.getRates(baseCurrency)

}