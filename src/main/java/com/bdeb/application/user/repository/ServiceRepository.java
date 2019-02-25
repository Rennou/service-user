package com.bdeb.application.user.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ServiceRepository {
	@PersistenceContext
	private EntityManager em;

	@Transactional
	public com.bdeb.application.user.model.Service getService(String code) {
		return (com.bdeb.application.user.model.Service) em
				.createQuery("select s from Service s  where  s.code =: code").setParameter("code", code.trim())
				.getSingleResult();
	}

}
