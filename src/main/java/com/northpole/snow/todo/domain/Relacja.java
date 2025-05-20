package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "relacja")
public class Relacja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pasazerid", nullable = false)
    private Pasazer pasazerid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "przystanekpoczatkowyid", nullable = false)
    private Przystanek przystanekpoczatkowyid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "przystanekkoncowyid", nullable = false)
    private Przystanek przystanekkoncowyid;

    @OneToMany(mappedBy = "relacjaid")
    private Set<Wyszukanie> wyszukania = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pasazer getPasazerid() {
        return pasazerid;
    }

    public void setPasazerid(Pasazer pasazerid) {
        this.pasazerid = pasazerid;
    }

    public Przystanek getPrzystanekpoczatkowyid() {
        return przystanekpoczatkowyid;
    }

    public void setPrzystanekpoczatkowyid(Przystanek przystanekpoczatkowyid) {
        this.przystanekpoczatkowyid = przystanekpoczatkowyid;
    }

    public Przystanek getPrzystanekkoncowyid() {
        return przystanekkoncowyid;
    }

    public void setPrzystanekkoncowyid(Przystanek przystanekkoncowyid) {
        this.przystanekkoncowyid = przystanekkoncowyid;
    }

    public Set<Wyszukanie> getWyszukania() {
        return wyszukania;
    }

    public void setWyszukania(Set<Wyszukanie> wyszukania) {
        this.wyszukania = wyszukania;
    }

}