package com.springBoot.saravana_bhavan.REPO;
	


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.springBoot.saravana_bhavan.project_help.AES;
import com.springBoot.saravana_bhavan.MODEL.employee_model;


import jakarta.persistence.EntityManager;
	import jakarta.persistence.ParameterMode;
	import jakarta.persistence.PersistenceContext;
	import jakarta.persistence.StoredProcedureQuery;

	@Repository
	public class employee_repository {
		@PersistenceContext
		private EntityManager entityManager;
		
		public  String employee_signup (String emp_id,String emp_name,
				String emp_email,String emp_cell,String emp_pass,
				String emp_role,String idt) {
			String enc_pass = AES.Encrypt(emp_pass);
			
			StoredProcedureQuery sp  = entityManager
					.createStoredProcedureQuery("sp_emp_ins")
					.registerStoredProcedureParameter("emp_id",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("emp_name",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("emp_email",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("emp_cell",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("emp_pass",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("emp_role",String.class,ParameterMode.IN)
					.registerStoredProcedureParameter("idt",String.class,ParameterMode.OUT)
					.registerStoredProcedureParameter("res",String.class,ParameterMode.OUT);
					
					
		    sp.setParameter("emp_id", emp_id);
			sp.setParameter("emp_name", emp_name);
			sp.setParameter("emp_email", emp_email);
			sp.setParameter("emp_cell", emp_cell);
			sp.setParameter("emp_pass", emp_pass);
			sp.setParameter("emp_role", emp_role);
			
			
			sp.execute();
			
			
					
			return (String)sp.getOutputParameterValue("res");
		}

		

		public  Map<String, String> employee_login(String emp_email, String emp_pass) {
			
			StoredProcedureQuery  sp  = entityManager
					.createStoredProcedureQuery("sp_emp_login")
					
					.registerStoredProcedureParameter("emp_email",String.class,ParameterMode.IN)	
					.registerStoredProcedureParameter("emp_pass",String.class,ParameterMode.IN)
					
					
					.registerStoredProcedureParameter("emp_name",String.class,ParameterMode.OUT)			
					.registerStoredProcedureParameter("emp_role",String.class,ParameterMode.OUT)
					.registerStoredProcedureParameter("res",String.class,ParameterMode.OUT);
			
			sp.setParameter("emp_email", emp_email);
			sp.setParameter("emp_pass", emp_pass);
			sp.execute();
			
			
			Map<String,String> sp_resultMap = new HashMap<String, String>();
			
			sp_resultMap.put("result",(String) sp.getOutputParameterValue("res"));
			sp_resultMap.put("emp_role",(String) sp.getOutputParameterValue("emp_role"));
			sp_resultMap.put("emp_name", (String) sp.getOutputParameterValue("emp_name"));
			
			return sp_resultMap;
		}
		
	    
		public Map<String, Object> emp_search(String emp_id) {
		    StoredProcedureQuery sp = entityManager
		            .createStoredProcedureQuery("sp_emp_getby_id", employee_model.class)
		            .registerStoredProcedureParameter("emp_id", String.class, ParameterMode.IN)
		            .registerStoredProcedureParameter("res", String.class, ParameterMode.OUT);

		    sp.setParameter("emp_id", emp_id);

		    boolean has_result = sp.execute();
		    List<employee_model> emp = new ArrayList<>();
		    if(has_result) {
		        emp = sp.getResultList();
		    }


		    String resultmsg = (String) sp.getOutputParameterValue("res");
		    Map<String, Object> response = new HashMap<>();
		    response.put("res", resultmsg);
		    response.put("emp", emp.isEmpty() ? null : emp.get(0));

		    return response;
		}

		   
		   
		   
			public  String employee_update (String emp_id,String emp_name,
					String emp_email,String emp_cell,String emp_pass,
					String emp_role,String idt) {
				String enc_pass = AES.Encrypt(emp_pass);
				
				StoredProcedureQuery sp  = entityManager
						.createStoredProcedureQuery("sp_emp_upd")
						.registerStoredProcedureParameter("emp_id",String.class,ParameterMode.IN)
						.registerStoredProcedureParameter("emp_name",String.class,ParameterMode.IN)
						.registerStoredProcedureParameter("emp_email",String.class,ParameterMode.IN)
						.registerStoredProcedureParameter("emp_cell",String.class,ParameterMode.IN)
						.registerStoredProcedureParameter("emp_pass",String.class,ParameterMode.IN)
						.registerStoredProcedureParameter("emp_role",String.class,ParameterMode.IN)
						.registerStoredProcedureParameter("idt",String.class,ParameterMode.IN)
						.registerStoredProcedureParameter("res",String.class,ParameterMode.OUT);
						
						
			    sp.setParameter("emp_id", emp_id);
				sp.setParameter("emp_name", emp_name);
				sp.setParameter("emp_email", emp_email);
				sp.setParameter("emp_cell", emp_cell);
				sp.setParameter("emp_pass", emp_pass);
				sp.setParameter("emp_role", emp_role);
				sp.setParameter("idt", idt);
				
				sp.execute();
				
				
				return (String)sp.getOutputParameterValue("res");
			}
			
			
			public  String employee_delete (String emp_id) {
				
				StoredProcedureQuery sp  = entityManager
						.createStoredProcedureQuery("sp_emp_del")
						.registerStoredProcedureParameter("emp_id",String.class,ParameterMode.IN)
						.registerStoredProcedureParameter("res",String.class,ParameterMode.OUT);
				
						
					    sp.setParameter("emp_id", emp_id);
				       sp.execute();
				       String resultmsg = (String)sp.getOutputParameterValue("res");
				       
				return resultmsg;
				
			}



			public List<employee_model> emp_all() {
				
				return entityManager.createNativeQuery("SELECT * FROM emp_tbl ", 
						employee_model.class)
						.getResultList();
			
			}
		    	
	    
		
		
		
		
		
		
		

}
