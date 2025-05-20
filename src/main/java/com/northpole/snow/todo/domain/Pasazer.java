package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "pasazer")
public class Pasazer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "imienazwisko", nullable = false, length = 100)
    private String imienazwisko;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 15)
    @NotNull
    @Column(name = "numertelefonu", nullable = false, length = 15)
    private String numertelefonu;

    @Size(max = 50)
    @NotNull
    @Column(name = "login", nullable = false, length = 50)
    private String login;

    @Size(max = 100)
    @NotNull
    @Column(name = "haslo", nullable = false, length = 100)
    private String haslo;

    @OneToOne(mappedBy = "pasazerid")
    private Administrator administrator;

    @OneToMany(mappedBy = "pasazerid")
    private Set<Relacja> relacje = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImienazwisko() {
        return imienazwisko;
    }

    public void setImienazwisko(String imienazwisko) {
        this.imienazwisko = imienazwisko;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumertelefonu() {
        return numertelefonu;
    }

    public void setNumertelefonu(String numertelefonu) {
        this.numertelefonu = numertelefonu;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public Set<Relacja> getRelacje() {
        return relacje;
    }

    public void setRelacje(Set<Relacja> relacje) {
        this.relacje = relacje;
    }

}