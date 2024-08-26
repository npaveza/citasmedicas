package com.example.citasmedicas.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.citasmedicas.model.CitaMedica;

@RestController
@RequestMapping("/api/citas")
public class CitasMedicasController {

    private final List<CitaMedica> citas = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    public CitasMedicasController() {
        // Inicializar con 8 citas médicas de ejemplo
        citas.add(new CitaMedica(idCounter.incrementAndGet(), "Juan Pérez", "Dr. Gómez", LocalDateTime.of(2024, 9, 1, 9, 0), "Cardiología"));
        citas.add(new CitaMedica(idCounter.incrementAndGet(), "María López", "Dra. Sánchez", LocalDateTime.of(2024, 9, 1, 10, 0), "Dermatología"));
        citas.add(new CitaMedica(idCounter.incrementAndGet(), "Carlos Martínez", "Dr. Gómez", LocalDateTime.of(2024, 9, 1, 11, 0), "Cardiología"));
        citas.add(new CitaMedica(idCounter.incrementAndGet(), "Ana González", "Dra. Hernández", LocalDateTime.of(2024, 9, 1, 12, 0), "Pediatría"));
        citas.add(new CitaMedica(idCounter.incrementAndGet(), "Luis Ramírez", "Dra. Sánchez", LocalDateTime.of(2024, 9, 1, 13, 0), "Dermatología"));
        citas.add(new CitaMedica(idCounter.incrementAndGet(), "Patricia Torres", "Dr. Gómez", LocalDateTime.of(2024, 9, 2, 9, 0), "Cardiología"));
        citas.add(new CitaMedica(idCounter.incrementAndGet(), "Miguel Díaz", "Dra. Hernández", LocalDateTime.of(2024, 9, 2, 10, 0), "Pediatría"));
        citas.add(new CitaMedica(idCounter.incrementAndGet(), "Lucía Fernández", "Dra. Sánchez", LocalDateTime.of(2024, 9, 2, 11, 0), "Dermatología"));
    }

    @PostMapping
    public ResponseEntity<CitaMedica> programarCita(@RequestBody CitaMedica nuevaCita) {
        // Validar que no haya otra cita en el mismo horario con el mismo doctor
        boolean existeConflicto = citas.stream().anyMatch(
                cita -> cita.getDoctor().equals(nuevaCita.getDoctor()) &&
                        cita.getFechaHora().equals(nuevaCita.getFechaHora())
        );

        if (existeConflicto) {
            return ResponseEntity.badRequest().body(null); // Devuelve 400 Bad Request si hay conflicto
        }

        nuevaCita.setId(idCounter.incrementAndGet());
        citas.add(nuevaCita);
        return ResponseEntity.ok(nuevaCita);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarCita(@PathVariable Long id) {
        boolean removed = citas.removeIf(cita -> cita.getId().equals(id));
        if (removed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<CitaMedica> consultarCitas() {
        return citas;
    }

    @GetMapping("/disponibilidad/{doctor}/{fecha}")
    public ResponseEntity<List<LocalTime>> consultarDisponibilidad(
            @PathVariable String doctor,
            @PathVariable LocalDate fecha) {

        // Definir el horario de trabajo del doctor (ejemplo: de 9:00 a 21:00)
        LocalTime horaInicio = LocalTime.of(9, 0);
        LocalTime horaFin = LocalTime.of(21, 0);

        // Generar la lista de horarios disponibles en intervalos de una hora
        List<LocalTime> horariosDisponibles = new ArrayList<>();
        for (LocalTime hora = horaInicio; !hora.isAfter(horaFin); hora = hora.plusHours(1)) {
            horariosDisponibles.add(hora);
        }

        // Filtrar los horarios que ya están ocupados por citas existentes
        List<LocalTime> horariosOcupados = citas.stream()
                .filter(cita -> cita.getDoctor().equals(doctor) && cita.getFechaHora().toLocalDate().equals(fecha))
                .map(cita -> cita.getFechaHora().toLocalTime())
                .collect(Collectors.toList());

        horariosDisponibles.removeAll(horariosOcupados);

        return ResponseEntity.ok(horariosDisponibles);
    }
}
