package com.bdeb.application.user.mapper;

import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.bdeb.application.user.model.UsersRole;
import com.bdeb.service.user.Role;

@Mapper(componentModel = "spring", implementationPackage = "<PACKAGE_NAME>.generated")
public abstract class UserMapper {


	@Mapping(source = "username", target = "username")
	@Mapping(source = "password", target = "password")
	@Mapping(source = "firstName", target = "firstname")
	@Mapping(source = "lastName", target = "lastname")
	@Mapping(source = "phoneNumber", target = "phonenumber")
	@Mapping(source = "email", target = "email")
	@Mapping(ignore = true, target = "roles")
	public abstract com.bdeb.service.user.User toUser(com.bdeb.application.user.model.User user);

	@AfterMapping
	public void mapParrameters(@MappingTarget com.bdeb.service.user.User userTarget,
			com.bdeb.application.user.model.User userSource) {
		if (null != userSource.getUsersRoles()) {
			List<UsersRole> listeRole = userSource.getUsersRoles();
			for (int i = 0; i < listeRole.size(); i++) {
				userTarget.getRoles().add(getRole(listeRole.get(i).getRole()));
			}
		}
		
	}

	public abstract List<com.bdeb.service.user.User> toUser(List<com.bdeb.application.user.model.User> userSource);

	public Role getRole(com.bdeb.application.user.model.Role role) {
		if (null == role) {
			return null;
		}
		return Role.fromValue(role.getCode());
	}
	
	public abstract List< com.bdeb.service.user.User> toList(List<com.bdeb.application.user.model.User> user);

	
	
}
