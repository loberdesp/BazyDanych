package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "trasa")
public class Trasa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "nazwatrasy", nullable = false, length = 100)
    private String nazwatrasy;

    @NotNull
    @Column(name = "numertrasy", nullable = false)
    private Integer numertrasy;

    @NotNull
    @Column(name = "kierunek", nullable = false)
    private Short kierunek;

    @OneToMany(mappedBy = "trasaid")
    private Set<Kurs> kurs = new LinkedHashSet<>();

    @OneToMany(mappedBy = "trasaid", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Przystaneknatrasie> przystankinatrasie = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNazwatrasy() {
        return nazwatrasy;
    }

    public void setNazwatrasy(String nazwatrasy) {
        this.nazwatrasy = nazwatrasy;
    }

    public Integer getNumertrasy() {
        return numertrasy;
    }

    public void setNumertrasy(Integer numertrasy) {
        this.numertrasy = numertrasy;
    }

    public Short getKierunek() {
        return kierunek;
    }

    public void setKierunek(Short kierunek) {
        this.kierunek = kierunek;
    }

    public Set<Kurs> getKurs() {
        return kurs;
    }

    public void setKurs(Set<Kurs> kurs) {
        this.kurs = kurs;
    }

    public Set<Przystaneknatrasie> getPrzystankinatrasie() {
        return przystankinatrasie;
    }

    public void setPrzystankinatrasie(Set<Przystaneknatrasie> przystankinatrasie) {
        this.przystankinatrasie = przystankinatrasie;
    }

}