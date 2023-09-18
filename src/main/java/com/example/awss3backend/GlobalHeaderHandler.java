package com.example.awss3backend;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@ControllerAdvice
public class GlobalHeaderHandler {

    @ModelAttribute("http-headers")
    public String getCustomHeader(@RequestHeader Map<String, String> headers) {
        String statusHeader = headers.get("status");
        headers.forEach((key, value) -> System.out.println("GlobalHeaderHandler: " + key + " : " + value));
        return statusHeader;
    }
}
