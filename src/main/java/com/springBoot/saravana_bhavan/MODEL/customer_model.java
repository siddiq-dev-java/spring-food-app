package com.springBoot.saravana_bhavan.MODEL;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="cus_tbl")
public class customer_model {
	
	@Id
	private String cus_id;

	private String cus_name;
	private String cus_email;
	private String cus_cell;
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
