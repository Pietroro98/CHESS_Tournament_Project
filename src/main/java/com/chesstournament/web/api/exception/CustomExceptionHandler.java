package com.chesstournament.web.api.exception;

import com.chesstournament.dto.ResponseBusta;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler  {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseBusta<List<String>>> handleValidationExceptions(
			MethodArgumentNotValidException ex,
			WebRequest request) {

		List<String> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ResponseBusta.success(HttpStatus.BAD_REQUEST.value(), "Errore di validazione", errors));
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ResponseBusta<Void>> handleAgendaNotFoundException(NotFoundException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ResponseBusta.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(NotAllowedException.class)
	public ResponseEntity<ResponseBusta<Void>> handleNotAllowedException(NotAllowedException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBusta.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage()));
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ResponseBusta<Void>> handleBadRequestException(BadRequestException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ResponseBusta.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}


	@ExceptionHandler(IdNotNullForInsertException.class)
	public ResponseEntity<ResponseBusta<Void>> handleIdNotNullForInsertException(IdNotNullForInsertException ex,
			WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBusta.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ResponseBusta<Void>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ResponseBusta.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ResponseBusta<Void>> handleInsufficientAuthoritiesException(ForbiddenException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(ResponseBusta.error(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResponseBusta<Void>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(ResponseBusta.error(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
	}

}
