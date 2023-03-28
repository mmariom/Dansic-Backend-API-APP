package com.dansic.project.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFound extends  RuntimeException{

    private String resourceName;
    private String fieldName;
    private Long fieldValue;

    private String fieldValueString;


    public ResourceNotFound(String resourceName, String fieldName, Long fieldValue) {
        super(String.format("%s not found with %s :%s", resourceName, fieldName,fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }


    public ResourceNotFound(String resourceName, String fieldName, String fieldValueString) {
        super(String.format("%s not found with %s :%s", resourceName, fieldName,fieldValueString));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValueString = fieldValueString;
    }



}
