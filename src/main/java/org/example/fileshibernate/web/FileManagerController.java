package org.example.fileshibernate.web;

import org.example.fileshibernate.Utils;
import org.example.fileshibernate.dto.PrefsDto;
import org.example.fileshibernate.model.MyFile;
import org.example.fileshibernate.model.User;
import org.example.fileshibernate.service.FileService;
import org.example.fileshibernate.service.UserService;
import org.example.fileshibernate.storage.FileProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class FileManagerController {

    @Value("${root.folder}")
    private String rootFolder;

    @Autowired
    FileProvider fileProvider;

    @Autowired
    FileService fileService;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String getIndex(Model model) {


        return "index";
    }

    @RequestMapping("/go")
    public String showLog(Model model) {
        MyFile parent = fileService.findById(1L);
        List<MyFile> files = fileService.findByParentOrderByLastModifiedAsc(parent);
        List<MyFile> folder = Utils.foldersFirst(files);

        for( MyFile file : folder) {
            System.out.println(file.getName() + " " + file.getIsDirectory());
        }


        return "index";
    }


    @RequestMapping("/files")
    public String listFolder(Model model, HttpServletRequest request) {

        String username = request.getUserPrincipal().getName();
        User user = userService.findByUsername(username);

        MyFile parent = fileService.findByPath(rootFolder);
        List<MyFile> contents = fileService.getByPrefs(parent, user.getPrefs());

        model.addAttribute("prefsDto", user.getPrefs());
        model.addAttribute("folder", contents);

        return "folder";
    }


//    @RequestMapping("/files/{id}")
//    public String getFile(@PathVariable Long id,  Model model) {
//        MyFile file = fileService.findById(id);
//
//        if (file != null) {
//            if (file.getIsDirectory() == true) {
//                List<MyFile> files = getContents(file.getPath());
//                model.addAttribute("up", file.getParent());
//                model.addAttribute("folder", files);
//                return "folder";
//            } else {
//                return "redirect:/download/" + id;
//            }
//        }
//        return "index";
//    }

    @RequestMapping("/files/{id}")
    public String getFile(@PathVariable Long id,  Model model, HttpServletRequest request) {
        MyFile file = fileService.findById(id);

        if (file != null) {
            if (file.getIsDirectory() == true) {

                String username = request.getUserPrincipal().getName();
                User user = userService.findByUsername(username);

                List<MyFile> contents = fileService.getByPrefs(file, user.getPrefs());

                model.addAttribute("up", file.getParent());
                model.addAttribute("prefsDto", user.getPrefs());
                model.addAttribute("folder", contents);
                return "folder";
            } else {
                return "redirect:/download/" + id;
            }
        }
        return "index";
    }



    @RequestMapping("/download/{id}")
    public void serveFile(@PathVariable Long id, HttpServletResponse response) throws IOException {

        MyFile myFile = fileService.findById(id);
        if (myFile == null) {
             return;
        }

        File file = new File(myFile.getPath());
        if (file == null || file.isDirectory() == true) {
            return;
        }


        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }
        System.out.println("mimetype : " + mimeType);
        response.setContentType(mimeType);

        response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setContentLengthLong((long) file.length());

        try {
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            FileInputStream fis = new FileInputStream(myFile.getPath());
            int len;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) > 0 ) {
                bos.write(buf,0,len);
            }
            bos.close();
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @RequestMapping("/changeprefs")
    public String changePrefs(@Valid PrefsDto prefsDto, Model model, HttpServletRequest request) {
        String username = request.getUserPrincipal().getName();
        User user = userService.findByUsername(username);

        user.setPrefs(prefsDto);
        userService.save(user);

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;

    }

    private List<MyFile> getContents(String path) {

        Map<Boolean, List<MyFile>> folderGrouped = fileService.findByPath(path).getContents().stream()
                .sorted(Comparator.comparing(MyFile::getName))
                .collect(Collectors.groupingBy(MyFile::getIsDirectory));

//        List<MyFile> files = fileService.findByPath(rootFolder).getContents().stream()
//                .sorted(Comparator.comparing( f -> f.getName()))
//                .collect(Collectors.toList());

        List<MyFile> contents = new ArrayList<>();

        if (folderGrouped.get(true) != null) {
            contents.addAll(folderGrouped.get(true));
        }

        if (folderGrouped.get(false) != null) {
            contents.addAll(folderGrouped.get(false));
        }

        return contents;
    }

}
