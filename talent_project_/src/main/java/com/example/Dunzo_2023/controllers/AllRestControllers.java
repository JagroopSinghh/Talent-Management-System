/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.Dunzo_2023.controllers;

import com.example.Dunzo_2023.vmm.DBLoader;
import com.example.Dunzo_2023.vmm.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AllRestControllers {

    @PostMapping("/Emplogin")
    public String adminLogin(@RequestParam String mail, @RequestParam String password, HttpSession session) {

        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("select * from employee_personal_data where E_mail= '" + mail + "' and E_password = '" + password + "' ");
            if (rs.next()) {
                session.setAttribute("mail", mail);
                ans = "success";
            } else {
                ans = "fail";
            }
        } catch (Exception e) {
            e.toString();
        }

        return ans;

    }
    @GetMapping("/fetchuser")
    public Map<String, String> getUser(HttpSession session) {
        String user = (String) session.getAttribute("mail");
        Map<String, String> response = new HashMap<>();
        response.put("username", user != null ? user : "No user logged in");
        return response;
    }
    
    
     @PostMapping("/addjobposting")
    public String addjobposting(@RequestParam String job_name,
            @RequestParam String job_desc,@RequestParam String salary,@RequestParam String vacancy) {
        String ans = "";
        try {
           
            ResultSet rs = DBLoader.executeSQL("select * from job_posting");
  

                rs.moveToInsertRow();
                rs.updateString("job_name", job_name);
                 rs.updateString("descr", job_desc);
                  rs.updateString("salary", salary);
                   rs.updateString("vacancies", vacancy);
                rs.insertRow();
                ans = "success";
            
        } catch (Exception e) {
            return e.toString();
        }
        return ans;
    }
    
    @PostMapping("/addfeedback")
    public String addfeedback(@RequestParam String ftype,
            @RequestParam String feedback) {
        String ans = "";
        try {
           
            ResultSet rs = DBLoader.executeSQL("select * from feedback");
  

                rs.moveToInsertRow();
                rs.updateString("feedback_type", ftype);
                 rs.updateString("mess", feedback);
                 
                rs.insertRow();
                ans = "success";
            
        } catch (Exception e) {
            return e.toString();
        }
        return ans;
    }

     @PostMapping("/addcourse")
    public String addcourse(@RequestParam String c_name,
            @RequestParam String c_info) {
        String ans = "";
        try {
           
            ResultSet rs = DBLoader.executeSQL("select * from coursename");
  

                rs.moveToInsertRow();
                rs.updateString("course_name", c_name);
                 rs.updateString("descr", c_info);
                 
                rs.insertRow();
                ans = "success";
            
        } catch (Exception e) {
            return e.toString();
        }
        return ans;
    }
    
    @PostMapping("/showfeedback")
    public String showfeedback() {

        String ans = new RDBMS_TO_JSON().generateJSON("select * from feedback");

        return ans;
    }

    @PostMapping("/resgisterEmp")
    public String resgisterEmp(@RequestParam String fullname,
            @RequestParam String phone,@RequestParam String address,@RequestParam String email,@RequestParam String position,@RequestParam String password) {
        String ans = "";
        try {
           
            ResultSet rs = DBLoader.executeSQL("select * from employee_personal_data");
  

                rs.moveToInsertRow();
                rs.updateString("E_name", fullname);
                 rs.updateString("E_phone", phone);
                  rs.updateString("E_address", address);
                   rs.updateString("E_mail", email);
                    rs.updateString("E_post", position);
                     rs.updateString("E_password", password);
              System.out.print(position);
                rs.insertRow();
                ans = "success";
            
        } catch (Exception e) {
            return e.toString();
        }
        return ans;
    }

    @PostMapping("/showEmployeeProfile")
    public String showEmployeeProfile(@RequestParam String mail) {

        String ans = new RDBMS_TO_JSON().generateJSON("select * from employee_personal_data where E_mail = '" + mail + "'");

        return ans;
    }
    
    @PostMapping("/showjobposting")
    public String showjobposting() {

        String ans = new RDBMS_TO_JSON().generateJSON("select * from job_posting");

        return ans;
    }
    @PostMapping("/updateprofileinfo")
    public String updateprofileinfo(@RequestParam String newmail, @RequestParam String newaddress, @RequestParam String newphone,@RequestParam String mail) {
        String ans;
        
        try {
           ResultSet rs = DBLoader.executeSQL("select * from employee_personal_data where E_mail = '" + mail + "'");
            if(rs.next()){
                rs.updateString("E_phone", newphone);
                rs.updateString("E_address", newaddress);
                rs.updateString("E_mail", newmail);
                rs.updateRow();
            }
                ans = "success";
            
        } catch (Exception e) {
            return e.toString();
        }
         return ans;   
    }
    @PostMapping("adminDeleteCategory")
    public String adminDeleteCategory(@RequestParam int catid) {

        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("select * from category where catid='" + catid + "' ");
            if (rs.next()) {
                rs.deleteRow();
                ans = "success";
            }
        } catch (Exception e) {
            e.toString();
        }
        return ans;
    }

    @PostMapping("/adminshowDropdown")
    public String adminshowDropdown() {

        String ans = new RDBMS_TO_JSON().generateJSON("select * from category");

        return ans;
    }

    @PostMapping("/adminAddProduct")
    public String adminAddProduct(@RequestParam String pname,
            @RequestParam String desc, @RequestParam String price, @RequestParam String offerprice,
            @RequestParam int catid,
            @RequestParam MultipartFile photo) {
        System.out.println(photo);
        String catname = "";

        String ans = "";
        try {
            ResultSet rs2 = DBLoader.executeSQL("select catname from category where catid='" + catid + "' ");
            if (rs2.next()) {
                catname = rs2.getString("catname");
            }

            ResultSet rs = DBLoader.executeSQL("select * from products where pname='" + pname + "' ");
            if (rs.next()) {
                ans = "fail";
            } else {

                File f = new File("src/main/webapp/myuploads/" + photo.getOriginalFilename());
                byte b[] = photo.getBytes();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(b);
                fos.close();

                rs.moveToInsertRow();
                rs.updateString("pname", pname);
                rs.updateString("desc", desc);
                rs.updateString("price", price);
                rs.updateString("offerprice", offerprice);
                rs.updateInt("catid", catid);
                rs.updateString("catname", catname);
                rs.updateString("photo", "../myuploads/" + photo.getOriginalFilename());
                rs.insertRow();
                ans = "success";
            }
        } catch (Exception e) {
            return e.toString();
        }
        return ans;
    }

    @PostMapping("/adminshowProducts")
    public String adminshowProducts() {

        String ans = new RDBMS_TO_JSON().generateJSON("select * from products");
        return ans;

    }

    @PostMapping("adminDeleteProduct")
    public String adminDeleteProduct(@RequestParam int pid) {

        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("select * from products where pid='" + pid + "' ");
            if (rs.next()) {
                rs.deleteRow();
                ans = "success";
            }
        } catch (Exception e) {
            e.toString();
        }
        return ans;
    }

    @PostMapping("/adminAddProductPhotos")
    public String adminAddProductPhotos(@RequestParam int pid,
            @RequestParam MultipartFile photo) {
        System.out.println(photo);

        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("select * from photos");

            File f = new File("src/main/webapp/myuploads/" + photo.getOriginalFilename());
            byte b[] = photo.getBytes();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(b);
            fos.close();

            rs.moveToInsertRow();
            rs.updateInt("pid", pid);
            rs.updateString("photo", "../myuploads/" + photo.getOriginalFilename());
            rs.insertRow();
            ans = "success";

        } catch (Exception e) {
            return e.toString();
        }
        return ans;
    }

    @PostMapping("/adminshowProductPhotos")
    public String adminshowProductPhotos(@RequestParam int pid) {

        String ans = new RDBMS_TO_JSON().generateJSON("select * from photos where pid = '" + pid + "' ");

        return ans;
    }

    @PostMapping("adminDeleteProductPhoto")
    public String adminDeleteProductPhoto(@RequestParam int photoid) {

        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("select * from photos where photoid='" + photoid + "' ");
            if (rs.next()) {
                rs.deleteRow();
                ans = "success";
            }
        } catch (Exception e) {
            e.toString();
        }
        return ans;
    }

    @PostMapping("/userSignup")
    public String userSignup(@RequestParam String username, @RequestParam String useremail,
            @RequestParam String password, @RequestParam String address,
            @RequestParam String phoneno, @RequestParam MultipartFile photo) {

        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("select * from users where useremail ='" + useremail + "' ");
            if (rs.next()) {
                ans = "fail";
            } else {

                File f = new File("src/main/webapp/myuploads/" + photo.getOriginalFilename());
                byte b[] = photo.getBytes();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(b);
                fos.close();

                rs.moveToInsertRow();
                rs.updateString("username", username);
                rs.updateString("useremail", useremail);
                rs.updateString("password", password);
                rs.updateString("address", address);
                rs.updateString("phoneno", phoneno);
                rs.updateString("photo", "../myuploads/" + photo.getOriginalFilename());
                rs.insertRow();

                ans = "success";

            }
        } catch (Exception e) {
            return e.toString();
        }

        return ans;

    }

    @PostMapping("/userlogin")
    public String userlogin(@RequestParam String useremail, @RequestParam String password, HttpSession session) {

        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("select * from users where useremail= '" + useremail + "' and password = '" + password + "' ");
            if (rs.next()) {
                session.setAttribute("useremail", useremail);
                ans = "success";
            } else {
                ans = "fail";
            }
        } catch (Exception e) {
            return e.toString();
        }

        return ans;

    }

    @PostMapping("/userShowCategories")
    public String userShowCategories() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from category");

        return ans;
    }

    @PostMapping("/userShowProducts")
    public String userShowProducts(@RequestParam int catid) {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from products where catid='" + catid + "' ");

        return ans;
    }

    @PostMapping("/userShowProductDetail")
    public String userShowProductDetail(@RequestParam int pid) {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from products where pid='" + pid + "' ");

        return ans;
    }

    @PostMapping("/userAddToCart")
    public String userAddToCart(@RequestParam int pid, HttpSession session) {
        String pname = "";
        String photo = "";
        int price = 0;
        int quantity = 1;
        String ans = "";
        try {
            String useremail = (String) session.getAttribute("useremail");
            ResultSet rs = DBLoader.executeSQL("select * from products where pid='" + pid + "' ");
            if (rs.next()) {
                pname = rs.getString("pname");
                photo = rs.getString("photo");
                price = rs.getInt("price");
            }

            ResultSet rs2 = DBLoader.executeSQL("select * from cart where pid='" + pid + "' and useremail ='" + useremail + "' ");
            if (rs2.next()) {
                ans = "fail";
            } else {

                rs2.moveToInsertRow();
                rs2.updateString("pname", pname);
                rs2.updateString("photo", photo);
                rs2.updateInt("pid", pid);
                rs2.updateInt("perprice", price);
                rs2.updateInt("quantity", quantity);
                rs2.updateInt("total", price);
                rs2.updateString("useremail", useremail);
                rs2.insertRow();

                ans = "success";
            }

        } catch (Exception e) {
            return e.toString();
        }
        return ans;
    }

    @PostMapping("/userShowCart")
    public String userShowCart(HttpSession session) {
        String useremail = (String) session.getAttribute("useremail");
        String ans = new RDBMS_TO_JSON().generateJSON("select * from cart where useremail='" + useremail + "' ");

        return ans;
    }

    @PostMapping("/userIncreaseCart")
    public String userIncreaseCart(@RequestParam int pid, HttpSession session) {
        String useremail = (String) session.getAttribute("useremail");
        int perprice = 0;
        int total = 0;
        int quantity = 0;
        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("select * from cart where pid='" + pid + "' and useremail='" + useremail + "'  ");
            if(rs.next()){
                perprice = rs.getInt("perprice");
                total = rs.getInt("total");
                quantity = rs.getInt("quantity");
            }
            
            quantity = quantity + 1;
            total = total + perprice;
            
            ResultSet rs2 = DBLoader.executeSQL("select * from cart where pid='" + pid + "' and useremail='" + useremail + "'  ");
            if(rs2.next()){
                rs2.updateInt("quantity", quantity);
                rs2.updateInt("total", total);
                rs2.updateRow();
                
                ans = "success";
            }
            
            
        } catch (Exception e) {
            return e.toString();
        }
