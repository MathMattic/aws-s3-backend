package com.example.awss3backend.controllers;

import com.example.awss3backend.entities.BucketObject;
import com.example.awss3backend.services.S3Service;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class GetBucketObjects {

    private final S3Service s3Service;
    public GetBucketObjects(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    // produces: What this endpoint returns, and the client may have 'Accept' headers to specify what it wants
    // consumes: What this endpoint accepts, and the client may have 'Content-Type' headers to specify what it sends
    // headers:  What headers the client must have for this endpoint to be used
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, headers = {"content-type=application/json"})
    public ResponseEntity<List<BucketObject>> listBucketObjects() {
        List<BucketObject> bucketObjects = s3Service.ListObjects();
        return ResponseEntity.ok(bucketObjects);

    }

}
