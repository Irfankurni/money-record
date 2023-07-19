package com.irfan.moneyrecord.user.repository;

import java.util.Optional;

import com.irfan.moneyrecord.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByEmail(String email);

}
