package com.booking

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.dao.DataRetrievalFailureException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

data class ExceptionResult(val errorMessage: String, val url: String)

@RestControllerAdvice
class GlobalExceptionHandler {
    // Reason is shown in message field of the response provided application property is set: server.error.include-message=always
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested data not found with id")
    @ExceptionHandler(DataRetrievalFailureException::class)
    fun handleDataNotFound(request: HttpServletRequest, ex: Exception?): ExceptionResult {
        logger.warn("Request failed [${request.requestURL}]: ${ex?.message}")
        return ExceptionResult(errorMessage = "Requested data does not exist", url = request.requestURI)
    }
}
