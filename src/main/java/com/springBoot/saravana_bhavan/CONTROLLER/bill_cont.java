package com.springBoot.saravana_bhavan.CONTROLLER;

import java.net.http.HttpClient;
import java.sql.CallableStatement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.QTypeContributor;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.springBoot.saravana_bhavan.DTO.customer_signup_dto;
import com.springBoot.saravana_bhavan.MODEL.cart_model;
import com.springBoot.saravana_bhavan.REPO.food_repository;


import jakarta.servlet.http.HttpSession;

@Controller
public class bill_cont {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private food_repository foodRepository;
	
	@GetMapping("/bill")
	public String bill_page(Model model, HttpSession session){

	    model.addAttribute("food_all1", foodRepository.food_all());

	    customer_signup_dto cus = (customer_signup_dto) session.getAttribute("customer");
	    if(cus == null){
	        cus = new customer_signup_dto();
	    }

	    model.addAttribute("dto", cus);

	    List<cart_model> cart = (List<cart_model>) session.getAttribute("cart");
	    if(cart == null){
	        cart = new ArrayList<>();
	    }

	    model.addAttribute("cart", cart);
	    double grand_total = 0;
	    for(cart_model c : cart){
	        grand_total += c.getFood_total();
	    }
	    model.addAttribute("grand_total", grand_total);


	    return "bill";
	}


	
	@PostMapping("/save_bill")
	public String saveCustomer(
	        @ModelAttribute("dto") customer_signup_dto dto,
	        HttpSession session,
	        RedirectAttributes ra
	){
	    if(dto.getCus_id() == null || dto.getCus_id().isBlank()){
	        ra.addFlashAttribute("res","❌ Customer ID required");
	        return "redirect:/bill";
	    }

	    // Store in session
	    session.setAttribute("customer", dto);

	    ra.addFlashAttribute("res","✔ Customer Saved");
	    return "redirect:/bill";
	}

	
	@PostMapping("/bill_insert")
	public String billInsert(
	        @RequestParam(required = false) String food_id,
	        @RequestParam(required = false) String food_name,
	        @RequestParam(required = false) String food_qty,
	        @RequestParam(required = false) String food_price,
	        @RequestParam(required = false) String food_total,
	        HttpSession session,
	        RedirectAttributes ra
	) {

	    // ===== GET CUSTOMER FROM SESSION =====
	    customer_signup_dto cus = (customer_signup_dto) session.getAttribute("customer");

	    if(cus == null || cus.getCus_id() == null || cus.getCus_id().isBlank()){
	        ra.addFlashAttribute("res","❌ Customer not selected. Please save customer first.");
	        return "redirect:/bill";
	    }

	    String cus_id = cus.getCus_id();   // Now you have customer id 👍


	    // ===== FOOD VALIDATION =====
	    if (food_id == null || food_id.isBlank()) {
	        ra.addFlashAttribute("res","❌ Select food");
	        return "redirect:/bill";
	    }

	    int qty;
	    double price;
	    double total;

	    try {
	        qty = Integer.parseInt(food_qty);
	        price = Double.parseDouble(food_price);
	        total = Double.parseDouble(food_total);
	    }catch(Exception e){
	        ra.addFlashAttribute("res","❌ Invalid values");
	        return "redirect:/bill";
	    }

	    if(qty <= 0 || total <= 0){
	        ra.addFlashAttribute("res","❌ Quantity / Total invalid");
	        return "redirect:/bill";
	    }

	    // ===== SESSION CART =====
	    List<cart_model> cart = (List<cart_model>) session.getAttribute("cart");
	    if(cart == null){
	        cart = new ArrayList<>();
	    }

	    cart_model c = new cart_model();
	    c.setFood_id(food_id);
	    c.setFood_name(food_name);
	    c.setFood_price(price);
	    c.setFood_qty(qty);
	    c.setFood_total(total);

	    cart.add(c);
	    session.setAttribute("cart", cart);

	    ra.addFlashAttribute("res","✔ Added to Cart");

	    return "redirect:/bill";
	}

	
	@GetMapping("/cart_delete")
	public String cartDelete(@RequestParam int index, HttpSession session, RedirectAttributes ra){

	    List<cart_model> cart = (List<cart_model>) session.getAttribute("cart");

	    if(cart != null && cart.size() > index){
	        cart.remove(index);
	        session.setAttribute("cart", cart);
	        ra.addFlashAttribute("res","🗑 Item removed from cart");
	    }else{
	        ra.addFlashAttribute("res","⚠ Unable to delete");
	    }

	    return "redirect:/bill";
	}

	
	@PostMapping("/generate_bill")
	public String generateBill(HttpSession session, RedirectAttributes ra) {

	    List<cart_model> cart = (List<cart_model>) session.getAttribute("cart");
	    customer_signup_dto cus = (customer_signup_dto) session.getAttribute("customer");

	    if(cus == null || cus.getCus_id() == null){
	        ra.addFlashAttribute("res","❌ Customer not selected");
	        return "redirect:/bill";
	    }

	    if(cart == null || cart.isEmpty()){
	        ra.addFlashAttribute("res","❌ Cart is empty");
	        return "redirect:/bill";
	    }

	    double grandTotal = 0;
	    for(cart_model c : cart){
	        grandTotal += c.getFood_total();
	    }

	    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	    String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

	    // --- MAIN BILL SAVE ---
	    jdbcTemplate.update(
	            "INSERT INTO bill_master(cus_id,bill_date,bill_time,grand_total) VALUES(?,?,?,?)",
	            cus.getCus_id(), date, time, grandTotal
	    );

	    Integer billNo = jdbcTemplate.queryForObject(
	            "SELECT MAX(bill_no) FROM bill_master",
	            Integer.class
	    );

	    // --- ITEMS SAVE ---
	    for(cart_model c : cart){

	        jdbcTemplate.update(
	                "INSERT INTO bill_details(bill_no,food_id,qty,price,total) VALUES(?,?,?,?,?)",
	                billNo,
	                c.getFood_id(),
	                c.getFood_qty(),
	                c.getFood_price(),
	                c.getFood_total()
	        );

	        jdbcTemplate.execute((ConnectionCallback<String>) con -> {
	            CallableStatement cs = con.prepareCall("{call sp_food_stock(?,?,?)}");
	            cs.setString(1, c.getFood_id());
	            cs.setInt(2, c.getFood_qty());
	            cs.registerOutParameter(3, Types.VARCHAR);
	            cs.execute();
	            return cs.getString(3);
	        });

	    }

	    session.removeAttribute("cart");

	    ra.addFlashAttribute("res","✅ Bill Generated Successfully | Bill No : " + billNo);
	    return "redirect:/view_bill?billNo=" + billNo;

	}
	
	@GetMapping("/view_bill")
	public String viewBill(@RequestParam int billNo, Model model){

	    // Get Bill Master
	    var bill = jdbcTemplate.queryForMap(
	            "SELECT * FROM bill_master WHERE bill_no = ?", billNo);

	    // Get Bill Items
	    var items = jdbcTemplate.queryForList(
	            "SELECT * FROM bill_details WHERE bill_no = ?", billNo);

	    model.addAttribute("bill", bill);
	    model.addAttribute("items", items);

	    return "view_bill";
	}


	
	//bill insert

//	String res = jdbcTemplate.execute(ConnectionCallback<String>) con ->{
//		CallableStatement cs = con.prepareCall("{call sp_ebill_ins(?,?,?,?,?,?,?)}");
//		cs.setString(1, cus_id);
//		cs.setString(2, food_id);
//		cs.setString(3, food_qty);
//		cs.setString(4, food_total);
//		cs.setString(5, date);
//		cs.setString(6, time);
//		cs.registerOutParameter(7, Types.VARCHAR);
//		cs.execute();
//		return cs.getString(7);
//	});
//	//stock update
	//grand total
	// food carty table

	
}

	


