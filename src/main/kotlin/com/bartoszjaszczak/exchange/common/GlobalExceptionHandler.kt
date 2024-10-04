package com.bartoszjaszczak.exchange.common

import com.bartoszjaszczak.exchange.application.domain.AccountNotFound
import com.bartoszjaszczak.exchange.application.domain.RatesNotAvailable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [AccountNotFound::class])
    protected fun handleAccountNotFound(ex: RuntimeException, request: WebRequest): ResponseEntity<Any>? =
        handleExceptionInternal(ex, ex.message, HttpHeaders(), HttpStatus.BAD_REQUEST, request)

    @ExceptionHandler(value = [RatesNotAvailable::class])
    protected fun handleRatesNotAvailable(ex: RuntimeException, request: WebRequest): ResponseEntity<Any>? =
        handleExceptionInternal(ex, ex.message, HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request)

}