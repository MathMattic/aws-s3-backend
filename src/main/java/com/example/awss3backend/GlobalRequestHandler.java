package com.example.awss3backend;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalRequestHandler.class);

    @ModelAttribute
    public void getCustomHeader(HttpServletRequest req) {
        Map<String,String> headers = new HashMap<>();
        req.getHeaderNames().asIterator().forEachRemaining(headerName -> headers.put(headerName, req.getHeader(headerName)));

        logger.info("({}): [{}] call sent to endpoint [{}]({}) with content-type [{} ({})]. Dispatcher type: [{}].",
                req.getProtocol(), req.getMethod(), req.getRequestURI(), req.getQueryString(), req.getContentType(), req.getCharacterEncoding(), req.getDispatcherType());
        logger.info("User {} with authentication {}", req.getRemoteUser(), req.getAuthType());
        logger.info("Headers: {}", headers);
        logger.info("Client: [{}:{}] . Server: [({}) {}:{}]", req.getRemoteAddr(), req.getRemotePort(), req.getLocalName(), req.getLocalAddr(), req.getLocalPort());
//        req.getParameterMap().forEach((key, value) -> logger.info("Parameter: {} = {}", key, value));

        // Passing information to the controller test
        if(req.getRequestURI().equals("/status")) req.setAttribute("custom-key", "custom-value");

    }
}
