package com.bdeb.application.user.services;

import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bdeb.application.user.exception.AuthentificationException;
import com.bdeb.application.user.repository.ServiceRepository;
import com.bdeb.application.user.repository.UserRepository;

@Service
public class SecurityService {
	@Autowired
	ServiceRepository serviceRepository;
	@Value("${security.charset}")
	String charset;

	@Value("${security.algorithm}")
	String algorithm;

	@Autowired
	UserRepository userRepository;

	public String hash(String plainTextPassword) throws RuntimeException {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			digest.update(plainTextPassword.getBytes(charset));
			byte[] rawHash = digest.digest();
			return new String(DatatypeConverter.printHexBinary(rawHash));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public com.bdeb.application.user.model.Service authentifiate(String username, String password)
			throws AuthentificationException {
		com.bdeb.application.user.model.Service service = null;
		
			service = serviceRepository.getService(username);
			if ((null == service) || (!hash(password).equalsIgnoreCase(service.getPassword()))) {
				throw new AuthentificationException("Authentification Faild1");
			}
			return service;
		
	}

}
