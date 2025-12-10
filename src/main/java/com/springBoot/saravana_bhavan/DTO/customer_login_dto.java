package com.springBoot.saravana_bhavan.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class customer_login_dto {

	

	
	@NotBlank(message = "enter cell---")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "cell num must have 10 digits----")
	private String cus_cell;

	
	@NotBlank(message = "enter pass---")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{4,}$", message = "Password must have upper,lower,symbol,number----")

	private String cus_pass;
	
	
	public String getCus_cell() {
		return cus_cell;
	}


	public void setCus_cell(String cus_cell) {
		this.cus_cell = cus_cell;
	}


	public String getCus_pass() {
		return cus_pass;
	}


	public void setCus_pass(String cus_pass) {
		this.cus_pass = cus_pass;
	}


	
}
