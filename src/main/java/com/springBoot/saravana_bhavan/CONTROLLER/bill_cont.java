package com.springBoot.saravana_bhavan.CONTROLLER;

import java.net.http.HttpClient;
import java.sql.CallableStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.OutputStream;

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
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Element;

import com.springBoot.saravana_bhavan.DTO.customer_signup_dto;
import com.springBoot.saravana_bhavan.MODEL.cart_model;
import com.springBoot.saravana_bhavan.REPO.food_repository;

import jakarta.servlet.http.HttpServletResponse;
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
	        RedirectAttributes ra,Model model
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

	    return "redirect:/cart";

	}
	
	@GetMapping("/cart")
	public String cartPage(HttpSession session , Model model){

	    List<cart_model> cart = (List<cart_model>) session.getAttribute("cart");

	    if(cart == null){
	        cart = new ArrayList<>();
	    }

	    model.addAttribute("cart", cart);

	    double grandTotal = 0;
	    for(cart_model c: cart){
	        grandTotal += c.getFood_total();
	    }

	    model.addAttribute("grand_total", grandTotal);

	    return "cart";
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
	    session.removeAttribute("customer");

	    session.setAttribute("lastBillNo", billNo);

	    ra.addFlashAttribute("res","Bill Generated Successfully | Bill No : " + billNo);
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

	
	@GetMapping("/bill_pdf")
	public void billPdf(@RequestParam int billNo,
	                    HttpServletResponse response){

	    try {
	        response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition",
	                "inline; filename=bill_"+billNo+".pdf");

	        OutputStream out = response.getOutputStream();

	        Document doc = new Document(PageSize.A4);
	        PdfWriter.getInstance(doc, out);
	        doc.open();

	        // ====== FONTS ======
	        Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.DARK_GRAY);
	        Font subFont   = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
	        Font headFont  = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
	        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

	        // ====== HEADER ======
	        Paragraph title = new Paragraph("SARAVANA BHAVAN", titleFont);
	        title.setAlignment(Element.ALIGN_CENTER);
	        doc.add(title);

	        Paragraph billTitle = new Paragraph("CUSTOMER BILL", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
	        billTitle.setAlignment(Element.ALIGN_CENTER);
	        doc.add(billTitle);

	        doc.add(new Paragraph("\n"));

	        // ====== BILL INFO ======
	        doc.add(new Paragraph("Bill No : " + billNo, subFont));
	        doc.add(new Paragraph("Date     : " + LocalDate.now(), subFont));
	        doc.add(new Paragraph("-------------------------------------------------------------"));

	        doc.add(new Paragraph("\n"));

	        // ====== TABLE ======
	        PdfPTable table = new PdfPTable(4);
	        table.setWidthPercentage(100);
	        table.setSpacingBefore(10f);
	        table.setSpacingAfter(10f);
	        table.setWidths(new float[]{3, 2, 2, 2});

	        BaseColor headerColor = new BaseColor(34, 197, 94); // green stylish

	        PdfPCell h1 = new PdfPCell(new Phrase("Food", headFont));
	        PdfPCell h2 = new PdfPCell(new Phrase("Price", headFont));
	        PdfPCell h3 = new PdfPCell(new Phrase("Qty", headFont));
	        PdfPCell h4 = new PdfPCell(new Phrase("Total", headFont));

	        h1.setBackgroundColor(headerColor);
	        h2.setBackgroundColor(headerColor);
	        h3.setBackgroundColor(headerColor);
	        h4.setBackgroundColor(headerColor);

	        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
	        h4.setHorizontalAlignment(Element.ALIGN_CENTER);

	        table.addCell(h1);
	        table.addCell(h2);
	        table.addCell(h3);
	        table.addCell(h4);

	        // ====== GET DB DATA ======
	        List<Map<String,Object>> items =
	                jdbcTemplate.queryForList(
	                        "select f.food_name ,bd.price ,bd.qty ,bd.total " +
	                        "from bill_details bd join food_tbl f " +
	                        "on bd.food_id=f.food_id where bill_no=?",
	                        billNo
	                );

	        double grand = 0;

	        for(Map<String,Object> r : items){

	            PdfPCell c1 = new PdfPCell(new Phrase(r.get("food_name").toString(), normalFont));
	            PdfPCell c2 = new PdfPCell(new Phrase("₹ " + r.get("price"), normalFont));
	            PdfPCell c3 = new PdfPCell(new Phrase(r.get("qty").toString(), normalFont));
	            PdfPCell c4 = new PdfPCell(new Phrase("₹ " + r.get("total"), normalFont));

	            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
	            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
	            c4.setHorizontalAlignment(Element.ALIGN_CENTER);

	            table.addCell(c1);
	            table.addCell(c2);
	            table.addCell(c3);
	            table.addCell(c4);

	            grand += Double.parseDouble(r.get("total").toString());
	        }

	        doc.add(table);

	        // ====== GRAND TOTAL ======
	        Paragraph gt = new Paragraph("Grand Total : ₹" + grand,
	                new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.RED));
	        gt.setAlignment(Element.ALIGN_RIGHT);
	        doc.add(gt);

	        doc.add(new Paragraph("\n"));

	        Paragraph thank = new Paragraph("Thank You! Visit Again ❤️",
	                new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE));
	        thank.setAlignment(Element.ALIGN_CENTER);
	        doc.add(thank);

	        doc.close();
	        out.close();

	    } catch (Exception e){
	        e.printStackTrace();
	    }
	}




	
}

	


