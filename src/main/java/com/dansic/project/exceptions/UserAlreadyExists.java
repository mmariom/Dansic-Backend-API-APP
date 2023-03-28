package com.dansic.project.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserAlreadyExists extends RuntimeException {

    private String userEmail;

    public UserAlreadyExists(String userEmail) {
        super(String.format("User with this email: %s already registered!", userEmail));
        this.userEmail = userEmail;
    }
}
