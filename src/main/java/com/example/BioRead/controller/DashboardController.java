package com.example.BioRead.controller;

import com.example.BioRead.model.RegistroAsistencia;
import com.example.BioRead.model.Usuario;
import com.example.BioRead.repository.RegistroAsistenciaRepository;
import com.example.BioRead.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private RegistroAsistenciaRepository asistenciaRepo;

    @GetMapping("/admin/dashboard")
    public String dashboardAdmin(Model model, Principal principal) {
        model.addAttribute("usuario", principal.getName());

        long totalUsuarios = usuarioRepo.count();
        model.addAttribute("totalUsuarios", totalUsuarios);

        List<RegistroAsistencia> registros = asistenciaRepo.findTop5ByOrderByFechaHoraEntradaDesc();
        model.addAttribute("registros", registros);

        return "admin/dashboard";
    }

    @GetMapping("/usuario/dashboard")
    public String dashboardUsuario(Model model, Principal principal) {
        String nombreUsuario = principal.getName();
        Optional<Usuario> usuarioOpt = usuarioRepo.findByNombre(nombreUsuario);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            RegistroAsistencia ultimaEntrada = asistenciaRepo
                .findTopByUsuarioOrderByFechaHoraEntradaDesc(usuario)
                .orElse(null);

            RegistroAsistencia ultimaSalida = asistenciaRepo
                .findTopByUsuarioAndFechaHoraSalidaIsNotNullOrderByFechaHoraSalidaDesc(usuario)
                .orElse(null);

            LocalDate hoy = LocalDate.now();

            int registrosHoy = asistenciaRepo.countByUsuarioAndFechaHoraEntradaBetween(
                usuario,
                hoy.atStartOfDay(),
                hoy.plusDays(1).atStartOfDay()
            );

            LocalDate inicioMes = hoy.withDayOfMonth(1);
            int registrosMes = asistenciaRepo.countByUsuarioAndFechaHoraEntradaBetween(
                usuario,
                inicioMes.atStartOfDay(),
                inicioMes.plusMonths(1).atStartOfDay()
            );

            // Historial completo del usuario
            List<RegistroAsistencia> historial = asistenciaRepo.findByUsuarioOrderByFechaHoraEntradaDesc(usuario);

            model.addAttribute("usuario", nombreUsuario);
            model.addAttribute("ultimaEntrada", ultimaEntrada);
            model.addAttribute("ultimaSalida", ultimaSalida);
            model.addAttribute("registrosHoy", registrosHoy);
            model.addAttribute("registrosMes", registrosMes);
            model.addAttribute("historial", historial); // añadido
        }

        return "usuarios/dashboard";
    }
}
