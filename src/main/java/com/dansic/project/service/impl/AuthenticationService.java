package com.dansic.project.service.impl;

import com.dansic.project.config.JwtService;
import com.dansic.project.exceptions.ResourceNotFound;
import com.dansic.project.exceptions.UserAlreadyExists;
import com.dansic.project.payload.AuthenticationRequest;
import com.dansic.project.payload.AuthenticationResponse;
import com.dansic.project.payload.RegisterRequest;
import com.dansic.project.entity.Token;
import com.dansic.project.repo.TokenRepository;
import com.dansic.project.entity.enums.TokenType;
import com.dansic.project.entity.enums.Role;
import com.dansic.project.entity.User;
import com.dansic.project.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService   {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

//  public AuthenticationResponse register(RegisterRequest request) {
//    var user = User.builder()
//        .firstname(request.getFirstname())
//        .lastname(request.getLastname())
//        .email(request.getEmail())
//        .password(passwordEncoder.encode(request.getPassword()))
//        .role(Role.USER)
//        .build();
//    var savedUser = repository.save(user);
//    var jwtToken = jwtService.generateToken(user);
//    saveUserToken(savedUser, jwtToken);
//    return AuthenticationResponse.builder()
//        .token(jwtToken)
//        .build();
//  }
//
//


//  private   Post getPostById(Long postId){
//    return postRepository.findById(postId).orElseThrow(
//            () -> new ResourceNotFound("Post","Id", postId)
//    );
//  }



  public ResponseEntity register(RegisterRequest registerRequest) {
    var existingUser = repository.findByEmail(registerRequest.getEmail());
    if (existingUser.isPresent()) {
      throw new UserAlreadyExists(registerRequest.getEmail());
    }

    var userBuild = User.builder()
            .nickname(registerRequest.getNickname())
            .address(registerRequest.getAddress())
            .zipCode(registerRequest.getZipCode())
            .phoneNo(registerRequest.getPhoneNo())
            .email(registerRequest.getEmail())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .role(Role.USER)
            .build();

    repository.save(userBuild);
    return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");

//    var savedUser = repository.save(registerRequest);
//    var jwtToken = jwtService.generateToken(registerRequest);
//    saveUserToken(savedUser, jwtToken);
//    return AuthenticationResponse.builder()
//            .token(jwtToken)
//            .build();

  }






  public AuthenticationResponse authenticate(AuthenticationRequest user) {

    var existingUser = repository.findByEmail(user.getEmail()).orElseThrow(() -> new ResourceNotFound("User", "email", user.getEmail()));

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            user.getEmail(),
            user.getPassword()
        )
    );
    var jwtToken = jwtService.generateToken(existingUser);
    revokeAllUserTokens(existingUser);
    saveUserToken(existingUser, jwtToken);
    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }



}
