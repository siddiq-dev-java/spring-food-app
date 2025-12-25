package com.springBoot.saravana_bhavan.MODEL;

public class cart_model {
	

	    private String food_id;
	    private String food_name;
	    private double food_price;
	    private int food_qty;
	    private double food_total;

	    // getter & setter

	    public String getFood_id(){
	     return food_id; 
	    
	    }
	    public void setFood_id(String food_id) {
	    	this.food_id = food_id;
	    	
	    }

	    public String getFood_name() {
	    	return food_name;
	    	
	    }
	    
	    public void setFood_name(String food_name) { 
	    	this.food_name = food_name; }

	    
	    public double getFood_price() { 
	    	return food_price;
	    	
	    }
	    
	    public void setFood_price(double food_price) {
	     this.food_price = food_price;
	     
	    }

	    public int getFood_qty() 
	    { return food_qty; 
	    
	    }
	    public void setFood_qty(int food_qty) {
	     this.food_qty = food_qty; 
	    
	    }

	    public double getFood_total()
	    { return food_total;
	    }
	    
	    public void setFood_total(double food_total){
	     this.food_total = food_total;
	     
	    }
	}



