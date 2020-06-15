package com.sgr.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, String id) {
        super("Could not find " + resource + " with id : " + id);
    }

    public ResourceNotFoundException(String resource) {
        super("Could not find " + resource);
    }

}
