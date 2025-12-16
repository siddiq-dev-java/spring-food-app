package com.springBoot.saravana_bhavan.REPO;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.springBoot.saravana_bhavan.MODEL.food_model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;


@Repository
public class food_repository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	public  String food_insert (String food_id,String emp_id,
			String food_name,String food_price,String food_stock,
			String food_image,String food_detail, String idt) {
		
		
		StoredProcedureQuery sp  = entityManager
				.createStoredProcedureQuery("sp_food_ins")
				.registerStoredProcedureParameter("food_id",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("emp_id",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("food_name",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("food_price",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("food_stock",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("food_image",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("food_detail",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("idt",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("res",String.class,ParameterMode.OUT);
				
	    sp.setParameter("food_id",food_id);	
	    sp.setParameter("emp_id", emp_id);
		sp.setParameter("food_name", food_name);
		sp.setParameter("food_price", food_price);
		sp.setParameter("food_stock", food_stock);
		sp.setParameter("food_image", food_image);
		sp.setParameter("food_detail", food_detail);
		sp.setParameter("idt", java.time.LocalDateTime.now().
				format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
		
		
		sp.execute();
		
		
				
		return (String)sp.getOutputParameterValue("res");
	}
	
	
	public String food_del(String food_id) {
		StoredProcedureQuery sp = entityManager.createNamedStoredProcedureQuery("sp_food_del")
				.registerStoredProcedureParameter("food_id", String.class, ParameterMode.IN)
				.registerStoredProcedureParameter("res",String.class, ParameterMode.OUT);
		
		sp.setParameter("food_id",food_id);
		
		
		return (String)sp.getOutputParameterValue("res");
	}

	public List<food_model> food_all() {

	    return entityManager
	            .createQuery("from food_model", food_model.class)
	            .getResultList();
	}




}
