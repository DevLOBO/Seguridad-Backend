package unitec.exceptions;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@RestControllerAdvice
public class HandlerExceptions extends ResponseEntityExceptionHandler {
	@ExceptionHandler(IncorrectAudienceException.class)
	public ResponseEntity<CustomError> handleIncorrectAudienceException(IncorrectAudienceException iae, WebRequest request) {
		CustomError error = new CustomError(HttpStatus.UNAUTHORIZED, iae.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<CustomError>(error, error.getStatus());
	}
	
	@ExceptionHandler(JWTCreationException.class)
	public ResponseEntity<CustomError> handleJWTCreationException(JWTCreationException jce, WebRequest request) {
		CustomError error = new CustomError(HttpStatus.INTERNAL_SERVER_ERROR, jce.getLocalizedMessage(),
				request.getDescription(false));
		return new ResponseEntity<CustomError>(error, error.getStatus());
	}
	
	@ExceptionHandler(JWTDecodeException.class)
	public ResponseEntity<CustomError> handleJWTDecodeException(JWTDecodeException jde, WebRequest request) {
		CustomError error = new CustomError(HttpStatus.INTERNAL_SERVER_ERROR, jde.getLocalizedMessage(),
				request.getDescription(false));
		return new ResponseEntity<CustomError>(error, error.getStatus());
	}
	
	@ExceptionHandler(JWTVerificationException.class)
	public ResponseEntity<CustomError> handleJWTVerificationException(JWTVerificationException jve, WebRequest request) {
		CustomError error = new CustomError(HttpStatus.UNAUTHORIZED, jve.getLocalizedMessage(),
				jve.getMessage());
		return new ResponseEntity<CustomError>(error, error.getStatus());
	}
	
	@ExceptionHandler(InvalidKeyException.class)
	public ResponseEntity<CustomError> handleInvalidKeyException(InvalidKeyException ike, WebRequest request) {
		CustomError error = new CustomError(HttpStatus.BAD_REQUEST, ike.getLocalizedMessage(),
				request.getDescription(false));
		return new ResponseEntity<CustomError>(error, error.getStatus());
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<CustomError> handleIOException(IOException ioe, WebRequest request) {
		CustomError error = new CustomError(HttpStatus.BAD_REQUEST, ioe.getLocalizedMessage(),
				request.getDescription(false));
		return new ResponseEntity<CustomError>(error, error.getStatus());
	}

	@ExceptionHandler(BadPaddingException.class)
	public ResponseEntity<CustomError> handleBadPaddingException(BadPaddingException bpe, WebRequest request) {
		CustomError error = new CustomError(HttpStatus.BAD_REQUEST, bpe.getLocalizedMessage(),
				request.getDescription(false));
		return new ResponseEntity<CustomError>(error, error.getStatus());
	}

	@ExceptionHandler(NoSuchPaddingException.class)
	public ResponseEntity<CustomError> handleNoSuchPaddingException(NoSuchPaddingException nspe, WebRequest request) {
		CustomError error = new CustomError(HttpStatus.BAD_REQUEST, nspe.getLocalizedMessage(),
				request.getDescription(false));
		return new ResponseEntity<CustomError>(error, error.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manve,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<>();
		for (FieldError error : manve.getBindingResult().getFieldErrors())
			errors.add(error.getField() + ": " + error.getDefaultMessage());

		for (ObjectError error : manve.getBindingResult().getGlobalErrors())
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());

		CustomError error = new CustomError(HttpStatus.BAD_REQUEST, manve.getLocalizedMessage(), errors);
		return handleExceptionInternal(manve, error, headers, error.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException msrpe,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = msrpe.getParameterName() + " parameter is missing";

		CustomError customError = new CustomError(HttpStatus.BAD_REQUEST, msrpe.getLocalizedMessage(), error);
		return new ResponseEntity<Object>(customError, new HttpHeaders(), customError.getStatus());
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException cve, WebRequest request) {
		List<String> errors = new ArrayList<>();
		for (ConstraintViolation<?> violation : cve.getConstraintViolations())
			errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
					+ violation.getMessage());

		CustomError customError = new CustomError(HttpStatus.BAD_REQUEST, cve.getLocalizedMessage(), errors);
		return new ResponseEntity<Object>(customError, new HttpHeaders(), customError.getStatus());
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException matme,
			WebRequest request) {
		String error = matme.getName() + " should be of type " + matme.getRequiredType().getName();

		CustomError customError = new CustomError(HttpStatus.BAD_REQUEST, matme.getLocalizedMessage(), error);
		return new ResponseEntity<Object>(customError, new HttpHeaders(), customError.getStatus());
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception e, WebRequest request) {
		CustomError error = new CustomError(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(),
				"Error occurred");
		return new ResponseEntity<Object>(error, new HttpHeaders(), error.getStatus());
	}
}