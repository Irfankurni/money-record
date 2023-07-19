package com.irfan.moneyrecord.config;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public abstract class BaseEntityManager {

	protected EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

}
