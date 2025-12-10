package com.springBoot.saravana_bhavan.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class customer_signup_dto {
	
	

	
	@NotBlank(message = "enter id---")
	@Pattern(regexp = "^(?i)c[0-9]+$", message = "ID only starts c1")
	private String cus_id;
	
	@NotBlank(message = "enter name---")
	private String cus_name;
	
	
	@NotBlank(message = "enter email---")
	@Pattern(regexp = "^[a-z0-9._%+-]+@gmail\\.com$", message = "Email must be a valid Gmail id")
	private String cus_email;
	
	@NotBlank(message = "enter cell---")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "cell num must have 10 digits----")
	private String cus_cell;
	
	@NotBlank(message = "enter pass---")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{4,}$", message = "Password must have upper,lower,symbol,number----")
	private String cus_pass;
	
	
	
	public String getCus_id() {
		return cus_id;
	}

	public void setCus_id(String cus_id) {
		this.cus_id = cus_id;
	}

	public String getCus_name() {
		return cus_name;
	}

	public void setCus_name(String cus_name) {
		this.cus_name = cus_name;
	}

	public String getCus_email() {
		return cus_email;
	}

	public void setCus_email(String cus_email) {
		this.cus_email = cus_email;
	}

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
