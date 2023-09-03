package com.example.awss3backend.controllers;

import com.example.awss3backend.entities.BucketObject;
import com.example.awss3backend.services.S3Service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // default for Angular testing.
public class Controller {

    private final S3Service s3Service;
    public Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    final String json = MediaType.APPLICATION_JSON_VALUE;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/status")
    public String status() {
        return "OK";
    }

//<dependency>
//    <groupId>org.springframework.boot</groupId>
//    <artifactId>spring-boot-starter-thymeleaf</artifactId>
//</dependency>
    @GetMapping("/console")
    public Resource html() {
        return new ClassPathResource("static/console.html");
    }

    @GetMapping(path = "/", produces = json)
    public ResponseEntity<List<BucketObject>> listBucketObjects(@RequestHeader Map<String, String> headers) {
        System.out.println("listBucketObjects()");
//        headers.forEach((key, value) -> System.out.printf("Header '%s' = %s", key, value));
        System.out.println(headers.toString());
        List<BucketObject> bucketObjects = s3Service.listBucketObjects();
        return ResponseEntity.ok(bucketObjects);
    }

    @GetMapping(path="/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadBucketObject(@RequestParam String key) { // or RequestHeader for the custom HTTP header
        System.out.println("downloadBucketObject()");
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+key+"\"");
        byte[] object = s3Service.getObject(key);

        return ResponseEntity.ok().headers(headers).body(object);
    }


    @PostMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBucketObject(@RequestPart("file") MultipartFile file, @RequestParam("shouldEncrypt") boolean encrypt) {
        System.out.println("uploadBucketObject() and encrypt is " + encrypt + " and file is " + file.getContentType());

        System.out.println(file.getOriginalFilename());
        System.out.println(s3Service.putObject(file));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteBucketObject(@RequestParam String key) {
        String response = s3Service.deleteObject(key);
        System.out.println("deleteBucketObject() and response is " + response);
        return ResponseEntity.ok(response);
    }

    @PutMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateBucketObject() {
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path="/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> patchBucketObject() {
        return ResponseEntity.noContent().build();
    }



}
