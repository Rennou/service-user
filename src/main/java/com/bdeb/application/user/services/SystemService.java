package com.bdeb.application.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bdeb.service.commun.SecurityHeader;
@Service
public class SystemService {
		@Autowired
		SecurityService securityService;	
		public com.bdeb.application.user.model.Service  getSystem(SecurityHeader credentiel) {
			return securityService.authentifiate(credentiel.getUsername(), credentiel.getPassword());
		}
}
