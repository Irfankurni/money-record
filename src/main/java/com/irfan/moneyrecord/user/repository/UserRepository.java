package com.irfan.moneyrecord.user.repository;

import com.irfan.moneyrecord.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

}
