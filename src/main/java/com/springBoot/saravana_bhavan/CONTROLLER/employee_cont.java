package com.springBoot.saravana_bhavan.CONTROLLER;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.springBoot.saravana_bhavan.DTO.employee_login_dto;
import com.springBoot.saravana_bhavan.DTO.employee_signup_dto;
import com.springBoot.saravana_bhavan.MODEL.customer_model;
import com.springBoot.saravana_bhavan.MODEL.employee_model;
import com.springBoot.saravana_bhavan.REPO.customer_repository;
import com.springBoot.saravana_bhavan.REPO.employee_repository;
import com.springBoot.saravana_bhavan.project_help.AES;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import org.springframework.ui.Model;

@Controller
public class employee_cont {

    @Autowired
    private employee_repository employeeRepository;

    @Autowired
    private customer_repository customerRepository;
    
    @GetMapping("/home")
    public String home(){
        return "home";
    }
    
   
  
    @GetMapping("/employee_login") 
    public String showLogin(Model model) {
    	model.addAttribute("emp", new employee_login_dto());
    	return "employee_login"; 
    	}

    @PostMapping("/employee_login")
    public String login(@Valid @ModelAttribute("emp") employee_login_dto employee_login_dto,
                        BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

        if (result.hasErrors()) {
        	   System.out.println("❌ Validation Errors");
            return "employee_login";
        }

        String emp_pass = AES.Encrypt(employee_login_dto.getEmp_pass());
        String emp_email = employee_login_dto.getEmp_email();

        Map<String, String> emp_log_res = employeeRepository.employee_login(emp_email, emp_pass);

        if (emp_log_res == null || emp_log_res.get("result") == null) {
            model.addAttribute("msg", "Invalid Email or Password");
            return "employee_login";
        }

        String res = emp_log_res.get("result");
        String emp_role = emp_log_res.get("emp_role");
        String emp_name = emp_log_res.get("emp_name");
        System.out.println("👉 SP RETURN RES = " + res);


        if (res != null && res.toLowerCase().contains("sucess")) {   

            if ("admin".equalsIgnoreCase(emp_role)) {

                // ✅ SESSION SAVE DEFINITELY
                session.setAttribute("emp_name", emp_name);
                session.setAttribute("emp_role", emp_role);

                System.out.println("✅ SESSION STORED:");
                System.out.println("emp_name = " + emp_name);
                System.out.println("emp_role = " + emp_role);

                return "redirect:/employee_dash";
            } 
            else {
                model.addAttribute("msg", "Only admin allowed");
                return "employee_login";
            }

        } 
        else {
            System.out.println("❌ LOGIN FAILED RES = " + res);
            model.addAttribute("msg", res);
            return "employee_login";
        }
}
       

    

 
    @GetMapping("/employee_signup")
    public String showSignup(Model model) {
        model.addAttribute("employee_signup_dto", new employee_signup_dto());
        return "employee_signup";
    }

    @PostMapping("/employee_signup")
    public String signup(@Valid @ModelAttribute("employee_signup_dto") employee_signup_dto emp,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            return "employee_signup";
        }

        String emp_role = "";

        // Role validate
        if (emp.getEmp_email().contains(".adm@gmail.com")) {
            emp_role = "admin";
        } else if (emp.getEmp_email().contains(".emp@gmail.com")) {
            emp_role = "employee";
        } else {
            model.addAttribute("err","Invalid email format: use .emp@gmail.com or .adm@gmail.com");
            return "employee_signup";
        }

        // Encrypt pass
        String enc_pass = AES.Encrypt(emp.getEmp_pass());

        // timestamp
        String idt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-| HH:mm:ss"));

        // Call repo
        String sp_res = employeeRepository.employee_signup(
                emp.getEmp_id(),
                emp.getEmp_name(),
                emp.getEmp_email(),
                emp.getEmp_cell(),
                enc_pass,
                emp_role,
                idt
        );

