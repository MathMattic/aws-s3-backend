package com.example.awss3backend.entities;

import jakarta.persistence.*;

@Entity
public class BucketObject {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String key;
    private long size;
    private String lastModified;
    private String contentType;
    private String eTag;

    protected BucketObject() {}

    public BucketObject(String key, long size, String lastModified, String contentType, String eTag) {
        this.key = key;
        this.size = size;
        this.lastModified = lastModified;
        this.contentType = contentType;
        this.eTag = eTag;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    @Override
    public String toString() {
        return "BucketObject{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", size=" + size +
                ", lastModified='" + lastModified + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
