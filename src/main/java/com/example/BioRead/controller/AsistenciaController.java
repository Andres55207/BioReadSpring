package com.example.BioRead.controller;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.BioRead.model.RegistroAsistencia;
import com.example.BioRead.model.Usuario;
import com.example.BioRead.repository.RegistroAsistenciaRepository;
import com.example.BioRead.repository.UsuarioRepository;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/asistencia")
public class AsistenciaController {

    @Autowired
    private RegistroAsistenciaRepository asistenciaRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    // Mostrar todos los registros
    @GetMapping
    public String listarAsistencia(Model model) {
        List<RegistroAsistencia> registros = asistenciaRepo.findAll();
        model.addAttribute("registros", registros);
        return "admin/asistencia"; // Coincide con admin/asistencia.html
    }

    // Filtro por ID o nombre de usuario
    @GetMapping("/buscar")
    public String buscarAsistencia(
            @RequestParam("filtro") String filtro,
            @RequestParam("termino") String termino,
            Model model) {

        List<RegistroAsistencia> registros;

        if (filtro.equals("id")) {
            try {
                Long id = Long.parseLong(termino);
                Optional<RegistroAsistencia> resultado = asistenciaRepo.findById(id);
                registros = resultado.map(List::of).orElse(List.of());
            } catch (NumberFormatException e) {
                registros = List.of();
            }
        } else if (filtro.equals("nombre")) {
            registros = asistenciaRepo.findByUsuarioNombreContainingIgnoreCase(termino);
        } else {
            registros = asistenciaRepo.findAll();
        }

        model.addAttribute("registros", registros);
        return "admin/asistencia"; // Mismo archivo HTML
    }

    // Mostrar formulario manual
    @GetMapping("/create")
    public String mostrarFormularioManual(Model model, @ModelAttribute("mensajeExito") String mensajeExito) {
        model.addAttribute("registro", new RegistroAsistencia());
        model.addAttribute("usuarios", usuarioRepo.findAll());
        model.addAttribute("mensajeExito", mensajeExito);
        return "admin/create"; // templates/admin/create.html
    }

    // Guardar formulario manual
    @PostMapping("/guardar")
    public String guardarManual(@ModelAttribute RegistroAsistencia registro, RedirectAttributes redirectAttributes) {
        asistenciaRepo.save(registro);
        redirectAttributes.addFlashAttribute("mensajeExito", "Registro guardado exitosamente.");
        return "redirect:/admin/asistencia"; // ← Esto te redirige a la vista principal
    }

    // Registro rápido
    @PostMapping("/nuevo")
    public String nuevoRegistro(@RequestParam Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId).orElseThrow();
        RegistroAsistencia nuevo = new RegistroAsistencia();
        nuevo.setUsuario(usuario);
        nuevo.setFechaHoraEntrada(LocalDateTime.now());
        asistenciaRepo.save(nuevo);
        return "redirect:/admin/asistencia";
    }

    // Marcar salida
    @PostMapping("/salida/{id}")
    public String marcarSalida(@PathVariable Long id) {
        RegistroAsistencia registro = asistenciaRepo.findById(id).orElseThrow();
        registro.setFechaHoraSalida(LocalDateTime.now());
        asistenciaRepo.save(registro);
        return "redirect:/admin/asistencia";
    }

    // Ver detalle de un registro
    @GetMapping("/ver/{id}")
    public String verRegistro(@PathVariable Long id, Model model) {
        RegistroAsistencia registro = asistenciaRepo.findById(id).orElseThrow();
        model.addAttribute("registro", registro);
        return "admin/ver"; // templates/admin/ver.html
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        RegistroAsistencia registro = asistenciaRepo.findById(id).orElseThrow();
        model.addAttribute("registro", registro);
        model.addAttribute("usuarios", usuarioRepo.findAll());
        return "admin/editar"; // templates/admin/editar.html
    }

    // Guardar cambios de edición
    @PostMapping("/editar/{id}")
    public String guardarEdicion(@PathVariable Long id,
                                @ModelAttribute RegistroAsistencia registroEditado,
                                RedirectAttributes redirectAttributes) {
        RegistroAsistencia registro = asistenciaRepo.findById(id).orElseThrow();

        // Actualiza campos
        registro.setUsuario(registroEditado.getUsuario());
        registro.setFechaHoraEntrada(registroEditado.getFechaHoraEntrada());
        registro.setFechaHoraSalida(registroEditado.getFechaHoraSalida());

        asistenciaRepo.save(registro);
        redirectAttributes.addFlashAttribute("mensajeExito", "Registro actualizado exitosamente.");
        return "redirect:/admin/asistencia";
    }

    // Eliminar un registro
    @PostMapping("/eliminar/{id}")
    public String eliminarRegistro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        asistenciaRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Registro eliminado exitosamente.");
        return "redirect:/admin/asistencia";
    }


    // Exportar Excel
    @GetMapping("/exportar")
    public void exportarExcel(@RequestParam(required = false) String filtro,
                            @RequestParam(required = false) String termino,
                            HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=registro_asistencia.xlsx");

        List<RegistroAsistencia> registros;

        if (filtro != null && termino != null && !termino.isBlank()) {
            if (filtro.equals("id")) {
                try {
                    Long id = Long.parseLong(termino);
                    Optional<RegistroAsistencia> resultado = asistenciaRepo.findById(id);
                    registros = resultado.map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    registros = List.of();
                }
            } else if (filtro.equals("nombre")) {
                registros = asistenciaRepo.findByUsuarioNombreContainingIgnoreCase(termino);
            } else {
                registros = asistenciaRepo.findAll();
            }
        } else {
            registros = asistenciaRepo.findAll();
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Asistencia");

        // Encabezados
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Usuario");
        header.createCell(2).setCellValue("Entrada");
        header.createCell(3).setCellValue("Salida");
        header.createCell(4).setCellValue("Duración (HH:mm)");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        int rowIndex = 1;
        for (RegistroAsistencia r : registros) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(r.getId());
            row.createCell(1).setCellValue(r.getUsuario().getNombre() + " " + r.getUsuario().getApellido());

            String entradaStr = r.getFechaHoraEntrada() != null ? formatter.format(r.getFechaHoraEntrada()) : "";
            String salidaStr = r.getFechaHoraSalida() != null ? formatter.format(r.getFechaHoraSalida()) : "";
            row.createCell(2).setCellValue(entradaStr);
            row.createCell(3).setCellValue(salidaStr);

            if (r.getFechaHoraEntrada() != null && r.getFechaHoraSalida() != null) {
                Duration duracion = Duration.between(r.getFechaHoraEntrada(), r.getFechaHoraSalida());
                long horas = duracion.toHours();
                long minutos = duracion.toMinutes() % 60;
                row.createCell(4).setCellValue(String.format("%02d:%02d", horas, minutos));
            } else {
                row.createCell(4).setCellValue("—");
            }
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}