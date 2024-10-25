package com.je.playground.feature.utility

sealed interface Result {
    data object Success : Result
    data class Failure(val message: String) : Result
}