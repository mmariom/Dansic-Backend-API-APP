package com.dansic.project.repo;

import java.util.Optional;
import java.util.UUID;

import com.dansic.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);


}
