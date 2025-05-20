package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@Entity
@Table(name = "kurs")
public class Kurs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "godzinastartu", nullable = false)
    private LocalTime godzinastartu;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trasaid", nullable = false)
    private Trasa trasaid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalTime getGodzinastartu() {
        return godzinastartu;
    }

    public void setGodzinastartu(LocalTime godzinastartu) {
        this.godzinastartu = godzinastartu;
    }

    public Trasa getTrasaid() {
        return trasaid;
    }

    public void setTrasaid(Trasa trasaid) {
        this.trasaid = trasaid;
    }

}