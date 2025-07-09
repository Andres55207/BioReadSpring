package com.example.BioRead.repository;

import com.example.BioRead.model.RegistroAsistencia;
import com.example.BioRead.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistroAsistenciaRepository extends JpaRepository<RegistroAsistencia, Long> {

    // Búsqueda por nombre (para filtros)
    List<RegistroAsistencia> findByUsuarioNombreContainingIgnoreCase(String nombre);

    // Últimos 5 registros globales (admin)
    List<RegistroAsistencia> findTop5ByOrderByFechaHoraEntradaDesc();

    // Última entrada del usuario
    Optional<RegistroAsistencia> findTopByUsuarioOrderByFechaHoraEntradaDesc(Usuario usuario);

    // Última salida registrada del usuario
    Optional<RegistroAsistencia> findTopByUsuarioAndFechaHoraSalidaIsNotNullOrderByFechaHoraSalidaDesc(Usuario usuario);

    // Conteo por rango de fechas (para hoy y mes)
    int countByUsuarioAndFechaHoraEntradaBetween(Usuario usuario, LocalDateTime inicio, LocalDateTime fin);

    // Historial completo del usuario
    List<RegistroAsistencia> findByUsuarioOrderByFechaHoraEntradaDesc(Usuario usuario);
}
