package com.ecommerce.project.CategoryService.exception;

import lombok.Data;

//@Data
public class ResourceNotFoundException extends RuntimeException{
    private String resource;
    private String filedName;
    private Long fieldId;

    public ResourceNotFoundException(String resource, String filedName, Long fieldId) {
        super(String.format("%s not available for %s:%d",resource,filedName,fieldId));
        this.resource = resource;
        this.filedName = filedName;
        this.fieldId = fieldId;
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
