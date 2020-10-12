package org.example.fileshibernate.web;

import org.example.fileshibernate.model.MyFile;
import org.example.fileshibernate.model.User;
import org.example.fileshibernate.service.FileService;
import org.example.fileshibernate.service.RoleService;
import org.example.fileshibernate.service.UserService;
import org.example.fileshibernate.storage.FileProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${root.folder}")
    private String rootFolder;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    FileService fileService;

    @Autowired
    FileProvider fileProvider;

    @RequestMapping("/users")
    public String getUsers(Model model) {

        Iterable<User> users = userService.findAll();
        model.addAttribute("users", users);

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }

        model.addAttribute("roles", roleService.findAll());

        return "users";

    }

    @RequestMapping(value="/adduser", method = RequestMethod.POST)
    public String registerUser(User user, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        if (!request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/logout";
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Taki użytkownik już jest.", FlashMessage.Status.FAILURE));

            return "redirect:/admin/users";
        }

        String secret = new RandomString().nextString();
        user.setSecret(secret);
        userService.save(user);

        return "redirect:/admin/users";
    }

    @RequestMapping(value="/edituser/{userId}")
    public String editUser(@PathVariable Long userId,
                           HttpServletRequest request, RedirectAttributes redirectAttributes) {

        if (!request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/logout";
        }

        User user = userService.findOne(userId);

        if (user != null) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("action","/admin/edituser");

        } else {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Coś poszło nie tak.", FlashMessage.Status.FAILURE));
        }


        return "redirect:/admin/users";
    }

    @RequestMapping(value="/edituser", method = RequestMethod.POST)
    public String procesEditedUserData(@Valid User user, RedirectAttributes redirectAttributes,
                                       HttpServletRequest request) {
        if (!request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/logout";
        }

        User oldUser = userService.findOne(user.getId());

        if (oldUser != null) {
            oldUser.setUsername(user.getUsername());
            oldUser.setRole(user.getRole());
            userService.save(oldUser);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Użytkownik wyedytowany.", FlashMessage.Status.SUCCESS));
        } else {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Coś poszło nie tak.", FlashMessage.Status.FAILURE));
        }


        return "redirect:/admin/users";


    }

    @RequestMapping("/database")
    public String getDatabaseInfo(Model model) {
        model.addAttribute("count", fileService.count());

        return "database";

    }

    @RequestMapping("/index")
    public String startIndexingFiles() {
        fileProvider.indexFilesInDirectory(rootFolder);

        return "redirect:/admin/database";
    }

    @RequestMapping("/delete")
    public String purgeDatabase() {
        fileService.deleteAll();
        MyFile root = fileProvider.createFile(new File(rootFolder));
        fileService.saveFile(root);
        return "redirect:/admin/database";
    }


}
