package com.irfan.moneyrecord.history.model;

import com.irfan.moneyrecord.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String type;
    private LocalDate date;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
