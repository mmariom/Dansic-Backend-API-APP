package com.dansic.project.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorDetails {



    private Date date;
    private String body;
    private String details;

    public ErrorDetails(Date date, String message, String details) {
        this.date = date;
        this.body = message;
        this.details = details;
    }

    
}
