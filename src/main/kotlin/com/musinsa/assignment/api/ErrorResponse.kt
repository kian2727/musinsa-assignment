package com.musinsa.assignment.api

import org.springframework.http.HttpStatus

data class ErrorResponse(
    val message: String?,
    val httpStatus: HttpStatus
)