package com.northpole.snow.todo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "przystaneknatrasie")
public class Przystaneknatrasie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "czasprzejazdu", nullable = false)
    private Integer czasprzejazdu;

    @NotNull
    @Column(name = "kolejnosc", nullable = false)
    private Integer kolejnosc;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "przystanekid", nullable = false)
    private Przystanek przystanekid;

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

    public Integer getCzasprzejazdu() {
        return czasprzejazdu;
    }

    public void setCzasprzejazdu(Integer czasprzejazdu) {
        this.czasprzejazdu = czasprzejazdu;
    }

    public Integer getKolejnosc() {
        return kolejnosc;
    }

    public void setKolejnosc(Integer kolejnosc) {
        this.kolejnosc = kolejnosc;
    }

    public Przystanek getPrzystanekid() {
        return przystanekid;
    }

    public void setPrzystanekid(Przystanek przystanekid) {
        this.przystanekid = przystanekid;
    }

    public Trasa getTrasaid() {
        return trasaid;
    }

    public void setTrasaid(Trasa trasaid) {
        this.trasaid = trasaid;
    }

}