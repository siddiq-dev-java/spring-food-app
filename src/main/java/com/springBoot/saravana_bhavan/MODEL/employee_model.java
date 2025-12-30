package com.springBoot.saravana_bhavan.MODEL;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "emp_tbl")
public class employee_model {
	
	@Id
	
	private String emp_id;

	private String emp_name;
	private String emp_email;
	private String emp_cell;
	private String emp_pass;
	private String emp_role;

	
	public String getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
	public String getEmp_email() {
		return emp_email;
	}
	public void setEmp_email(String emp_email) {
		this.emp_email = emp_email;
	}
	public String getEmp_cell() {
		return emp_cell;
	}
	public void setEmp_cell(String emp_cell) {
		this.emp_cell = emp_cell;
	}
	public String getEmp_pass() {
		return emp_pass;
	}
	public void setEmp_pass(String emp_pass) {
		this.emp_pass = emp_pass;
	}
	public String getEmp_role() {
		return emp_role;
	}
	public void setEmp_role(String emp_role) {
		this.emp_role = emp_role;
	}

}
