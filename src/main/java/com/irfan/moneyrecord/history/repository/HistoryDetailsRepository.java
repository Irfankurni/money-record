package com.irfan.moneyrecord.history.repository;

import com.irfan.moneyrecord.history.model.History;
import com.irfan.moneyrecord.history.model.HistoryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryDetailsRepository extends JpaRepository<HistoryDetails, String> {

    boolean deleteByHistoryId(String historyId);

    List<HistoryDetails> findAllByHistory(History history);
}
