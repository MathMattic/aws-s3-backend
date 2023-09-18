package com.example.awss3backend.controllers;

import com.example.awss3backend.entities.BucketObject;
import com.example.awss3backend.services.S3Service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public String status(@ModelAttribute("http-headers") String headers) {
        System.out.println("the status header: " + headers);
        return "OK";
    }

    @GetMapping("/console") //TODO Thymeleaf
    public Resource html() {
        return new ClassPathResource("static/console.html");
    }

    @GetMapping(path = "/", produces = json)
    public ResponseEntity<List<BucketObject>> listBucketObjects() {
        List<BucketObject> bucketObjects = s3Service.listObjects();
        return ResponseEntity.ok(bucketObjects);
    }

    @GetMapping(path="/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadBucketObject(@RequestParam String key) {
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