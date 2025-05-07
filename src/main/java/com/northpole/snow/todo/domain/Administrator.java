package com.northpole.snow.todo.domain;

import jakarta.persistence.*;

@Entity
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pasazer_id")
    private Pasazer pasazer;
}