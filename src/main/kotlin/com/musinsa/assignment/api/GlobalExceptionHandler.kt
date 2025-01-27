package com.musinsa.assignment.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
        e.message ?: "에러가 발생했습니다.",
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        )

        return ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

