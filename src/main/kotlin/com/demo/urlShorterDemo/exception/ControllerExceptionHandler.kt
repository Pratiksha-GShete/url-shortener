package com.demo.urlShorterDemo.exception


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import java.security.InvalidParameterException


@ControllerAdvice
@Component
class ControllerExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger("Exception_Controller")

    @ExceptionHandler(InvalidParameterException::class)
    fun handleInvalidParameterException(e: InvalidParameterException): ResponseEntity<ErrorMessage?>? {
        val msg = e.message?.let { ErrorMessage(it) }
        logger.error("Invalid Url Exception : {} ", msg)
        logger.error("Exception stackTrace: {}", e.toString())
        return ResponseEntity<ErrorMessage?>(msg, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ObjectAlreadyPresentException::class)
    fun objectAlreadyPresentException(e: ObjectAlreadyPresentException): ResponseEntity<ErrorMessage?>? {
        val msg = ErrorMessage(e.message)
        logger.error("Url already present : {} ", msg)
        logger.error("Exception stackTrace: {}", e.toString())
        return ResponseEntity<ErrorMessage?>(msg, HttpStatus.CONFLICT)
    }

}