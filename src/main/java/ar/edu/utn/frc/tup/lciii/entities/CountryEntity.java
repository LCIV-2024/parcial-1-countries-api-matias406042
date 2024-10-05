package ar.edu.utn.frc.tup.lciii.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Data
@Entity
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable= false)
    private Long id;
    @Column(name = "nombre")
    private  String nombre;

    @Column(name = "codigo")
    private String codigo ;

    @Column(name = "area")
    private String area;

    @Column(name = "poblacion")
    private String poblacion;

}
