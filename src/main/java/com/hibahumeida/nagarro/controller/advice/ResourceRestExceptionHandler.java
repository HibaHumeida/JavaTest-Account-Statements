package com.hibahumeida.nagarro.controller.advice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ResourceRestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<ResourceErrorResponse> handleControllerException(AccessDeniedException ex,WebRequest request) {
		ResourceErrorResponse error = new ResourceErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Parameter error");// "Database
		// error"

		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ResourceErrorResponse> handleMethodArgumentTypeMismatch(
			MethodArgumentTypeMismatchException ex, WebRequest request) {

		ResourceErrorResponse error = new ResourceErrorResponse(HttpStatus.BAD_REQUEST.value(), "Argument mismatach error");

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<ResourceErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			WebRequest request) {

		ResourceErrorResponse error = new ResourceErrorResponse(HttpStatus.CONFLICT.value(), "Database error");// "Database
																												// error"

		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ResourceErrorResponse error = new ResourceErrorResponse(HttpStatus.BAD_REQUEST.value(),
				String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ResourceErrorResponse error = new ResourceErrorResponse(HttpStatus.BAD_REQUEST.value(),
				"Malformed JSON request");
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ResourceErrorResponse> handleException(Exception exc) {

		ResourceErrorResponse error = new ResourceErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}