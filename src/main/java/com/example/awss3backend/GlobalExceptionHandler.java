package com.example.awss3backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        System.out.println("GlobalExceptionHandler.handleException()");
        System.out.println(Arrays.toString(e.getStackTrace()));
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<String> handleS3Exception(S3Exception e) {
        System.out.println("GlobalExceptionHandler.handleException()");
        System.out.println(Arrays.toString(e.getStackTrace()));
        System.out.println(e.getMessage());
        return ResponseEntity.status(e.statusCode()).body(e.awsErrorDetails().errorMessage());
    }

}
