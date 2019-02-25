package com.bdeb.application.user.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private Date activeUntil;
	//private BigInteger currentConnections;
	private Date dateIns;
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private Date passwordExpirationDate;
	private Date passwordTokenValidUntil;
	private String passwordUpdateTokenId;
	private String phoneNumber;
	//private String user;
	private Service service;
	private List<UsersRole> usersRoles;

	public User() {
	}


	@Id
	@Column(unique=true, nullable=false, length=50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	@Temporal(TemporalType.TIMESTAMP)
	public Date getActiveUntil() {
		return this.activeUntil;
	}

	public void setActiveUntil(Date activeUntil) {
		this.activeUntil = activeUntil;
	}

/*
	@Column(name="CURRENT_CONNECTIONS", nullable=false)
	public BigInteger getCurrentConnections() {
		return this.currentConnections;
	}

	public void setCurrentConnections(BigInteger currentConnections) {
		this.currentConnections = currentConnections;
	}*/


	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getDateIns() {
		return this.dateIns;
	}

	public void setDateIns(Date dateIns) {
		this.dateIns = dateIns;
	}


	@Column(nullable=false, length=50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@Column(nullable=false, length=50)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	@Column(nullable=false, length=50)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	@Column(nullable=false, length=32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Temporal(TemporalType.TIMESTAMP)
	public Date getPasswordExpirationDate() {
		return this.passwordExpirationDate;
	}

	public void setPasswordExpirationDate(Date passwordExpirationDate) {
		this.passwordExpirationDate = passwordExpirationDate;
	}


	@Temporal(TemporalType.TIMESTAMP)
	public Date getPasswordTokenValidUntil() {
		return this.passwordTokenValidUntil;
	}

	public void setPasswordTokenValidUntil(Date passwordTokenValidUntil) {
		this.passwordTokenValidUntil = passwordTokenValidUntil;
	}


	@Column(length=100)
	public String getPasswordUpdateTokenId() {
		return this.passwordUpdateTokenId;
	}

	public void setPasswordUpdateTokenId(String passwordUpdateTokenId) {
		this.passwordUpdateTokenId = passwordUpdateTokenId;
	}


	@Column(nullable=false, length=12)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


/*	@Column(length=32)
	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}*/


	//bi-directional many-to-one association to Service
	@ManyToOne
	@JoinColumn(name="userIns", nullable=false)
	public Service getService() {
		return this.service;
	}

	public void setService(Service service) {
		this.service = service;
	}


	//bi-directional many-to-one association to UsersRole
	@OneToMany(mappedBy="user", cascade={CascadeType.ALL})
	public List<UsersRole> getUsersRoles() {
		return this.usersRoles;
	}

	public void setUsersRoles(List<UsersRole> usersRoles) {
		this.usersRoles = usersRoles;
	}

	public UsersRole addUsersRole(UsersRole usersRole) {
		getUsersRoles().add(usersRole);
		usersRole.setUser(this);

		return usersRole;
	}

	public UsersRole removeUsersRole(UsersRole usersRole) {
		getUsersRoles().remove(usersRole);
		usersRole.setUser(null);

		return usersRole;
	}

}