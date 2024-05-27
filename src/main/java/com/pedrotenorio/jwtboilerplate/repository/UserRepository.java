package com.pedrotenorio.jwtboilerplate.repository;

import com.pedrotenorio.jwtboilerplate.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
