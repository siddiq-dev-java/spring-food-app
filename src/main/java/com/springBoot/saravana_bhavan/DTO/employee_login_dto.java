package com.springBoot.saravana_bhavan.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class employee_login_dto {
	
	



	@NotBlank(message = "enter email---")
	@Pattern(regexp = "^[a-z0-9._%+-]+@gmail\\.com$", message = "Email must be a valid Gmail id")

	private String emp_email;
	
	
	
	@NotBlank(message = "enter pass---")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{4,}$", message = "Password must have upper,lower,symbol,number----")

	private String emp_pass;
	
	
	public String getEmp_email() {
		return emp_email;
	}



	public void setEmp_email(String emp_email) {
		this.emp_email = emp_email;
	}



	public String getEmp_pass() {
		return emp_pass;
	}



	public void setEmp_pass(String emp_pass) {
		this.emp_pass = emp_pass;
	}


}
