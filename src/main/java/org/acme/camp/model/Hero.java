package org.acme.camp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String classe;

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

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    @Override
    public String toString() {
        return "Hero{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", classe='" + classe + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hero hero)) return false;
        return Objects.equals(getId(), hero.getId())
            && Objects.equals(getName(), hero.getName())
            && Objects.equals(getClasse(), hero.getClasse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getClasse());
    }
}
