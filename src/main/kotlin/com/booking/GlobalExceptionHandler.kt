package com.booking

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import mu.KotlinLogging
import org.springframework.dao.DataRetrievalFailureException
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

data class ExceptionResult(val errorMessage: String, val url: String)

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NotFoundException(s: String = "Data not found") : RuntimeException(s)

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class BadRequestException(s: String) : RuntimeException(s)

@RestControllerAdvice
class GlobalExceptionHandler {
    /**
     * Convert ORM DataRetrievalFailureException to HTTP-404.
     *
     * Note: Reason is shown in message field of the response provided application property is set: server.error.include-message=always
     */
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(*[DataRetrievalFailureException::class, NotFoundException::class])
    fun handleDataNotFound(request: HttpServletRequest, ex: Exception?): ExceptionResult {
        logger.warn("Request failed [${request.requestURL}]: ${ex?.message}")
        return ExceptionResult(errorMessage = ex?.message ?: "Data not found", url = request.requestURI)
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(request: HttpServletRequest, ex: Exception?): ExceptionResult {
        val msg = ex?.message ?: "Bad request"
        logger.warn("Request failed [${request.requestURL}]: $ex")
        return ExceptionResult(errorMessage = msg, url = request.requestURI)
    }

    /**
     * Return RequestBody validation errors as json.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        MethodArgumentNotValidException::class,
    )
    fun handleRequestBodyExceptions(
        ex: MethodArgumentNotValidException,
    ): Map<String, String?>? {
        logger.warn("Validation failed: ${ex.message}")
        // FEATURE: If field has multiple errors, only the last one will be shown
        // .allErrors returns both field and class level validation errors
        return ex.bindingResult.allErrors.associate {
            val fieldName = when (it) {
                is FieldError -> (it as FieldError).field
                else -> it.objectName
            }
            fieldName to it.defaultMessage
        }
    }

    /**
     * Return PathVariable validation errors as json.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        ConstraintViolationException::class,
    )
    fun handlePathVariableExceptions(
        ex: ConstraintViolationException,
    ): List<String>? {
        logger.warn("PathVariable validation failed failed: ${ex.message} - violations: ${ex.constraintViolations}")
        return ex.constraintViolations.map {
            "${it.propertyPath} - ${it.message}"
        }
    }
}
