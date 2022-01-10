package com.raf.users.domain;

import javax.persistence.*;

@Entity
@Table(name = "ranks")
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double popust;

    public Rank() {
    }

    public Rank(String name) {
        this.name = name;
        this.popust = 0.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPopust() {
        return popust;
    }

    public void setPopust(Double popust) {
        this.popust = popust;
    }
}
