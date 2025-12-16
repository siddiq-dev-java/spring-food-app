package com.springBoot.saravana_bhavan.CONTROLLER;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springBoot.saravana_bhavan.DTO.customer_signup_dto;
import com.springBoot.saravana_bhavan.DTO.customer_update_dto;
import com.springBoot.saravana_bhavan.MODEL.customer_model;
import com.springBoot.saravana_bhavan.MODEL.employee_model;
import com.springBoot.saravana_bhavan.DTO.customer_login_dto;
import com.springBoot.saravana_bhavan.REPO.customer_repository;
import com.springBoot.saravana_bhavan.project_help.AES;
import com.springBoot.saravana_bhavan.REPO.customer_repository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class customer_cont {
	
	@Autowired
	private customer_repository customerRepository;

	
	   @GetMapping("/customer_signup")
	    public String customer_Signup(Model model) {
	        model.addAttribute("customer_signup_dto", new customer_signup_dto());
	        return "customer_signup";
	    }
	   
	   
	    @PostMapping("/customer_signup")
	    public String customer_signup(@Valid @ModelAttribute("customer_signup_dto") customer_signup_dto cus,
	                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {

	        if(result.hasErrors()) {
	            return "customer_signup";
	        }

	        // Encrypt pass
	        String enc_pass = AES.Encrypt(cus.getCus_pass());

	        // timestamp
	        String idt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-| HH:mm:ss"));

	        // Call repo
	        String sp_res = customerRepository.customer_signup(
	                cus.getCus_id(),
	                cus.getCus_name(),
	                cus.getCus_email(),
	                cus.getCus_cell(),
	                enc_pass,
	                idt
	        );

	        if(sp_res.contains("CUSTOMER DATA inserted")) {
	            // Flash attribute for redirect → clears form automatically
	            redirectAttributes.addFlashAttribute("msg", sp_res);
	            return "redirect:/customer_signup";  // redirect to same page
	        } else {
	            model.addAttribute("msg", sp_res);   // show DB error on same page
	            return "customer_signup";
	        }
	    }
	    
	    
	    
	    
	    @GetMapping("/customer_login") 
	    public String customer_Login(Model model) {
	    	model.addAttribute("cus", new customer_login_dto());
	    	return "customer_login"; 
	    	}

	    @PostMapping("/customer_login")
	    public String customer_login(@Valid @ModelAttribute("cus") customer_login_dto customer_login_dto,
	                        BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

	        if (result.hasErrors()) {
	        	   System.out.println(" Validation Errors");
	            return "customer_login";
	        }
	        String cus_cell = customer_login_dto.getCus_cell();
	        String cus_pass = AES.Encrypt(customer_login_dto.getCus_pass());
	      

	        Map<String, String> cus_log_res = customerRepository.customer_login(cus_cell, cus_pass);

	        if (cus_log_res == null || cus_log_res.get("result") == null) {
	            model.addAttribute("msg", "Invalid cell num or Password");
	            return "customer_login";
	        }

	        String res = cus_log_res.get("result");
	        String cus_name = cus_log_res.get("cus_name");
	        String cus_id = cus_log_res.get("cus_id");
	        System.out.println(" SP RETURN RES = " + res);


	        if (res != null && res.toLowerCase().contains("sucess")) {   

	    

	                //  SESSION SAVE DEFINITELY
	                session.setAttribute("cus_name", cus_name);
	                session.setAttribute("cus_id", cus_id);

	                System.out.println(" SESSION STORED:");
	                System.out.println("cus_name = " + cus_name);
	                System.out.println("cus_id = " + cus_id);

	                return "redirect:/customer_dash";
	            } 
	            else {
	                model.addAttribute("res", res);
	                return "customer_login";
	            }

	        } 
	    
	    
	    @GetMapping("/customer_dash")
	    public String customer_dash(HttpSession session, Model model) {
	    	String cus_name = (String)session.getAttribute("cus_name");
	    	String cus_id= (String)session.getAttribute("cus_id");
	    	
	    	model.addAttribute("cus_name", cus_name);
	    	model.addAttribute("cus_id", cus_id);
	    	
	    	if(cus_name == null) {
	    		return "redirect:/customer_login";
	    	}
	      
	        return "customer_dash";
	    }
	     
	    
	    @GetMapping("/customer_logout")
	    public String customer_logout(HttpSession session, RedirectAttributes redirectAttributes) {
	        session.invalidate();  
	        redirectAttributes.addFlashAttribute("msg", "Logout Successfully");
	        return "redirect:/customer_login";
	  }
	    
	    @GetMapping("/customer_reset")
	    public String showCustomerReset( Model model) {

	        model.addAttribute("customer_signup_dto", new customer_signup_dto());
	        model.addAttribute("res","WELCOME");
	        return "customer_reset"; 
	    }


	    @PostMapping("/customer_reset")
	    public String customer_reset(HttpSession session, Model model,
	                                 RedirectAttributes redirectAttributes) {

	        String cus_cell = (String) session.getAttribute("cus_cell");

	        if (cus_cell == null) {
	            redirectAttributes.addFlashAttribute("res", "please login--");
	            return "redirect:/customer_login";
	        }

	        return "customer_dash";
	    }
	    
	    
	    
	    
	    @GetMapping("/customer_search")
	    public String customer_search_get() {
	        return "redirect:/customer_reset";
	    }
	    
	    
	    @PostMapping("/customer_search")
	    public String customer_search(HttpSession session,Model model,
	    		@RequestParam("cus_id")String cus_id) {
	    	  Map<String, Object> response = customerRepository.customer_search(cus_id);
	    	customer_model  cus = (customer_model)response.get("cus");
	    	String resultmsg  = (String) response.get("res");
	    	
	    	if(cus!=null) {
	    		customer_signup_dto dto = new customer_signup_dto();
	    		dto.setCus_id(cus.getCus_id());
	    		dto.setCus_name(cus.getCus_name());
	    		dto.setCus_email(cus.getCus_email());
	    		dto.setCus_cell(cus.getCus_cell());
	    		String decryptedPass = AES.Decrypt(cus.getCus_pass());

	    		System.out.println("Encrypted from DB: " + cus.getCus_pass());
	    		System.out.println("Decrypted value   : " + decryptedPass);

	    		dto.setCus_pass(cus_id);
	    		
	    		model.addAttribute("customer_signup_dto",dto);	
	    	}
	    	else {
	    		model.addAttribute("customer_signup_dto", new customer_signup_dto() );
	    		
	    	}
	    	model.addAttribute("res", resultmsg);
	    	return "customer_reset";	
	    }
	    
	    @PostMapping("/customer_update")
	    public String customer_update(
	            HttpSession session,
	            @Valid @ModelAttribute("customer_update_dto") customer_update_dto dto,
	            BindingResult result,
	            RedirectAttributes redirectAttributes,
	            Model model) {

	        String cus_id = (String) session.getAttribute("cus_id");

	        if (cus_id == null) {
	            redirectAttributes.addFlashAttribute("msg", "Please login first");
	            return "redirect:/customer_login";
	        }

	        if (result.hasErrors()) {
	            return "customer_reset";
	        }

	        String cus_pass = AES.Encrypt(dto.getCus_pass());
	        String idt = LocalDateTime.now()
	                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	        String res = customerRepository.customer_update(
	                cus_id,
	                dto.getCus_name(),
	                dto.getCus_email(),
	                dto.getCus_cell(),
	                cus_pass,
	                idt
	        );

	        System.out.println("UPDATE RESULT = " + res);

	        redirectAttributes.addFlashAttribute("msg", res);
	        return "redirect:/customer_reset";
	    }
	    
	    
	    
	    @PostMapping("/customer_delete")
	    public String employee_delete(HttpSession session,
	    		Model model ,@RequestParam("cus_id")String cus_id) {
	    	String response = customerRepository.customer_delete(cus_id);
	    	
	    	model.addAttribute("msg",response);
	    	model.addAttribute("customer_signup_dto",new customer_signup_dto());
	    	return "customer_reset";
	    
	    }
	    
	    
	    
	    
	    @GetMapping("/customer_all")
	    public String customer_all(HttpSession session,
	                               Model model,
	                               RedirectAttributes redirectAttributes) {

	        String cus_name = (String) session.getAttribute("cus_name");

	        if (cus_name == null) {
	            redirectAttributes.addFlashAttribute("msg", "Please login first");
	            return "redirect:/customer_login";
	        }

	        model.addAttribute("cus_name", cus_name);

	        List<customer_model> all_cus = customerRepository .cus_all();

	        for (customer_model c : all_cus) {
	            String dec = AES.Decrypt(c.getCus_pass());
	            c.setCus_pass(dec);
	        }

	        model.addAttribute("all_cus", all_cus);

	        return "employee_all";
	    }

	    
	}
	       

	    
	    

