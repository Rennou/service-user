package com.bdeb.application.user.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bdeb.service.commun.SecurityHeader;
import com.bdeb.service.email.EmailBody;
import com.bdeb.service.email.EmailStatus;
import com.bdeb.service.email.EmailType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmailService {

	@Autowired
	ObjectMapper jsonMapper;
		
	@Value("${application.name}")
	String username;
	
	@Value("${application.password}")
	String password;
	
	@Value("${service.email.url}")
	String emailUrl;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	
	public ResponseEntity<EmailBody> sendMail(com.bdeb.application.user.model.User user)
			throws JsonProcessingException, URISyntaxException {
		EmailBody emailBody = getEmailBody(user);
		ResponseEntity<EmailBody> result = sendEmail(emailBody, getHeader());
		if (null == result || !result.getBody().getStatus().equals(EmailStatus.SENT)) {
		}
		return result;
	}

	private EmailBody getEmailBody(com.bdeb.application.user.model.User user) {
		EmailBody emailBody = new EmailBody();
		emailBody.setStatus(EmailStatus.NEW);
		emailBody.setStatusDate(new Date());
		emailBody.setType(EmailType.NEW_USER);
		emailBody.setAddress(user.getEmail());
		emailBody.setFrom(username);
		emailBody.getParameters().add(user.getUsername());
		emailBody.getParameters().add(password);
		emailBody.getParameters().add("http://localhost:8080");
		return emailBody;
	}

	private SecurityHeader getHeader() {
		SecurityHeader securityHeader = new SecurityHeader();	
		securityHeader.setUsername(username);
		securityHeader.setPassword(password);
		return securityHeader;
	}

	private ResponseEntity<EmailBody> sendEmail(EmailBody emailBody, SecurityHeader securityHeader)
			throws URISyntaxException, JsonProcessingException {
		URI url = new URI(emailUrl + "send");
		HttpHeaders header = new HttpHeaders();
		header.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
		header.setContentType(MediaType.APPLICATION_JSON);
		header.set("Security-Header", jsonMapper.writeValueAsString(securityHeader));
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<EmailBody> entity = new HttpEntity<>(emailBody, header);
		ResponseEntity<EmailBody> result = restTemplate.postForEntity(url, entity, EmailBody.class);
		return result;
	}
}
