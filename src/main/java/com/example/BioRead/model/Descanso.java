package com.example.BioRead.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Descanso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mes;
    private String tiempoDescanso;

    @ManyToOne
    private Usuario usuario;

    @ManyToMany(mappedBy = "descansos")
    private List<ReporteMensual> reportesMensuales;

    // Getters y setters
}
