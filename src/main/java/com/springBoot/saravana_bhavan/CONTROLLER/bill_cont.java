package com.springBoot.saravana_bhavan.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.springBoot.saravana_bhavan.DTO.customer_signup_dto;
import com.springBoot.saravana_bhavan.REPO.food_repository;

@Controller
public class bill_cont {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private food_repository foodRepository;
	
	@GetMapping("/bill")
	public String bill_page(Model model){

	    model.addAttribute("food_all1", foodRepository.food_all());

	    model.addAttribute("dto", new customer_signup_dto());  

	    return "bill";
	}

	

}
