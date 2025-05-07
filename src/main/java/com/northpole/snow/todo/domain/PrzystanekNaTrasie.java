package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import java.time.Duration;

@Entity
public class PrzystanekNaTrasie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "przystanek_id")
    private Przystanek przystanek;

    @ManyToOne
    @JoinColumn(name = "trasa_id")
    private Trasa trasa;

    private int kolejnosc;
    private Duration czasPrzejazdu;
}