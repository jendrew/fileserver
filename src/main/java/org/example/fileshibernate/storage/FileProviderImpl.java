package org.example.fileshibernate.storage;


import org.example.fileshibernate.model.MyFile;
import org.example.fileshibernate.service.FileService;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class FileProviderImpl implements FileProvider {


    @Value("${root.folder}")
    private String rootFolder;

    @Autowired
    FileService fileService;


    @Override
    public File findByPath(Path path) {
        return path.toFile();
    }

    @Override
    public void reportAllFiles(Path path) {
        File file = path.toFile();

        File[] contents = file.listFiles();

        for (File f: contents) {
            if (f.isDirectory()) {
                System.out.println("Dir  -> " + f.getName() + " | size: " + f.length());
                reportAllFiles(Paths.get(f.getPath()));
            } else {
                System.out.println("File -> " + f.getName() + " | size: " + f.length());
            }
        }


    }

    @Override
    public void indexFilesInDirectory(String path) {
        File file = new File(path);
        MyFile rootMyFile = fileService.findByPath(file.getPath());
        if (rootMyFile == null) {
            rootMyFile = createFile(file);
            fileService.saveFile(rootMyFile);
        }
        indexFolder(rootMyFile);


    }


    private void indexFolder (MyFile parent) {
            File directory = new File(parent.getPath());
            if (directory.listFiles() != null) {
                for (File f: directory.listFiles()) {
                    MyFile myFile = fileService.findByPath(f.getPath());
                    if (myFile == null) {
                        myFile = createFile(f);
                        myFile.setParent(parent);
                        fileService.saveFile(myFile);
                    }
                    if (f.isDirectory()) {
                        indexFolder(myFile);
                    }
                }
            }

    }
    @Override
    public Pair<Integer, Integer> cleanupFolder(String path) {
        int count = 0;

        List<MyFile> files = fileService.findAll();
        for (MyFile f: files) {
            File file = new File(f.getPath());
                if (file == null) {
                    fileService.delete(f);
                    files.remove(f);
                    count++;
                }
        }

        System.out.println("Cleanup completed. " + count + " elements deleted. " +
                files.size() + " elements left.");

        return Pair.with(count, files.size());
    }


    public MyFile createFile(File file) {
        MyFile myFile = new MyFile();
        myFile.setPath(file.getPath());
        myFile.setName(file.getName());
        myFile.setSize(file.length());
        myFile.setLastModified(new Date(file.lastModified()));
        myFile.setIsDirectory(file.isDirectory());

        return myFile;

    }






}
