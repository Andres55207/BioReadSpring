
package com.example.BioRead.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "administrador")
    private List<Rol> roles;

    @OneToMany(mappedBy = "administrador")
    private List<ReporteMensual> reportesMensuales;

    // Getters y setters
}