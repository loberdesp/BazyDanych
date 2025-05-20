package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "administrator")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pasazerid", nullable = false)
    private Pasazer pasazerid;

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

}