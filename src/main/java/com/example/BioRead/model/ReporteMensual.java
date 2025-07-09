package com.example.BioRead.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class ReporteMensual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mes;
    private String horasTotales;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Administrador administrador;

    @ManyToMany
    @JoinTable(name = "reportemensual_descansos",
        joinColumns = @JoinColumn(name = "reporte_id"),
        inverseJoinColumns = @JoinColumn(name = "descanso_id"))
    private List<Descanso> descansos;

    // Getters y setters
}