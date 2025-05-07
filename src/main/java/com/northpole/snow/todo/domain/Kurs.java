package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
public class Kurs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trasa_id")
    private Trasa trasa;

    private LocalTime godzinaStartu;
}