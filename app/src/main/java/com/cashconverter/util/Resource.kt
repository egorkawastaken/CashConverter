package com.cashconverter.util

sealed class Resource<out R> {

    val succeeded
        get() = this is Success && data != null

    data class Success<T>(val data: T) : Resource<T>()
    class Error(val message: String) : Resource<Nothing>()
}