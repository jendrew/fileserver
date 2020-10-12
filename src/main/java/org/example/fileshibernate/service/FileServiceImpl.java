package org.example.fileshibernate.service;

import org.example.fileshibernate.Utils;
import org.example.fileshibernate.dao.FileDao;
import org.example.fileshibernate.dto.PrefsDto;
import org.example.fileshibernate.model.MyFile;
import org.example.fileshibernate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {


    @Autowired
    FileDao fileDao;

    @Override
    public long count() {
        return fileDao.count();
    }

    @Override
    public MyFile findByPath(String name) {
        return fileDao.findByPath(name);
    }

    @Override
    public MyFile findById(Long id) {
        return fileDao.findById(id);
    }

    @Override
    public List<MyFile> findByNameContainingIgnoreCase(String infix) {
        return fileDao.findByNameContainingIgnoreCase(infix);
    }

    @Override
    public List<MyFile> findAll() {
        return fileDao.findAll();
    }

    @Override
    public void delete(MyFile file) {
        fileDao.delete(file);
    }

    @Override
    public void deleteAll() {
        fileDao.deleteAll();
    }

    @Override
    public void saveFile(MyFile file) {
        report(file);
        fileDao.save(file);
    }

    @Override
    public List<MyFile> findByParentOrderByLastModifiedAsc(MyFile parent) {
        return fileDao.findByParentOrderByLastModifiedAsc(parent);
    }

    @Override
    public List<MyFile> findByParentOrderByLastModifiedDesc(MyFile parent) {
        return fileDao.findByParentOrderByLastModifiedDesc(parent);
    }

    @Override
    public List<MyFile> findByParentOrderByNameAsc(MyFile parent) {
        return fileDao.findByParentOrderByNameAsc(parent);
    }

    @Override
    public List<MyFile> findByParentOrderByNameDesc(MyFile parent) {
        return fileDao.findByParentOrderByNameDesc(parent);
    }

    @Override
    public List<MyFile> getByPrefs(MyFile parent, PrefsDto prefsDto) {
        List<MyFile> files;

        String sortingorder = prefsDto.getSortingOrder();
        String direction = prefsDto.getDirection();

        if (sortingorder.equals(User.ORDER_BY_NAME) && direction.equals(User.DIRECTION_ASCENDING)) {
            files = findByParentOrderByNameAsc(parent);
        } else if (sortingorder.equals(User.ORDER_BY_NAME) && direction.equals(User.DIRECTION_DESCENDING)) {
            files = findByParentOrderByNameDesc(parent);
        } else if (sortingorder.equals(User.ORDER_BY_DATE) && direction.equals(User.DIRECTION_ASCENDING)) {
            files = findByParentOrderByLastModifiedAsc(parent);
        } else if (sortingorder.equals(User.ORDER_BY_DATE) && direction.equals(User.DIRECTION_DESCENDING)) {
            files = findByParentOrderByLastModifiedDesc(parent);
        } else {
            return null;
        }

        return Utils.foldersFirst(files);
    }

    @Override
    public List<MyFile> searchByPrefs(String infix, PrefsDto prefsDto) {
        List<MyFile> files;
        String query = infix.toLowerCase();

        String sortingorder = prefsDto.getSortingOrder();
        String direction = prefsDto.getDirection();

        if (sortingorder.equals(User.ORDER_BY_NAME) && direction.equals(User.DIRECTION_ASCENDING)) {
            files = fileDao.searchByNameOrderByNameAsc(query);
        } else if (sortingorder.equals(User.ORDER_BY_NAME) && direction.equals(User.DIRECTION_DESCENDING)) {
            files = fileDao.searchByNameOrderByNameDesc(query);
        } else if (sortingorder.equals(User.ORDER_BY_DATE) && direction.equals(User.DIRECTION_ASCENDING)) {
            files = fileDao.searchByNameOrderByDateAsc(query);
        } else if (sortingorder.equals(User.ORDER_BY_DATE) && direction.equals(User.DIRECTION_DESCENDING)) {
            files = fileDao.searchByNameOrderByDateDesc(query);
        } else {
            return null;
        }

        return Utils.foldersFirst(files);
    }


    private void report (MyFile file) {
        System.out.println(
                "File saved. Name: " + file.getName() +
                " Size: " + file.getSize() + ((file.getParent() == null) ? " " : " Parent: " + file.getParent()));


    }


}
