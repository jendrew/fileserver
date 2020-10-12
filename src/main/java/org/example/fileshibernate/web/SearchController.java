package org.example.fileshibernate.web;

import org.example.fileshibernate.dto.PrefsDto;
import org.example.fileshibernate.model.MyFile;
import org.example.fileshibernate.model.User;
import org.example.fileshibernate.service.FileService;
import org.example.fileshibernate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SearchController {


    @Autowired
    FileService fileService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getFile(@RequestParam(value = "search", required = false) String query, Model model, HttpServletRequest request) {

        String username = request.getUserPrincipal().getName();
        User user = userService.findByUsername(username);
        PrefsDto prefs = user.getPrefs();

        List<MyFile> files = fileService.searchByPrefs(query, prefs);


        if (files != null) {
            model.addAttribute("folder", files);
            model.addAttribute("prefsDto", user.getPrefs());
            return "folder";
        }

        return "index";
    }
}
