package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Relacja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pasazer_id")
    private Pasazer pasazer;

    @ManyToOne
    @JoinColumn(name = "przystanek_poczatkowy_id")
    private Przystanek przystanekPoczatkowy;

    @ManyToOne
    @JoinColumn(name = "przystanek_koncowy_id")
    private Przystanek przystanekKoncowy;

    @OneToMany(mappedBy = "relacja", cascade = CascadeType.ALL)
    private List<Wyszukiwanie> wyszukiwania;
}