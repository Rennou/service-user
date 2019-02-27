package com.bdeb.application.user.controllers;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdeb.application.user.exception.ValidationException;
import com.bdeb.application.user.services.UserService;
import com.bdeb.service.commun.SecurityHeader;
import com.bdeb.service.user.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/users")
public class UserControllers {

	@Autowired
	UserService userService;
	
	@Autowired 
	ObjectMapper jsonMapper;

	@PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> add(@RequestBody @Valid com.bdeb.service.user.User user, @RequestHeader(name="Security-Header" , required=true) String securityHeader,BindingResult result) throws JsonParseException, JsonMappingException, IOException {
		verifyValidationError(result);
		userService.addUser(user,jsonMapper.readValue(securityHeader, SecurityHeader.class));
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}

	
	@GetMapping(path="/{username}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<com.bdeb.service.user.User> getUser(@PathVariable("username") String username,
			@RequestHeader(name="Security-Header" , required=true) String securityHeader) throws JsonParseException, JsonMappingException, IOException {
		ResponseEntity<User> rep = new ResponseEntity<User>(userService.getUser(username, jsonMapper.readValue(securityHeader, SecurityHeader.class)), HttpStatus.OK);
		return rep;
	}
	
	@GetMapping(path="/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<com.bdeb.service.user.User>> getListUser(
			@RequestHeader(name="Security-Header" , required=true) String securityHeader)throws JsonParseException, JsonMappingException, IOException {
		return new ResponseEntity<List<com.bdeb.service.user.User>>(userService.geListUser(jsonMapper.readValue(securityHeader, SecurityHeader.class)), HttpStatus.OK);
	}
	
	private void verifyValidationError(BindingResult result) {
		if (result.hasErrors()) {
			throw new ValidationException(result);
		}
	}

}
