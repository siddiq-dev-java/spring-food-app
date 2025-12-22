package com.springBoot.saravana_bhavan.CONTROLLER;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springBoot.saravana_bhavan.DTO.customer_signup_dto;
import com.springBoot.saravana_bhavan.DTO.food_dto;
import com.springBoot.saravana_bhavan.MODEL.customer_model;
import com.springBoot.saravana_bhavan.MODEL.employee_model;
import com.springBoot.saravana_bhavan.MODEL.food_model;
import com.springBoot.saravana_bhavan.REPO.employee_repository;
import com.springBoot.saravana_bhavan.REPO.food_repository;
import com.springBoot.saravana_bhavan.project_help.AES;


import org.springframework.beans.factory.annotation.Value;

import jakarta.servlet.http.HttpSession;
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
	        
	    	if(file != null && !file.isEmpty()) {

	    	    String img_name = file.getOriginalFilename();
	    	    dto.setFood_image(img_name);

	    	    File folder = new File(img_path);
	    	    if(!folder.exists()){
	    	        folder.mkdirs();
	    	    }

	    	    Path path = Paths.get(img_path + File.separator + img_name);
	    	    Files.copy(file.getInputStream(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

	    	    System.out.println("IMAGE STORED SUCCESSFULLY AT : " + path);
	    	}
	    	else{
	    	    System.out.println("NO FILE SELECTED!");
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
	@PostMapping("/food_search")
	public String food_search(Model model,
	                          @RequestParam("food_id") String food_id) {

	    Map<String, Object> response = foodRepository.food_search(food_id);

	    food_model food = (food_model) response.get("food");
	    String resultmsg = (String) response.get("res");

	    food_dto dto = new food_dto();

	    if(food != null){

	        dto.setFood_id(food.getFood_id());
	        dto.setEmp_id(food.getEmp_id());
	        dto.setFood_name(food.getFood_name());
	        dto.setFood_price(String.valueOf(food.getFood_price()));
	        dto.setFood_stock(String.valueOf(food.getFood_stock()));
	        dto.setFood_image(food.getFood_image());
	        dto.setFood_detail(food.getFood_detail());
	    }

	    model.addAttribute("dto", dto);
	    model.addAttribute("admins", employeeRepository.emp_admin());
	    model.addAttribute("res", resultmsg);

	    return "food_update";
	}



	@GetMapping("/food_update")
	public String foodupdate(Model model) {

	    List<food_model> food = foodRepository.food_all();
	    List<employee_model> admins = employeeRepository.emp_admin();
	    
	    model.addAttribute("food", food);
	    model.addAttribute("admins", admins);

	   
	    if(!model.containsAttribute("dto")) {
	        model.addAttribute("dto", new food_dto());
	    }

	    return "food_update";


	}
	
	@PostMapping("/food_update")
	public String food_update(
	        @ModelAttribute("dto") food_dto dto,
	        @RequestParam(value = "pimageFile", required = false) MultipartFile file,
	        Model model) {

	    try {

	        // 🔥 if new image uploaded → save new image
	        if(file != null && !file.isEmpty()) {

	            String img_name = file.getOriginalFilename();
	            dto.setFood_image(img_name);

	            File folder = new File(img_path);
	            if(!folder.exists())
	                folder.mkdirs();

	            Path path = Paths.get(img_path + File.separator + img_name);
	            Files.copy(file.getInputStream(), path,
	                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

	            System.out.println("UPDATED IMAGE SAVED = " + path);
	        }
	        else{
	            // 🔥 NO new image → keep old image
	            food_model oldFood = foodRepository.food_search(dto.getFood_id())
	                                               .get("food") != null ?
	                    (food_model) foodRepository.food_search(dto.getFood_id())
	                                               .get("food") : null;

	            if(oldFood != null)
	                dto.setFood_image(oldFood.getFood_image());
	        }

	    } catch (Exception e){
	        e.printStackTrace();
	        model.addAttribute("res","Image Error");
	    }

	    // 🔥 CALL UPDATE SP
	    String res = foodRepository.food_update(dto);
	    model.addAttribute("res", res);

	    model.addAttribute("admins", employeeRepository.emp_admin());
	    model.addAttribute("dto", dto);

	    return "food_update";
	}


	

}
