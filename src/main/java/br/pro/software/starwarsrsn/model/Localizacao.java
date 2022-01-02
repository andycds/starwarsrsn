package br.pro.software.starwarsrsn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Localizacao {
    @Id
    private long id;
    private double latitude;
    private double longitude;
    private String base;
    private String galaxia;
}
