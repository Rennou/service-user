package com.bdeb.application.user.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.bdeb.application.user.model.User;

@Repository
@Transactional
public class UserRepository {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void save(User user) {

		if (null != user.getDateIns()) {
			em.merge(user);
		} else {
			user.setDateIns(new Date());
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 1);
			c.setTime(new Date());
			user.setPasswordExpirationDate(c.getTime());
			em.persist(user);
		} 
	}

	@Transactional
	public com.bdeb.application.user.model.User getUser(String username) {
		return (com.bdeb.application.user.model.User) em
				.createQuery("select u from User u  where  u.username =: username").setParameter("username", username)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<com.bdeb.application.user.model.User> getListUser() {
		return (List<com.bdeb.application.user.model.User>) em.createQuery("select u from User u").getResultList();
	}

}
