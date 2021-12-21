package com.cashconverter.ui.fragments.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashconverter.data.model.ConversionRates
import com.cashconverter.data.repository.CashRepository
import com.cashconverter.util.Constants
import com.cashconverter.util.DispatcherProvider
import com.cashconverter.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Math.round
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: CashRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    /** Events from ViewModel */
    sealed class CashEvent {
        class Success(val resultText: String) : CashEvent()
        class Failure(val errorText: String) : CashEvent()
        object Loading : CashEvent()
        object Empty : CashEvent()
    }

    private var _conversion = MutableStateFlow<CashEvent>(CashEvent.Empty)
    val conversion = _conversion.asStateFlow()

    fun convert(amount: String, fromCurrency: String, toCurrency: String) {
        val fromAmount = amount.toFloatOrNull()
        if (fromAmount == null) {
            _conversion.value = CashEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CashEvent.Loading
            when(val ratesResponse = repo.getRates(fromCurrency)) {
                is Resource.Error -> {
                    _conversion.value = CashEvent.Failure(ratesResponse.message)
                }
                is Resource.Success -> {
                    val rates = ratesResponse.data.conversion_rates
                    val rate = getRateForCurrency(toCurrency,rates)
                    if(rate == null) {
                        _conversion.value = CashEvent.Failure(Constants.UNKNOWN_ERROR)
                    } else {
                        val convertedValue = rate
                        _conversion.value = CashEvent.Success(
                            "$fromAmount $fromCurrency = $convertedValue $toCurrency"
                        )
                    }

                }
            }

        }
    }

    private fun getRateForCurrency(currency: String, rates: ConversionRates?): Double? = when (currency) {
        "CAD" -> rates?.CAD
        "HKD" -> rates?.HKD
        "ISK" -> rates?.ISK
        "EUR" -> rates?.EUR
        "PHP" -> rates?.PHP
        "DKK" -> rates?.DKK
        "HUF" -> rates?.HUF
        "CZK" -> rates?.CZK
        "AUD" -> rates?.AUD
        "RON" -> rates?.RON
        "SEK" -> rates?.SEK
        "IDR" -> rates?.IDR
        "INR" -> rates?.INR
        "BRL" -> rates?.BRL
        "RUB" -> rates?.RUB
        "HRK" -> rates?.HRK
        "JPY" -> rates?.JPY
        "THB" -> rates?.THB
        "CHF" -> rates?.CHF
        "SGD" -> rates?.SGD
        "PLN" -> rates?.PLN
        "BGN" -> rates?.BGN
        "CNY" -> rates?.CNY
        "NOK" -> rates?.NOK
        "NZD" -> rates?.NZD
        "ZAR" -> rates?.ZAR
        "USD" -> rates?.USD
        "MXN" -> rates?.MXN
        "ILS" -> rates?.ILS
        "GBP" -> rates?.GBP
        "KRW" -> rates?.KRW
        "MYR" -> rates?.MYR
        else -> null
    }
}