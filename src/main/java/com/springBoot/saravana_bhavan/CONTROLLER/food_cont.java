package com.springBoot.saravana_bhavan.CONTROLLER;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springBoot.saravana_bhavan.DTO.food_dto;
import com.springBoot.saravana_bhavan.MODEL.employee_model;
import com.springBoot.saravana_bhavan.MODEL.food_model;
import com.springBoot.saravana_bhavan.REPO.employee_repository;
import com.springBoot.saravana_bhavan.REPO.food_repository;


import org.springframework.beans.factory.annotation.Value;

import jakarta.validation.Valid;

@Controller

public class food_cont {

	@Autowired
	private food_repository foodRepository;
	
	@Autowired
	private employee_repository employeeRepository;
	
	
	@Value("${food.image.path}")
	private String img_path;

	@GetMapping("/food")
	public String food(Model model) {
		List<food_model> food = foodRepository.food_all();
		List<employee_model> admins = employeeRepository.emp_admin();
		
		model.addAttribute("food",food);
		model.addAttribute("admins", admins);
		model.addAttribute("dto", new food_dto() );
		return "food";
	}
	
	@PostMapping("/insert")
	public String food_insert(
	        @Valid @ModelAttribute("dto") food_dto dto,
	        BindingResult resultBinding,
	        @RequestParam(value = "pimageFile", required = false) MultipartFile file,

	        Model model) {

	    // validation error
	    if (resultBinding.hasErrors()) {
	        model.addAttribute("food", foodRepository.food_all());
	        model.addAttribute("admins", employeeRepository.emp_admin());
	        return "food";
	    }

	    try {
	        if (!file.isEmpty()) {

	            String img_name = file.getOriginalFilename();
	            dto.setFood_image(img_name);
	            File simg = new File(img_path);
	            if (!simg.exists()) {
	                boolean created = simg.mkdirs();
	                System.out.println("IMAGE simg CREATED = " + created);
	            }
	            System.out.println("IMAGE NAME = " + img_name);
	            System.out.println("IMAGE PATH = " + img_path);


	            Path path = Paths.get(img_path , img_name);
	            file.transferTo(path.toFile());

	        }

	    } catch (Exception e) {
	    	
	    	  e.printStackTrace();
	        model.addAttribute("res", "Error : " + e.getMessage());
	        model.addAttribute("food", foodRepository.food_all());
	        model.addAttribute("admins", employeeRepository.emp_admin());
	        return "food";
	    }

	    // DB insert
	    String res = foodRepository.food_insert(dto);

	    model.addAttribute("res", res);
	    model.addAttribute("food", foodRepository.food_all());
	    model.addAttribute("admins", employeeRepository.emp_admin());
	    model.addAttribute("dto", new food_dto());

	    return "food";

	}

}
