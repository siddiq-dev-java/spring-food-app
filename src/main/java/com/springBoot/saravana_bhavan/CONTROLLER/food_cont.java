package com.springBoot.saravana_bhavan.CONTROLLER;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.springBoot.saravana_bhavan.DTO.food_dto;
import com.springBoot.saravana_bhavan.MODEL.employee_model;
import com.springBoot.saravana_bhavan.MODEL.food_model;
import com.springBoot.saravana_bhavan.REPO.employee_repository;
import com.springBoot.saravana_bhavan.REPO.food_repository;

@Controller
public class food_cont {

	@Autowired
	private food_repository foodRepository;
	
	@Autowired
	private employee_repository employeeRepository;
	
	
	@GetMapping("/food")
	public String food(Model model) {
		List<food_model> food = foodRepository.food_all();
		List<employee_model> admins = employeeRepository.emp_admin();
		
		model.addAttribute("food",food);
		model.addAttribute("admins", admins);
		model.addAttribute("dto", new food_dto() );
		return "food";
	}
}
