package com.springBoot.saravana_bhavan.REPO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.springBoot.saravana_bhavan.MODEL.customer_model;
import com.springBoot.saravana_bhavan.MODEL.employee_model;
import com.springBoot.saravana_bhavan.project_help.AES;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;

@Repository
public class customer_repository {
	@PersistenceContext
	private EntityManager entityManager;
	
	
	public  String customer_signup(String cus_id,String cus_name,
			String cus_email,String cus_cell,String cus_pass,
			String idt) {
		String enc_pass = AES.Encrypt(cus_pass);
		
		StoredProcedureQuery sp  = entityManager
				.createStoredProcedureQuery("sp_cus_ins")
				.registerStoredProcedureParameter("cus_id",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("cus_name",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("cus_email",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("cus_cell",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("cus_pass",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("idt",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("res",String.class,ParameterMode.OUT);
				
				
	    sp.setParameter("cus_id", cus_id);
		sp.setParameter("cus_name", cus_name);
		sp.setParameter("cus_email", cus_email);
		sp.setParameter("cus_cell", cus_cell);
		sp.setParameter("cus_pass", cus_pass);
		sp.setParameter("idt", idt);
		
		
		sp.execute();
		
		
				
		return (String)sp.getOutputParameterValue("res");
	}
	
	

	public  Map<String, String> customer_login(String cus_cell, String cus_pass) {
		
		StoredProcedureQuery  sp  = entityManager
				.createStoredProcedureQuery("sp_cus_login")
				
				.registerStoredProcedureParameter("cus_cell",String.class,ParameterMode.IN)	
				.registerStoredProcedureParameter("cus_pass",String.class,ParameterMode.IN)
			
				.registerStoredProcedureParameter("res",String.class,ParameterMode.OUT)
				.registerStoredProcedureParameter("cus_name",String.class,ParameterMode.OUT)			
				.registerStoredProcedureParameter("cus_id",String.class,ParameterMode.OUT);
				
		sp.setParameter("cus_cell", cus_cell);
		sp.setParameter("cus_pass", cus_pass);
		sp.execute();
		
		
		Map<String,String> sp_resultMap = new HashMap<String, String>();
		
		sp_resultMap.put("result",(String) sp.getOutputParameterValue("res"));
		sp_resultMap.put("cus_name",(String) sp.getOutputParameterValue("cus_name"));
		sp_resultMap.put("cus_id", (String) sp.getOutputParameterValue("cus_id"));
		
		return sp_resultMap;
	}
	

	public  String customer_delete (String cus_id) {
		
		StoredProcedureQuery sp  = entityManager
				.createStoredProcedureQuery("sp_cus_del")
				.registerStoredProcedureParameter("cus_id",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("res",String.class,ParameterMode.OUT);
		
				
			    sp.setParameter("cus_id", cus_id);
		       sp.execute();
		       String resultmsg = (String)sp.getOutputParameterValue("res");
		       
		return resultmsg;
		
	}

	public Map<String, Object> customer_search(String cus_id) {
	    StoredProcedureQuery sp = entityManager
	            .createStoredProcedureQuery("sp_cus_getby_id", customer_model.class)
	            .registerStoredProcedureParameter("cus_id", String.class, ParameterMode.IN)
	            .registerStoredProcedureParameter("res", String.class, ParameterMode.OUT);

	    sp.setParameter("cus_id", cus_id);

	    boolean has_result = sp.execute();
	    List<employee_model> cus = new ArrayList<>();
	    if(has_result) {
	        cus= sp.getResultList();
	    }


	    String resultmsg = (String) sp.getOutputParameterValue("res");
	    Map<String, Object> response = new HashMap<>();
	    response.put("res", resultmsg);
	    response.put("cus", cus.isEmpty() ? null : cus.get(0));

	    return response;
	}

	
	
	public List<customer_model>all_cus(){
		StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("sp_cus_getall",customer_model.class);
       return sp.getResultList();
		
	}
	

	public List<customer_model> cus_all() {
		
		return entityManager.createNativeQuery("SELECT * FROM cus_tbl ", 
				customer_model.class)
				.getResultList();
	
	}
	
	
	public  String customer_update(String cus_id,String cus_name,
			String cus_email,String cus_cell,String cus_pass,
			String idt) {
		       
		
		StoredProcedureQuery sp  = entityManager
				.createStoredProcedureQuery("sp_cus_upd")
				.registerStoredProcedureParameter("cus_id",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("cus_name",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("cus_email",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("cus_cell",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("cus_pass",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("idt",String.class,ParameterMode.IN)
				.registerStoredProcedureParameter("res",String.class,ParameterMode.OUT);
				
				
	    sp.setParameter("cus_id", cus_id);
		sp.setParameter("cus_name", cus_name);
		sp.setParameter("cus_email", cus_email);
		sp.setParameter("cus_cell", cus_cell);
		sp.setParameter("cus_pass", cus_pass );
		sp.setParameter("idt", idt);
		
		sp.execute();
		
		
				
		return (String)sp.getOutputParameterValue("res");
	}
	

	
	

}
