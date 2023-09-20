package com.example.awss3backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import software.amazon.awssdk.services.s3.model.S3Exception;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("GlobalExceptionHandler Exception: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage() + "\n" + e.getClass().getSimpleName());
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<String> handleS3Exception(S3Exception e) {
        logger.error("GlobalExceptionHandler S3Exception: ", e);
        return ResponseEntity.status(e.statusCode()).body(e.awsErrorDetails().errorMessage() + "\n" + e.getClass().getSimpleName());
    }

}
