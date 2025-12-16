package com.springBoot.saravana_bhavan.MODEL;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="food_tbl")
public class food_model {
	

			   private String emp_id;
	           @Id
	           private String food_id;
	           private String food_name;
	           private String food_price;
	           private String food_stock;
	           private String food_image;
	           private String food_detail;
	           private String idt;
	           
	           
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
	public String getFood_image() {
		return food_image;
	}
	public void setFood_image(String food_image) {
		this.food_image = food_image;
	}
	public String getFood_detail() {
		return food_detail;
	}
	public void setFood_detail(String food_detail) {
		this.food_detail = food_detail;
	}
	public String getIdt() {
		return idt;
	}
	public void setIdt(String idt) {
		this.idt = idt;
	}

}
