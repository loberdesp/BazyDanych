package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Pasazer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imieNazwisko;
    private String email;
    private String numerTelefonu;
    private String login;
    private String haslo;

    @OneToOne(mappedBy = "pasazer", cascade = CascadeType.ALL)
    private Administrator administrator;

    @OneToMany(mappedBy = "pasazer", cascade = CascadeType.ALL)
    private List<Relacja> relacje;
}