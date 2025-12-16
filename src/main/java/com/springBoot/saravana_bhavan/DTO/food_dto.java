package com.springBoot.saravana_bhavan.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class food_dto {
	



	@NotBlank(message = "enter id---")
	private String emp_id;
	
	@NotBlank(message = "enter id---")
	@Pattern(regexp = "^(?i)f[0-9]+$", message = "ID only starts f1")
    private String food_id;
	
	@NotBlank(message = "enter food---")
    private String food_name;
	
	@NotBlank(message = "enter price---")
    private String food_price;
	
	@NotBlank(message = "enter stock---")
    private String food_stock;
	
	
	@NotBlank(message = "enter detail---")
    private String food_detail;
	
   
	
	
	public String getEmp_id() {
		return emp_id;
	}


	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}


	public String getFood_id() {
		return food_id;
	}


	public void setFood_id(String food_id) {
		this.food_id = food_id;
	}


	public String getFood_name() {
		return food_name;
	}


	public void setFood_name(String food_name) {
		this.food_name = food_name;
	}


	public String getFood_price() {
		return food_price;
	}


	public void setFood_price(String food_price) {
		this.food_price = food_price;
	}


	public String getFood_stock() {
		return food_stock;
	}


	public void setFood_stock(String food_stock) {
		this.food_stock = food_stock;
	}


	public String getFood_detail() {
		return food_detail;
	}


	public void setFood_detail(String food_detail) {
		this.food_detail = food_detail;
	}

}
