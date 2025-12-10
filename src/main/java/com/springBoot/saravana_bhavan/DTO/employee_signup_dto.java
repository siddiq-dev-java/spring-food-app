package com.springBoot.saravana_bhavan.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class employee_signup_dto {
	


	@NotBlank(message = "enter id---")
	@Pattern(regexp = "^(?i)e[0-9]+$", message = "ID only starts e1")
	private String emp_id;
	
	@NotBlank(message = "enter name---")
	private String emp_name;
	
	
	@NotBlank(message = "enter email---")
	@Pattern(regexp = "^[a-z0-9._%+-]+@gmail\\.com$", message = "Email must be a valid Gmail id")
	private String emp_email;
	
	@NotBlank(message = "enter cell---")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "cell num must have 10 digits----")
	private String emp_cell;
	
	@NotBlank(message = "enter pass---")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{4,}$", message = "Password must have upper,lower,symbol,number----")
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
	

