package com.springBoot.saravana_bhavan.REPO;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.springBoot.saravana_bhavan.DTO.food_dto;
import com.springBoot.saravana_bhavan.MODEL.food_model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;



@Repository

public class food_repository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	public  String food_insert (food_dto dto) {
		
		
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
				
	    sp.setParameter("food_id",dto.getFood_id());	
	    sp.setParameter("emp_id", dto.getEmp_id());
		sp.setParameter("food_name",dto.getFood_name());
		sp.setParameter("food_price",dto.getFood_price());
		sp.setParameter("food_stock", dto.getFood_stock());
		sp.setParameter("food_image", dto.getFood_image());
		sp.setParameter("food_detail", dto.getFood_detail());
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
