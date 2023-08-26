package com.example.awss3backend.controllers;

import com.example.awss3backend.entities.BucketObject;
import com.example.awss3backend.services.S3Service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // default for Angular testing.
public class GetBucketObjects {

    private final S3Service s3Service;
    public GetBucketObjects(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    final String json = MediaType.APPLICATION_JSON_VALUE;

    // produces: What this endpoint returns, and the client may have 'Accept' headers to specify what it wants
    // consumes: Endpoint only responds if the client has 'Content-Type' header that matches this value
    // headers:  What headers the client must have for this endpoint to be used
    @GetMapping(path = "/", produces = json) //headers = {"content-type=application/json"})
    public ResponseEntity<List<BucketObject>> listBucketObjects(@RequestHeader Map<String, String> headers) {
//        List<BucketObject> bucketObjects = s3Service.ListObjects();
        System.out.println(headers.toString());

        return ResponseEntity.ok(s3Service.ListObjects());
    }

    @GetMapping(path="/dl", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadBucketObject(@RequestParam String key) { // or RequestHeader for the custom HTTP header
        System.out.println("downloadBucketObject()");
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+key+"\"").body(s3Service.getObject(key));
    }


    @PostMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBucketObject(@RequestPart("file") MultipartFile file, @RequestParam("shouldEncrypt") boolean encrypt) {
        System.out.println("uploadBucketObject() and encrypt is " + encrypt + " and file is " + file.getContentType());

        System.out.println(file.getOriginalFilename());

        return ResponseEntity.ok().body(s3Service.putObject(file));
    }

    @PutMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBucketObject() {
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> patchBucketObject() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteBucketObject() {
        return ResponseEntity.ok(s3Service.deleteObject());
    }


}
