package com.example.awss3backend.controllers;

import com.example.awss3backend.BucketObject;
import com.example.awss3backend.services.S3Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ListBucketObjects {


    private final S3Service s3Service;
    public ListBucketObjects(S3Service s3Service) {
        this.s3Service = s3Service;
    }


    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BucketObject>> listBucketObjects() {

        List<BucketObject> bucketObjects = s3Service.ListObjects();
        return ResponseEntity.ok(bucketObjects);

    }

}
