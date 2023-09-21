package com.example.awss3backend.entities;

import jakarta.persistence.*;

@Entity
public class BucketObject {

//    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id private String key;
    private long size;
    private String lastModified;
    private String contentType;
    private String eTag;

    public BucketObject() {}

    public BucketObject(String key, long size, String lastModified, String contentType, String eTag) {
        this.key = key;
        this.size = size;
        this.lastModified = lastModified;
        this.contentType = contentType;
        this.eTag = eTag;
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
                ", key='" + key + '\'' +
                ", size=" + size +
                ", lastModified='" + lastModified + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
