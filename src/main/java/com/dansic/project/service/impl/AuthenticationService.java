package com.dansic.project.service.impl;

import com.dansic.project.config.JwtService;
import com.dansic.project.exceptions.ResourceNotFound;
import com.dansic.project.exceptions.UserAlreadyExists;
import com.dansic.project.payload.AuthenticationRequest;
import com.dansic.project.payload.AuthenticationResponse;
import com.dansic.project.payload.RegisterRequest;
import com.dansic.project.entity.Token;
import com.dansic.project.payload.UpdateUserDetailsDto;
import com.dansic.project.repo.TokenRepository;
import com.dansic.project.entity.enums.TokenType;
import com.dansic.project.entity.enums.Role;
import com.dansic.project.entity.User;
import com.dansic.project.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthenticationService {

  @Autowired
  private  UserRepository userRepository;
  @Autowired
  private  TokenRepository tokenRepository;
  @Autowired
  private  PasswordEncoder passwordEncoder;
  @Autowired
  private  JwtService jwtService;
  @Autowired
  private  AuthenticationManager authenticationManager;

  /**
   * Register a new user
   *
   * @param registerRequest containing user registration information
   * @return ResponseEntity with the registration result
   */
  public ResponseEntity<String> register(RegisterRequest registerRequest) {
    if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
      throw new UserAlreadyExists(registerRequest.getEmail());
    }

    User newUser = User.builder()
            .nickname(registerRequest.getNickname())
            .address(registerRequest.getAddress())
            .zipCode(registerRequest.getZipCode())
            .phoneNo(registerRequest.getPhoneNo())
            .email(registerRequest.getEmail())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .role(Role.USER)
            .build();

    userRepository.save(newUser);
    return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
  }

  /**
   * Authenticate a user
   *
   * @param authenticationRequest containing user authentication information
   * @return AuthenticationResponse with the authentication result
   */
  public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
    User existingUser = userRepository.findByEmail(authenticationRequest.getEmail())
            .orElseThrow(() -> new ResourceNotFound("User", "email", authenticationRequest.getEmail()));

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()
            )
    );

    String jwtToken = jwtService.generateToken(existingUser);
    revokeAllUserTokens(existingUser);
    saveUserToken(existingUser, jwtToken);

    return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
  }

  /**
   * Update a user's profile
   *
   * @param updateUserDetailsDto containing updated user details
   * @return ResponseEntity with the update result
   */
  public ResponseEntity<String> updateProfile(UpdateUserDetailsDto updateUserDetailsDto) {
    User currentUser = getCurrentUser();

    if (userRepository.findByEmail(updateUserDetailsDto.getEmail()).isPresent()) {
      throw new UserAlreadyExists(updateUserDetailsDto.getEmail());
    }

    currentUser.setAddress(updateUserDetailsDto.getAddress());
    currentUser.setZipCode(updateUserDetailsDto.getZipCode());
    currentUser.setPhoneNo(updateUserDetailsDto.getPhoneNo());
    currentUser.setEmail(updateUserDetailsDto.getEmail());

    userRepository.save(currentUser);
    return ResponseEntity.status(HttpStatus.OK).body("Profile updated successfully");
  }

  /**
   * Update a user's password
   *
   * @param currentPassword user's current password
   * @param newPassword     user's new password
   * @return ResponseEntity with the update result
   */
  public ResponseEntity<Map<String,String>> updatePassword(String currentPassword, String newPassword) {
    User currentUser = getCurrentUser();

    if (!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("body", "Current password is incorrect"));
    }

    currentUser.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(currentUser);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("body", "Password updated successfully"));
  }

  private User getCurrentUser() {
    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }


  private void saveUserToken(User user, String jwtToken) {
    Token token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();


    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty()) {
      return;
    }
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }
}
