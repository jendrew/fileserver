package org.example.fileshibernate.storage;


import org.example.fileshibernate.model.MyFile;
import org.javatuples.Tuple;

import java.io.File;
import java.nio.file.Path;

public interface FileProvider {
    File findByPath(Path path);
    void reportAllFiles(Path path);
    void indexFilesInDirectory(String path);
    Tuple cleanupFolder(String path);
    MyFile createFile(File file);

}
