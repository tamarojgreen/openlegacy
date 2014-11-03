package org.openlegacy.demo.db.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Service
public class DbService {

	@PersistenceContext
	private EntityManager em;

	public void saveEntity(Object entity) {
		em.persist(entity);
		em.flush();
	}

}