        if(sp_res.contains("EMPLOYEE DATA inserted")) {
            // Flash attribute for redirect → clears form automatically
            redirectAttributes.addFlashAttribute("msg", sp_res);
            return "redirect:/employee_signup";  // redirect to same page
        } else {
            model.addAttribute("msg", sp_res);   // show DB error on same page
            return "employee_signup";
        }
    }
    
    
    @GetMapping("/employee_dash")
    public String dash(HttpSession session,Model model,
   		 RedirectAttributes redirectAttributes) {
    	String emp_name = (String)session.getAttribute("emp_name");
    	String emp_role= (String)session.getAttribute("emp_role");
    	
    	model.addAttribute("emp_role", emp_role);
    	model.addAttribute("emp_name", emp_name);
    	
    	if(emp_name == null) {
    		redirectAttributes.addFlashAttribute("res","pls login in");
    		return "redirect:/employee_login";
    	}
    	List<customer_model> all_cus =  customerRepository.cus_all();
    	model.addAttribute("all_cus", all_cus);
      
        return "employee_dash";
    }

    
    
    
    @PostMapping("/emp_reset")
     public String employee_dash(HttpSession session,Model model,
    		 RedirectAttributes redirectAttributes) {
    	 
    	 String emp_name =(String)session.getAttribute("emp_name");
    	 String emp_role = (String)session.getAttribute("emp_role");
    	 
    	 if(emp_name == null) {
    		 redirectAttributes.addFlashAttribute("res","please login--");
    		 return "redirect:/employee_login";
    	 }
    	 return "employee_dash";	
     }
    
    
    
    @GetMapping("/employee_search")
    public String employee_search_get() {
        return "redirect:/emp_reset";
    }
    
    
    @PostMapping("/employee_search")
    public String employee_search(HttpSession session,Model model,
    		@RequestParam("emp_id")String emp_id) {
    	  Map<String, Object> response = employeeRepository.emp_search(emp_id);
    	employee_model  emp = (employee_model)response.get("emp");
    	String resultmsg  = (String) response.get("res");
    	
    	if(emp!=null) {
    		employee_signup_dto dto = new employee_signup_dto();
    		dto.setEmp_id(emp.getEmp_id());
    		dto.setEmp_name(emp.getEmp_name());
    		dto.setEmp_email(emp.getEmp_email());
    		dto.setEmp_cell(emp.getEmp_cell());
    		String decryptedPass = AES.Decrypt(emp.getEmp_pass());

    		System.out.println("Encrypted from DB: " + emp.getEmp_pass());
    		System.out.println("Decrypted value   : " + decryptedPass);

    		dto.setEmp_pass(decryptedPass);


    		dto.setEmp_role(emp.getEmp_role());
    		
    		model.addAttribute("employee_signup_dto",dto);	
    	}
    	else {
    		model.addAttribute("employee_signup_dto", new employee_signup_dto() );
    		
    	}
    	model.addAttribute("res", resultmsg);
    	return "emp_reset";	
    }
      
   

    @PostMapping("/employee_update")
    public String employee_update(Model model,
            @Valid @ModelAttribute("employee_signup_dto") employee_signup_dto employee_signup_dto,
            BindingResult result) {

        if(result.hasErrors()) {
            model.addAttribute("employee_signup_dto", employee_signup_dto);
            return "emp_reset";
        }

        String emp_role;
        if (employee_signup_dto.getEmp_email().endsWith(".adm@gmail.com")) {
            emp_role = "admin";
        } else if (employee_signup_dto.getEmp_email().endsWith(".emp@gmail.com")) {
            emp_role = "employee";
        } else {
            model.addAttribute("res", "Invalid Email Format!\n*.adm@gmail.com = ADMIN\n*.emp@gmail.com = EMPLOYEE");
            return "emp_reset";
        }

        String emp_pass = AES.Encrypt(employee_signup_dto.getEmp_pass());
        String idt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String res = employeeRepository.employee_update(
                employee_signup_dto.getEmp_id(),
                employee_signup_dto.getEmp_name(),
                employee_signup_dto.getEmp_email(),
                employee_signup_dto.getEmp_cell(),
                emp_pass,
                emp_role,
                idt);

        model.addAttribute("res", res);

        model.addAttribute("employee_signup_dto", new employee_signup_dto());

        return "emp_reset";
    }
    
    
    @GetMapping("/employee_all")
    public String employee_all(HttpSession session,
    		Model model ,RedirectAttributes redirectAttributes) {
    	String emp_name = (String)session.getAttribute("emp_name");	
     	String emp_role = (String)session.getAttribute("emp_role");
     	
     	model.addAttribute("emp_role",emp_role);
     	model.addAttribute("emp_name",emp_name);
     	
     	if(emp_name == null) {
     		redirectAttributes.addFlashAttribute("msg","pls login--");
            return "redirect:/employee_login"; 
     	}
     	List<employee_model> all_emp =  employeeRepository.emp_all();
     	for(employee_model e :all_emp) {
     	String enc = e.getEmp_pass();
     	String dec = AES.Decrypt(enc);
     	e.setEmp_pass(dec);
     	}
     	model.addAttribute("all_emp",all_emp);
     	return "employee_all";
    	 
}
    
    @PostMapping("/employee_delete")
    public String employee_delete(HttpSession session,
    		Model model ,@RequestParam("emp_id")String emp_id) {
    	String response = employeeRepository.employee_delete(emp_id);
    	
    	model.addAttribute("res",response);
    	model.addAttribute("employee_signup_dto",new employee_signup_dto());
    	return "emp_reset";
    
    }
    	


  @GetMapping("/emp_reset")
   public String empReset(Model model) {
  
      model.addAttribute("employee_signup_dto", new employee_signup_dto());
      model.addAttribute("res", "");
       return "emp_reset";
   }



   @GetMapping("/emp_logout")
  public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
      session.invalidate();  
      redirectAttributes.addFlashAttribute("msg", "Logout Successfully");
      return "redirect:/employee_login";
}
}
    





