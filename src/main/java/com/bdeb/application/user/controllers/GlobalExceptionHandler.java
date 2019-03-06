package com.bdeb.application.user.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bdeb.application.user.exception.AuthentificationException;
import com.bdeb.application.user.exception.DataNotFoundException;
import com.bdeb.application.user.exception.DataPersistentException;
import com.bdeb.application.user.exception.ValidationException;
import com.bdeb.service.error.ErrorType;
import com.bdeb.service.error.ProblemRest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Value("${application.name}")
	String appId;

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException e) {

		ProblemRest problem = new ProblemRest();

		problem.setType(ErrorType.CLIENT);
		problem.setDate(new Date());
		problem.setSystem(appId);
 
		for (ObjectError objectError : e.getErrors().getAllErrors()) {
			com.bdeb.service.error.Error error = new com.bdeb.service.error.Error();
			error.setChamps(objectError.getObjectName());
			error.setMessage(objectError.getDefaultMessage());
			problem.getErrors().add(error);
		}

		return new ResponseEntity<>(problem, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthentificationException.class)
	public ResponseEntity<?> handleAuthentificationException(AuthentificationException e) {

		ProblemRest problem = new ProblemRest();

		problem.setType(ErrorType.CLIENT);
		problem.setDate(new Date());
		problem.setSystem(appId);
		com.bdeb.service.error.Error error = new com.bdeb.service.error.Error();
		error.setMessage(e.getMessage());
		problem.getErrors().add(error);

		return new ResponseEntity<>(problem, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<?> NoDataFoundExceptionException(DataNotFoundException e) {

		ProblemRest problem = new ProblemRest();

		problem.setType(ErrorType.CLIENT);
		problem.setDate(new Date());
		problem.setSystem(appId);
		com.bdeb.service.error.Error error = new com.bdeb.service.error.Error();
		error.setMessage(e.getMessage());
		problem.getErrors().add(error);
		return new ResponseEntity<>(problem, HttpStatus.NO_CONTENT);
	}

	@ExceptionHandler(DataPersistentException.class)
	public ResponseEntity<?> DataPersistentException(DataPersistentException e) {

		ProblemRest problem = new ProblemRest();

		problem.setType(ErrorType.SERVER);
		problem.setDate(new Date());
		problem.setSystem(appId);
		com.bdeb.service.error.Error error = new com.bdeb.service.error.Error();
		error.setMessage(e.getMessage());
		problem.getErrors().add(error);

		return new ResponseEntity<>(problem, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> Exception(Exception e) {

		ProblemRest problem = new ProblemRest();

		problem.setType(ErrorType.SERVER);
		problem.setDate(new Date());
		problem.setSystem(appId);
		com.bdeb.service.error.Error error = new com.bdeb.service.error.Error();
		error.setMessage(e.getMessage());
		problem.getErrors().add(error);

		return new ResponseEntity<>(problem, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
