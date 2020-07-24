/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import DAO.CustomerDAO;
import javax.servlet.http.HttpSession;
import models.*;
import DAO.*;
import java.sql.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author phamq
 */
@Controller
@RequestMapping("/auth")
public class UserController {

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String LoginAction(String id, ModelMap mm) {//
        mm.put("tk", new User());
        return "auth";
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public String LoginAction(@ModelAttribute(value = "tk") User user, ModelMap mm, HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        String emails = user.getEmail();
        String pass = user.getPassword();
        UserDAO udao = new UserDAO();
        User check = udao.Login(emails, pass);
//        int id = user.getuId();
//        String name = user.getUsername();
        if (check != null) {
            Cookie emailCookie = new Cookie("ID", String.valueOf(check.getuId()));
            emailCookie.setMaxAge(60 * 60 * 24 * 365);
            Cookie nameCookie = new Cookie("Name", check.getUsername());
            nameCookie.setMaxAge(60 * 60 * 24 * 365);
            emailCookie.setPath("/");
            nameCookie.setPath("/");
            response.addCookie(emailCookie);
            response.addCookie(nameCookie);
//            request.getSession().setAttribute("ID", id);
//            request.getSession().setAttribute("Name", name);
            //          return "redirect:/";
            switch (check.getPremission()) {
                case 0: {
                    try {
                        String url = request.getParameter("returnURL");
                        if(url != null) {
                            return ("redirect:/" + url);
                        }
                        else {
                            return "redirect:/";
                        }
                    } catch (Exception e) {
                        return "redirect:/";
                    }
                }
                case 2:
                    return "redirect:/admins/filmList.html";
                default:
                    return "redirect:/";
            }
        } else {
            mm.put("message", "Không hợp lệ");
            return "redirect:/auth.html";
        }
    }

//    @RequestMapping(value = {""}, method = RequestMethod.GET)
//    public String RegisAction(ModelMap mm) {//
//        mm.put("dktk", new User());
//        return "auth";
//    }
    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public String RegisAction(@ModelAttribute(value = "tk") User user, ModelMap mm, HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        UserDAO udao = new UserDAO();
        CustomerDAO cdao = new CustomerDAO();
        if (request.getParameter("txtconfirmpass").equals(request.getParameter("txtPass"))) {
            udao.InsertUser(request.getParameter("txtName"), request.getParameter("txtEmail"), request.getParameter("txtPass"), Date.valueOf(request.getParameter("txtDate")), request.getParameter("txtAddress"), request.getParameter("txtPhone"));
            cdao.InsertCustomers();
            return "redirect:/";
        } else {
//        {mm.put("message", "Mật khẩu không hợp lệ");
            return "auth";
        }

    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    public String LogoutAction(@ModelAttribute(value = "tk") User user, ModelMap mm, HttpSession session, HttpServletResponse response, HttpServletRequest request) {

        Cookie[] list = request.getCookies();

        for (Cookie items : list) {
            if (items.getName().equals("Name")) {
                items.setMaxAge(0);
                items.setPath("/");
                response.addCookie(items);
            }
            if (items.getName().equals("ID")) {
                items.setMaxAge(0);
                items.setPath("/");
                response.addCookie(items);
            }
        }

        return "redirect:/";
    }
}
