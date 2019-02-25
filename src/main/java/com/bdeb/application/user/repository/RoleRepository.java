package com.bdeb.application.user.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public class RoleRepository {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public com.bdeb.application.user.model.Role getRol(String code) {
		return (com.bdeb.application.user.model.Role) em.createQuery("select r from Role r  where  r.code =: code")
				.setParameter("code", code).getSingleResult();
	}

}
