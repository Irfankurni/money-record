package com.irfan.moneyrecord.history.repository;

import com.irfan.moneyrecord.history.model.History;
import com.irfan.moneyrecord.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, String> {

    List<History> findByUserAndType(User user, String type);
}
