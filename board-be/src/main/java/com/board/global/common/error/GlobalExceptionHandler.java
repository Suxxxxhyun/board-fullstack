package com.board.global.common.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.board.global.common.error.exception.BoardNotFoundException;
import com.board.global.common.response.Response;

@RestControllerAdvice(annotations = { RestController.class })
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Response<Map<String, String>>> handleValidationException(
		final MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		final ExceptionDto response = new ExceptionDto(ErrorCode.NOT_VALID_ERROR, errors.toString());
		return ResponseEntity.badRequest().body(Response.fail(response));
	}

	@ExceptionHandler(BoardNotFoundException.class)
	public ResponseEntity<Response<ExceptionDto>> handleBusinessException(final BoardNotFoundException ex) {
		final ExceptionDto response = new ExceptionDto(ex.getErrorCode(), ex.getMessage());
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(Response.fail(response));
	}
}
