package org.example.fileshibernate.web;

import org.example.fileshibernate.dao.UserDao;
import org.example.fileshibernate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Autowired
    UserDao userDao;

    @RequestMapping("/login")
    public String logInForm(Model model, HttpServletRequest request) {
        try {
            Object flash = request.getSession().getAttribute("flash");
            model.addAttribute("flash", flash);
            model.addAttribute("user", new User());
            request.getSession().removeAttribute("flash");
        } catch (Exception ex) {
            // Do nothing
        }

        return "login";
    }



}
