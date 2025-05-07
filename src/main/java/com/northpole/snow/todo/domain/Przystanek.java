package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Przystanek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nazwa;
    private String ulica;
    private String polozenieGPS;

    @OneToMany(mappedBy = "przystanek", cascade = CascadeType.ALL)
    private List<PrzystanekNaTrasie> przystankiNaTrasie;
}