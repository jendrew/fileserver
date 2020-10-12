package org.example.fileshibernate;

import org.example.fileshibernate.model.MyFile;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Utils {



    public static String getFileSize(long length) {
        double d = (double) length;
        DecimalFormat df = new DecimalFormat("###.##");
        List<String> suf = Arrays.asList("B","KB","MB","GB","TB");
        int i = 0;
        while (d > 1024) {
            d = d / 1024;
            i++;
        }

        return df.format(d) + "" + suf.get(i);
    }

    public static String getDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy ");
        return sdf.format(date);

    }

    public static List<MyFile> foldersFirst (List<MyFile> input) {
        Map<Boolean, List<MyFile>> folderGrouped = input.stream()
                .collect(Collectors.groupingBy(MyFile::getIsDirectory));

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
