package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Wyszukiwanie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "relacja_id")
    private Relacja relacja;

    private LocalDateTime dataGodzina;
}