return ans;
    }
    
    
    @PostMapping("/userDecreasesCart")
    public String userDecreasesCart(@RequestParam int pid, HttpSession session) {
        String useremail = (String) session.getAttribute("useremail");
        int perprice = 0;
        int total = 0;
        int quantity = 0;
        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("select * from cart where pid='" + pid + "' and useremail='" + useremail + "'  ");
            if(rs.next()){
                perprice = rs.getInt("perprice");
                total = rs.getInt("total");
                quantity = rs.getInt("quantity");
            }
            quantity = quantity - 1;
            
            if(quantity == 0){
                
                ResultSet rs3 = DBLoader.executeSQL("select * from cart where pid='" + pid + "' and useremail='" + useremail + "' ");
                if(rs3.next()){
                    rs3.deleteRow();
                    return "success";
                }
                
            }else{
            
            total = total - perprice;
            
            ResultSet rs2 = DBLoader.executeSQL("select * from cart where pid='" + pid + "' and useremail='" + useremail + "'  ");
            if(rs2.next()){
                rs2.updateInt("quantity", quantity);
                rs2.updateInt("total", total);
                rs2.updateRow();
                
                ans = "success";
            }
            }
            
        } catch (Exception e) {
            return e.toString();
        }
return ans;
    }
    
    
    @PostMapping("/userShowGTotal")
    public String userShowGTotal(HttpSession session) {
        
        String useremail = (String) session.getAttribute("useremail");
        
        String ans = new RDBMS_TO_JSON().generateJSON("select SUM(total)as gtotal from cart where useremail='"+useremail+"' ");

        return ans;
        
    }
    
    @PostMapping("/userCashPayment")
    public String userCashPayment(HttpSession session) {
        String useremail =(String) session.getAttribute("useremail");
        String username = "";
        String phoneno = "";
        String address = "";
        String status = "Pending";
        String paymenttype = "Cash Payment";
        int orderid = 0;
        String ans = "";
        
        int pid = 0;
        int perprice = 0;
        int quantity = 0;
        int total = 0;
        String pname = "";
        try{
            
            ResultSet rs2 = DBLoader.executeSQL("select * from users where useremail ='"+useremail+"' ");
            if(rs2.next()){
                username = rs2.getString("username");
                phoneno = rs2.getString("phoneno");
                address = rs2.getString("address");
            }
            
        ResultSet rs = DBLoader.executeSQL("select * from orders");
        rs.moveToInsertRow();
        rs.updateString("username", username);
        rs.updateString("useremail", useremail);
        rs.updateString("phoneno", phoneno);
        rs.updateString("address", address);
        rs.updateString("status", status);
        rs.updateString("paymenttype", paymenttype);
        rs.insertRow();
        
        ResultSet rs3 = DBLoader.executeSQL("select MAX(orderid) as orderid from orders ");
        if(rs3.next()){
            orderid = rs3.getInt("orderid");
        }
        
        ResultSet rs4 = DBLoader.executeSQL("select * from cart where useremail ='"+useremail+"' ");
        while(rs4.next()){
            
            pid = rs4.getInt("pid");
            perprice = rs4.getInt("perprice");
            quantity = rs4.getInt("quantity");
            total = rs4.getInt("total");
            pname = rs4.getString("pname");
            int cartid = rs4.getInt("cartid");
            
            ResultSet rs5 = DBLoader.executeSQL("select * from orderdetail");
            
            rs5.moveToInsertRow();
            rs5.updateInt("orderid", orderid);
            rs5.updateInt("pid", pid);
            rs5.updateInt("perprice", perprice);
            rs5.updateInt("quantity", quantity);
            rs5.updateInt("total", total);
            rs5.updateString("pname", pname);
            rs5.updateString("useremail", useremail);
            rs5.insertRow();
            
            ResultSet rs6 = DBLoader.executeSQL("select * from cart where cartid='"+cartid+"' ");
            if(rs6.next()){
                rs6.deleteRow();
            }
            
            ans = "success";
        }
        
        }catch(Exception e){
            return e.toString();
        }
        return ans;
    }
    
    @PostMapping("/userOnlinePayment")
    public String userOnlinePayment(HttpSession session) {
        String useremail =(String) session.getAttribute("useremail");
        String username = "";
        String phoneno = "";
        String address = "";
        String status = "Pending";
        String paymenttype = "Online Payment";
        int orderid = 0;
        String ans = "";
        
        int pid = 0;
        int perprice = 0;
        int quantity = 0;
        int total = 0;
        String pname = "";
        try{
            
            ResultSet rs2 = DBLoader.executeSQL("select * from users where useremail ='"+useremail+"' ");
            if(rs2.next()){
                username = rs2.getString("username");
                phoneno = rs2.getString("phoneno");
                address = rs2.getString("address");
            }
            
        ResultSet rs = DBLoader.executeSQL("select * from orders");
        rs.moveToInsertRow();
        rs.updateString("username", username);
        rs.updateString("useremail", useremail);
        rs.updateString("phoneno", phoneno);
        rs.updateString("address", address);
        rs.updateString("status", status);
        rs.updateString("paymenttype", paymenttype);
        rs.insertRow();
        
        ResultSet rs3 = DBLoader.executeSQL("select MAX(orderid) as orderid from orders ");
        if(rs3.next()){
            orderid = rs3.getInt("orderid");
        }
        
        ResultSet rs4 = DBLoader.executeSQL("select * from cart where useremail ='"+useremail+"' ");
        while(rs4.next()){
            
            pid = rs4.getInt("pid");
            perprice = rs4.getInt("perprice");
            quantity = rs4.getInt("quantity");
            total = rs4.getInt("total");
            pname = rs4.getString("pname");
            int cartid = rs4.getInt("cartid");
            
            ResultSet rs5 = DBLoader.executeSQL("select * from orderdetail");
            
            rs5.moveToInsertRow();
            rs5.updateInt("orderid", orderid);
            rs5.updateInt("pid", pid);
            rs5.updateInt("perprice", perprice);
            rs5.updateInt("quantity", quantity);
            rs5.updateInt("total", total);
            rs5.updateString("pname", pname);
            rs5.updateString("useremail", useremail);
            rs5.insertRow();
            
            ResultSet rs6 = DBLoader.executeSQL("select * from cart where cartid='"+cartid+"' ");
            if(rs6.next()){
                rs6.deleteRow();
            }
            
            ans = "success";
        }
        
        }catch(Exception e){
            return e.toString();
        }
        return ans;
    }
    
    @PostMapping("/userShowOrders")
    public String userShowOrders(HttpSession session)
    {
        String useremail =(String) session.getAttribute("useremail");
        String orders = new RDBMS_TO_JSON().generateJSON("select * from (orders INNER JOIN orderdetail ON orders.orderid=orderdetail.orderid) INNER JOIN products ON orderdetail.pid=products.pid where orderdetail.useremail='"+useremail+"'");
        return orders;
    }
    
    @PostMapping("/adminManageOrders")
    public String adminManageOrders()
    {
        String orders = new RDBMS_TO_JSON().generateJSON("select * from orders");
        return orders;
    }
    
    
        @PostMapping("/adminApproveOrder")
    public String adminApproveOrder(@RequestParam int orderid) {

        String ans = "";
        String status = "Delivered";
        try {
            ResultSet rs = DBLoader.executeSQL("select * from orders where orderid='"+orderid+"' and status='Pending'  ");
            if(rs.next()){
                rs.updateString("status", status);
                rs.updateRow();
                ans = "success";
            }else{
                ans = "fail";
            }
        } catch (Exception e) {
            return e.toString();
        }

        return ans;

    }
    
    @PostMapping("/userShowProductPhotos")
    public String userShowProductPhotos(@RequestParam int pid)
    {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from photos where pid='"+pid+"' ");
        
        return ans;
    }
    

}
