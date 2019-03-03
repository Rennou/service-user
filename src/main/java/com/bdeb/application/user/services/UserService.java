package com.bdeb.application.user.services;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bdeb.application.user.exception.DataNotFoundException;
import com.bdeb.application.user.exception.DataPersistentException;
import com.bdeb.application.user.exception.SendEmailException;
import com.bdeb.application.user.mapper.UserMapper;
import com.bdeb.application.user.repository.RoleRepository;
import com.bdeb.application.user.repository.UserRepository;
import com.bdeb.service.commun.SecurityHeader;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class UserService {
	@Autowired
	EmailService emailService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	SystemService systemService;
	@Autowired
	UserMapper userMapper;

	public com.bdeb.service.user.User getUser(String username, SecurityHeader securityHeader) {

		// com.bdeb.application.user.model.Service service =
		// systemService.getSystem(securityHeader);
		/// if (null != service) {
		systemService.getSystem(securityHeader);
		com.bdeb.application.user.model.User user = userRepository.getUser(username);
		if (null != user) {
			return userMapper.toUser(user);
		} else {
			throw new DataNotFoundException("No data related to user " + username);
		}

	}

	public List<com.bdeb.service.user.User> geListUser(SecurityHeader securityHeader) {
		com.bdeb.application.user.model.Service service = systemService.getSystem(securityHeader);
		if (null != service) {

			List<com.bdeb.application.user.model.User> ListUser = userRepository.getListUser();
			if (null != ListUser) {
				return userMapper.toList(ListUser);
			} else {
				throw new DataNotFoundException("No data found ");
			}

		} else
			return null;
	}

	public void addUser(com.bdeb.service.user.User userInfo, SecurityHeader securityHeader)
			throws JsonProcessingException, URISyntaxException {
		com.bdeb.application.user.model.Service service = systemService.getSystem(securityHeader);
		com.bdeb.application.user.model.User user;
		com.bdeb.application.user.model.UsersRole userRole;
		List<com.bdeb.service.user.Role> liste;
		List<com.bdeb.application.user.model.UsersRole> listeUserRole;
		try {
			user = mapToUser(userInfo, service);
			listeUserRole = new ArrayList<com.bdeb.application.user.model.UsersRole>();
			// Récupérer les roles
			liste = userInfo.getRoles();
			for (int i = 0; i < liste.size(); i++) {
				userRole = getUserRole(liste.get(i).name(), user, securityHeader.getUsername());
				listeUserRole.add(userRole);
			}
			user.setUsersRoles(listeUserRole);
			userRepository.save(user);

		} catch (Exception e) {
			throw new DataPersistentException("Data not Inserted" + e.getMessage());
		}

		try {

			emailService.sendMail(user);
		} catch (Exception e) {
			throw new SendEmailException("Error sending Email " + e.getMessage());
		}
	}

	private com.bdeb.application.user.model.User mapToUser(com.bdeb.service.user.User userInfo,
			com.bdeb.application.user.model.Service service) {
		com.bdeb.application.user.model.User user;
		user = new com.bdeb.application.user.model.User();
		user.setUsername(userInfo.getUsername());
		user.setPassword(userInfo.getPassword());
		user.setFirstName(userInfo.getFirstname());
		user.setLastName(userInfo.getLastname());
		user.setEmail(userInfo.getEmail());
		user.setDateIns(new Date());
		user.setPhoneNumber(userInfo.getPhonenumber());
		user.setService(service);
		return user;
	}

	public com.bdeb.application.user.model.UsersRole getUserRole(String code, com.bdeb.application.user.model.User user,
			String usernameService) {
		com.bdeb.application.user.model.Role role = roleRepository.getRol(code);
		com.bdeb.application.user.model.UsersRole userRole = new com.bdeb.application.user.model.UsersRole();
		role.setCode(code);
		userRole.setUser(user);
		userRole.setRole(role);
		userRole.setDateIns(new Date());
		userRole.setUserIns(usernameService);
		return userRole;

	}

}