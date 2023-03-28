package com.dansic.project.entity;

import com.dansic.project.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "_user")
public class User implements Serializable,UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "nickname is required")
  private String nickname;
  @NotBlank(message = "Phone number is required")
  @Pattern(regexp="^[0-9]{8}$", message="Phone number must be 8 digits long")
  private String phoneNo;

  // email address = username
  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email address")
  private String email;
  @NotBlank(message = "Password is required")

  private String password;
  @NotBlank(message = "Adrress is required")
  private String address;
  @NotBlank(message = "Zip is required")
  private String zipCode;


  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<Token> tokens;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
  private List<Post> posts = new ArrayList<>();




  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}

