package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "przystanek")
public class Przystanek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "nazwa", nullable = false, length = 100)
    private String nazwa;

    @Size(max = 100)
    @NotNull
    @Column(name = "ulica", nullable = false, length = 100)
    private String ulica;

    @Size(max = 100)
    @Column(name = "polozenie", length = 100)
    private String polozenie;

    @OneToMany(mappedBy = "przystanekid")
    private Set<Przystaneknatrasie> przystankinatrasie = new LinkedHashSet<>();

    @OneToMany(mappedBy = "przystanekpoczatkowyid")
    private Set<Relacja> relacjepocz = new LinkedHashSet<>();

    @OneToMany(mappedBy = "przystanekkoncowyid")
    private Set<Relacja> relacjekonc = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getPolozenie() {
        return polozenie;
    }

    public void setPolozenie(String polozenie) {
        this.polozenie = polozenie;
    }

    public Set<Przystaneknatrasie> getPrzystankinatrasie() {
        return przystankinatrasie;
    }

    public void setPrzystankinatrasie(Set<Przystaneknatrasie> przystankinatrasie) {
        this.przystankinatrasie = przystankinatrasie;
    }

    public Set<Relacja> getRelacjepocz() {
        return relacjepocz;
    }

    public void setRelacjepocz(Set<Relacja> relacjepocz) {
        this.relacjepocz = relacjepocz;
    }

    public Set<Relacja> getRelacjekonc() {
        return relacjekonc;
    }

    public void setRelacjekonc(Set<Relacja> relacjekonc) {
        this.relacjekonc = relacjekonc;
    }

}