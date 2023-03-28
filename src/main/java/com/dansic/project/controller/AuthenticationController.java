package com.dansic.project.controller;

import com.dansic.project.payload.AuthenticationRequest;
import com.dansic.project.payload.AuthenticationResponse;
import com.dansic.project.payload.RegisterRequest;
import com.dansic.project.service.impl.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*",maxAge = 3600)
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;



  @PostMapping("/register")
  public ResponseEntity register(
      @RequestBody @Valid RegisterRequest user,
      BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      ResponseEntity response = ResponseEntity.badRequest().body(bindingResult.getAllErrors()
              .stream()
              .map(ObjectError::getDefaultMessage)
              .collect(Collectors.toList()));
      return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(service.register(user));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }


}
