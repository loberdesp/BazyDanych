package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Trasa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nazwaTrasy;
    private String numerTrasy;
    private boolean kierunek;

    @OneToMany(mappedBy = "trasa", cascade = CascadeType.ALL)
    private List<Kurs> kursy;

    @OneToMany(mappedBy = "trasa", cascade = CascadeType.ALL)
    private List<PrzystanekNaTrasie> przystanki;
}