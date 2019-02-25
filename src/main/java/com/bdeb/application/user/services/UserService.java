package com.bdeb.application.user.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bdeb.application.user.exception.DataNotFoundException;
import com.bdeb.application.user.exception.DataPersistentException;
import com.bdeb.application.user.mapper.UserMapper;
import com.bdeb.application.user.repository.RoleRepository;
import com.bdeb.application.user.repository.UserRepository;
import com.bdeb.service.commun.SecurityHeader;

@Service
public class UserService {

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

	public void addUser(com.bdeb.service.user.User userInfo, SecurityHeader securityHeader) {
		com.bdeb.application.user.model.Service service = systemService.getSystem(securityHeader);
		try {
			com.bdeb.application.user.model.User user = new com.bdeb.application.user.model.User();
			user.setUsername(userInfo.getUsername());
			user.setPassword(userInfo.getPassword());
			user.setFirstName(userInfo.getFirstname());
			user.setLastName(userInfo.getLastname());
			user.setEmail(userInfo.getEmail());
			user.setDateIns(new Date());
			user.setPhoneNumber(userInfo.getPhonenumber());
			user.setService(service);
			
			List<com.bdeb.application.user.model.UsersRole> listeUserRole = new ArrayList<com.bdeb.application.user.model.UsersRole>();
			// Récupérer les roles
			List<com.bdeb.service.user.Role> liste = userInfo.getRoles();
			for (int i = 0; i < liste.size(); i++) {
				com.bdeb.application.user.model.Role role = roleRepository.getRol(liste.get(i).value());
				com.bdeb.application.user.model.UsersRole userRole = new com.bdeb.application.user.model.UsersRole();
				role.setCode(liste.get(i).value());
				userRole.setUser(user);
				userRole.setRole(role);
				userRole.setDateIns(new Date());
				//****ICI C JUSTE UN TESTE
				userRole.setUserIns("root");
				listeUserRole.add(userRole);

			}
			user.setUsersRoles(listeUserRole);
			userRepository.save(user);

		} catch (Exception e) {
			throw new DataPersistentException("Data not Inserted" +e.getMessage());
		}
	}

}