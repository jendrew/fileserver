package org.example.fileshibernate.dao;

import org.example.fileshibernate.model.MyFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDao extends CrudRepository<MyFile, Long> {
    MyFile findByPath(String path);
    MyFile findById(Long id);
    List<MyFile> findAll();
    List<MyFile> findByNameContainingIgnoreCase(String infix);

    List<MyFile> findByParentOrderByLastModifiedAsc(MyFile parent);
    List<MyFile> findByParentOrderByLastModifiedDesc(MyFile parent);

    @Query("SELECT f FROM MyFile f WHERE parent = ?1 ORDER BY LOWER(name) ASC")
    List<MyFile> findByParentOrderByNameAsc(MyFile parent);

    @Query("SELECT f FROM MyFile f WHERE parent = ?1 ORDER BY LOWER(name) DESC")
    List<MyFile> findByParentOrderByNameDesc(MyFile parent);



    @Query("SELECT f FROM MyFile f WHERE LOWER(name) LIKE %?1% ORDER BY LOWER(name) ASC")
    List<MyFile> searchByNameOrderByNameAsc(String infix);

    @Query("SELECT f FROM MyFile f WHERE LOWER(name) LIKE %?1% ORDER BY LOWER(name) DESC")
    List<MyFile> searchByNameOrderByNameDesc(String infix);

    @Query("SELECT f FROM MyFile f WHERE LOWER(name) LIKE %?1% ORDER BY lastModified ASC")
    List<MyFile> searchByNameOrderByDateAsc(String infix);

    @Query("SELECT f FROM MyFile f WHERE LOWER(name) LIKE %?1% ORDER BY lastModified DESC")
    List<MyFile> searchByNameOrderByDateDesc(String infix);



    void deleteAll();



}


