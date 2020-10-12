package org.example.fileshibernate.web;

import org.example.fileshibernate.dto.PasswordDto;
import org.example.fileshibernate.dto.SecretDto;
import org.example.fileshibernate.model.User;
import org.example.fileshibernate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping("/register")
    public String serveRegistrationForm(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();

        if (session.getAttribute("secret") == null) {
            model.addAttribute("secretDto", new SecretDto());
            return "entercode";
        } else {
            String username = userService.findBySecret((String) session.getAttribute("secret")).getUsername();
            model.addAttribute("username", username);

            if (!model.containsAttribute("passwordDto")) {
                model.addAttribute("passwordDto", new PasswordDto());
            }
            return "register";
        }

    }

    @RequestMapping(value = "/sendsecret", method = RequestMethod.POST)
    public String processSecretEntered(@Valid SecretDto secretDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        User user = userService.findBySecret(secretDto.getSecret());

        if (user == null) {
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("To nie jest prawidłowy kod", FlashMessage.Status.FAILURE));

        } else {
            request.getSession().setAttribute("secret", secretDto.getSecret());

        }

        return "redirect:/register";


    }

    @RequestMapping(value="/register", method= RequestMethod.POST)
    public String processNewPassword(@Valid PasswordDto passwordDto, BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes, HttpServletRequest request) {
        HttpSession session = request.getSession();

        // If there is no "secret" attribute in the session - redirect to the beginning with error
        if (session.getAttribute("secret") == null) {
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("Jest problem z sekretnym kodem. Skontaktuj się z J.", FlashMessage.Status.FAILURE));
            return "redirect:/register";
        }


        // If there is a form error - send back to the form
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordDto", bindingResult);
            redirectAttributes.addFlashAttribute("passwordDto", passwordDto);

            return "redirect:/register";
        }


        // If no errors, invalidate session attribute, set the password and redirect to login.
        String secret = (String) session.getAttribute("secret");
        request.getSession().removeAttribute("secret");

        String password = passwordDto.getPassword();

        User user = userService.findBySecret(secret);
        user.setPassword(passwordEncoder.encode(password));

        userService.save(user);

        request.getSession().setAttribute("flash",
                new FlashMessage("Hasło zostało ustawione, możesz się zalogować", FlashMessage.Status.SUCCESS));

        return "redirect:/login";










    }

}
