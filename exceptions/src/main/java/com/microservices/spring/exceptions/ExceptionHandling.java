package com.microservices.spring.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.util.Objects;

import javax.persistence.NoResultException;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.microservices.spring.exceptions.models.EmailExistException;
import com.microservices.spring.exceptions.models.EmailNotFoundException;
import com.microservices.spring.exceptions.models.UserNotFoundException;
import com.microservices.spring.exceptions.models.UsernameExistException;
import com.microservices.spring.model.Response;

@RestControllerAdvice
public class ExceptionHandling  implements ErrorController{

	private Logger log=LoggerFactory.getLogger(getClass());
	private static final String ACCOUNT_LOCKED = "Your account has been locked. please contact administrator";
	private static final String METHOD_IS_NOT_ALLOWED = "The request method is not allowed on this endpoint. please send a '%s' request";
	private static final String INTERNAL_SERVER_ERROR_MESSAGE = "An error occured while processing the request";
	private static final String INCORRECT_CREDENTIALS = "Username / password incorrect. please try again";
	private static final String ACCOUNT_DISABLED = "Your account has been disabled. If this is an error, please contact administrator";
	private static final String ERROR_PROCESSING_FILE = "Error occured while processing file";
	private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
	private static final String PAGE_NOT_FOUND = "This page was not found";
	public static final String ERROR_PATH = "/error";
		
	
	
	private ResponseEntity<Response> createHttpResponse(HttpStatus status,String message){
		return new ResponseEntity<>(new Response(status.value(), status, status.getReasonPhrase().toUpperCase(), message),status);
	}
	
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<Response> accountDisabledException(){
		return createHttpResponse(BAD_REQUEST, ACCOUNT_DISABLED);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Response> badCredentialsException(){
		return createHttpResponse(BAD_REQUEST, INCORRECT_CREDENTIALS);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Response> accessDeniedException(){
		return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
	}
	
	@ExceptionHandler(LockedException.class)
	public ResponseEntity<Response> lockedException(){
		return createHttpResponse(UNAUTHORIZED, ACCOUNT_LOCKED);
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<Response> tokenExpiredException(TokenExpiredException e){
		return createHttpResponse(UNAUTHORIZED, e.getMessage().toUpperCase());
	}
	
	
	@ExceptionHandler(EmailExistException.class)
	public ResponseEntity<Response> emailExistException(EmailExistException e){
		return createHttpResponse(CONFLICT, e.getMessage().toUpperCase());
	}
	
	@ExceptionHandler(UsernameExistException.class)
	public ResponseEntity<Response> emailExistException(UsernameExistException e){
		return createHttpResponse(CONFLICT, e.getMessage().toUpperCase());
	}
	
	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<Response> emailExistException(EmailNotFoundException e){
		return createHttpResponse(NOT_FOUND, e.getMessage().toUpperCase());
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Response> userNotFoundException(UserNotFoundException e){
		return createHttpResponse(NOT_FOUND, e.getMessage().toUpperCase());
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Response> methodNotSupportedException(HttpRequestMethodNotSupportedException e){
		HttpMethod supportedMethod=Objects.requireNonNull(e.getSupportedHttpMethods()).iterator().next();
		
		return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
	}
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Response> noHandlerFoundException(NoHandlerFoundException e){
		return createHttpResponse(BAD_REQUEST,PAGE_NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> internalServerException(Exception e){
		log.error("Internal Server Exception {}",e);
		return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE);
	}
	
	@ExceptionHandler(NoResultException.class)
	public ResponseEntity<Response> notFoundException(NoResultException e){
		log.error(e.getMessage(),e);
		return createHttpResponse(NOT_FOUND, e.getMessage());
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<Response> ioException(IOException e){
		log.error(e.getMessage(),e);
		return createHttpResponse(NOT_FOUND, e.getMessage());
	}
	
	
}
