package org.example.fileshibernate.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;



@Entity
@Table(name = "FILE")
public class MyFile {



    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotNull
    private String name;

    @Column
    private Long size;

    @Column
    private Date lastModified;

    @Column (unique = true, columnDefinition="TXT")
    @NotNull
    private String path;




    @ManyToOne(cascade={CascadeType.ALL}, optional=true, fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id", columnDefinition = "integer")
    private MyFile parent;

    @OneToMany(mappedBy = "parent")
    private Set<MyFile> contents = new HashSet<>();

    @Basic
    private Character isDirectory;


    public Boolean getIsDirectory() {
        if (isDirectory == null) return null;
        return isDirectory == 'Y' ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setIsDirectory(Boolean isDirectory) {
        if (isDirectory == null) {
            this.isDirectory = null;
        } else {
            this.isDirectory = isDirectory ? 'Y' : 'N';
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<MyFile> getContents() {
        return contents;
    }

    public void setContents(Set<MyFile> contents) {
        this.contents = contents;
    }

    public MyFile getParent() {
        return parent;
    }

    public void setParent(MyFile parent) {
        this.parent = parent;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public void setIsDirectory(Character isDirectory) {
        this.isDirectory = isDirectory;
    }


}
