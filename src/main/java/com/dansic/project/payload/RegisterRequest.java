package com.dansic.project.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder

public class RegisterRequest {



  @NotBlank(message = "nickname is required")
  private String nickname;
  @NotBlank(message = "Phone number is required")
  @Pattern(regexp="^[0-9]{8}$", message="Phone number must be 8 digits long")
  private String phoneNo;
  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email address")
  private String email;
  @NotBlank(message = "Password is required")

  private String password;
  @NotBlank(message = "Adrress is required")
  private String address;
  @NotBlank(message = "Zip is required")
  private String zipCode;


}
