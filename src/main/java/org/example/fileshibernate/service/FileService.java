package org.example.fileshibernate.service;

import org.example.fileshibernate.dto.PrefsDto;
import org.example.fileshibernate.model.MyFile;

import java.util.List;

public interface FileService {

    long count();
    MyFile findByPath(String name);
    MyFile findById(Long id);
    List<MyFile> findByNameContainingIgnoreCase(String infix);
    List<MyFile> findAll();
    void delete(MyFile file);
    void deleteAll();
    void saveFile(MyFile file);
    List<MyFile> findByParentOrderByLastModifiedAsc(MyFile parent);
    List<MyFile> findByParentOrderByLastModifiedDesc(MyFile parent);
    List<MyFile> findByParentOrderByNameAsc(MyFile parent);
    List<MyFile> findByParentOrderByNameDesc(MyFile parent);

    List<MyFile> getByPrefs(MyFile parent, PrefsDto prefsDto);
    List<MyFile> searchByPrefs(String infix, PrefsDto prefsDto);
}
