package com.example.BioRead.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String nombre;

    private String apellido;

    private String email;

    private String password; // para login

    private String huella;   // para futuro biométrico

    private String rol;

    @OneToMany(mappedBy = "usuario")
    private List<Rol> roles;

    @OneToMany(mappedBy = "usuario")
    private List<Horario> horarios;

    @OneToMany(mappedBy = "usuario")
    private List<Notificacion> notificaciones;

    @OneToMany(mappedBy = "usuario")
    private List<Permiso> permisos;

    @OneToMany(mappedBy = "usuario")
    private List<RegistroAsistencia> registroAsistencia;

    @OneToMany(mappedBy = "usuario")
    private List<ReporteDiario> reportesDiarios;

    @OneToMany(mappedBy = "usuario")
    private List<ReporteMensual> reportesMensuales;

    @OneToMany(mappedBy = "usuario")
    private List<Descanso> descansos;

    @ManyToMany
    @JoinTable(
        name = "usuario_has_tareas_administrativas",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "tarea_id")
    )
    private List<TareaAdministrativa> tareasAdministrativas;

    // Getters y setters (puedo generarlos si lo deseas)
}
