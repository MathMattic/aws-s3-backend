package com.example.awss3backend.controllers;

import com.example.awss3backend.entities.BucketObject;
import com.example.awss3backend.services.S3Service;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    private final S3Service s3Service;
    public Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    final private String json = MediaType.APPLICATION_JSON_VALUE;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/status")
    public String status(HttpServletRequest request) {
        return request.getAttribute("custom-key").toString();
    }

    @GetMapping("/console") //TODO Thymeleaf
    public Resource html() {
        return new ClassPathResource("static/console.html");
    }

    @GetMapping(path = "/", produces = json)
    public ResponseEntity<List<BucketObject>> listBucketObjects() {
        logger.info("@GetMapping for '/' .");
        List<BucketObject> bucketObjects = s3Service.listObjects();
        return ResponseEntity.ok(bucketObjects);
    }

    @GetMapping(path="/download", produces = json)
    public ResponseEntity<byte[]> downloadBucketObject(@RequestParam String key) {
        logger.info("@GetMapping for '/download' , key was {}.", key);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+key+"\"");
        byte[] object = s3Service.getObject(key);
        return ResponseEntity.ok().headers(headers).body(object);
    }

    @PostMapping(path="/", produces = json, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBucketObject(@RequestPart("file") MultipartFile file, @RequestParam("encrypt") boolean encrypt) {
        logger.info("@PostMapping for '/' , encrypt was: {}. file was: {} , with content-type: {}.", encrypt, file.getOriginalFilename(), file.getContentType());
        return ResponseEntity.ok(s3Service.putObject(file));
    }

    @DeleteMapping(path="/", produces = json, consumes = json)
    public ResponseEntity<String> deleteBucketObject(@RequestParam String key) {
        logger.info("@DeleteMapping for '/' , key was {}.", key);
        return ResponseEntity.ok(s3Service.deleteObject(key));
    }

    @PutMapping(path="/", produces = json, consumes = json)
    public ResponseEntity<String> updateBucketObject() {
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path="/", produces = json, consumes = json)
    public ResponseEntity<String> patchBucketObject() {
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(path = "/**")
    public ResponseEntity<String> fallback() {
        return ResponseEntity.notFound().build();
    }
}