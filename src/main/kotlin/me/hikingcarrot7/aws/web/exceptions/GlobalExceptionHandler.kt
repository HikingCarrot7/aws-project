package me.hikingcarrot7.aws.web.exceptions

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
  companion object {
    private fun marshalErrorResponse(
      status: HttpStatus,
      errors: List<String?>
    ): ResponseEntity<Any> {
      val body: MutableMap<String, Any> = HashMap()
      body["timestamp"] = Date()
      body["status"] = status.value()
      body["errors"] = errors
      return ResponseEntity(body, status)
    }
  }

  override fun handleMethodArgumentNotValid(
    ex: MethodArgumentNotValidException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest
  ): ResponseEntity<Any> {
    val errors = ex.bindingResult
      .fieldErrors
      .map(FieldError::getDefaultMessage)
    return marshalErrorResponse(status, errors)
  }

}