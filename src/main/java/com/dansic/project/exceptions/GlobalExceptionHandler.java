package com.dansic.project.exceptions;

import com.dansic.project.payload.ErrorDetails;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    //handle specific custom exception
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFound(ResourceNotFound resourceNotFound,
                                                               WebRequest webRequest){

        ErrorDetails errorDetails = new ErrorDetails(new Date(),resourceNotFound.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ErrorDetails> handleUserAlreadyExistsException(UserAlreadyExists userAlreadyExists ,
                                                               WebRequest webRequest){

        ErrorDetails errorDetails = new ErrorDetails(new Date(),userAlreadyExists.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
    }


    //global exception

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                              WebRequest webRequest){

        ErrorDetails errorDetails = new ErrorDetails(new Date(),exception.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }










//
//
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        Map<String,ArrayList<String>> errorsArr = new HashMap<>();
//        ArrayList<String> errors = new ArrayList<>();
//
//        ex.getBindingResult().getAllErrors().forEach((error) ->{
//            String fieldName = ((FieldError)error).getField();
//            String message =  error.getDefaultMessage();
//            errors.add(message);
//            errorsArr.put("Errors",errors);
//        });
//
//        return new ResponseEntity<>(errorsArr, HttpStatus.BAD_REQUEST);
//    }








}
