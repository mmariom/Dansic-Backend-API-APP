package com.dansic.project.controller;

import com.dansic.project.payload.AuthenticationRequest;
import com.dansic.project.payload.AuthenticationResponse;
import com.dansic.project.payload.RegisterRequest;
import com.dansic.project.payload.UpdateUserDetailsDto;
import com.dansic.project.service.impl.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  /**
   * Register a new user
   *
   * @param registerRequest registration request containing user information
   * @param bindingResult   to hold validation errors
   * @return ResponseEntity with the registration result
   */
  @PostMapping("/register")
  public ResponseEntity<?> register(
          @RequestBody @Valid RegisterRequest registerRequest,
          BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return buildErrorResponse(bindingResult);
    }

    return ResponseEntity.ok(authenticationService.register(registerRequest));
  }

  /**
   * Authenticate a user
   *
   * @param authenticationRequest containing user credentials
   * @return ResponseEntity with the authentication result
   */
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
          @RequestBody AuthenticationRequest authenticationRequest) {

    return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
  }

  /**
   * Update a user's profile
   *
   * @param updateUserDetailsDto containing user details
   * @param bindingResult        to hold validation errors
   * @return ResponseEntity with the update result
   */
  @PutMapping("/update")
  public ResponseEntity<?> updateProfile(
          @RequestBody @Valid UpdateUserDetailsDto updateUserDetailsDto,
          BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      return buildErrorResponse(bindingResult);
    }

    return ResponseEntity.ok(authenticationService.updateProfile(updateUserDetailsDto));
  }

  /**
   * Update a user's password
   *
   * @param currentPassword user's current password
   * @param newPassword     user's new password
   * @return ResponseEntity with the update result
   */
  @PutMapping("/update-password")
  public ResponseEntity<Map<String,String>> updatePassword(
          @RequestParam("currentPassword") String currentPassword,
          @RequestParam("newPassword") String newPassword) {

//    authenticationService.updatePassword(currentPassword, newPassword);
//    return new ResponseEntity<>(Map.of("body", "Password updated successfully"), HttpStatus.OK);

    return authenticationService.updatePassword(currentPassword, newPassword);
  }

  /**
   * Build an error response for validation errors
   *
   * @param bindingResult containing validation errors
   * @return ResponseEntity with the error response
   */
  private ResponseEntity<?> buildErrorResponse(BindingResult bindingResult) {
    List<String> errors = bindingResult.getAllErrors()
            .stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.toList());

    return ResponseEntity.badRequest().body(errors);
  }
}
