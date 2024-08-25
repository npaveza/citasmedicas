package com.example.citasmedicas.model;

import java.time.LocalDateTime;

public class CitaMedica {

    private Long id;
    private String paciente;
    private String doctor;
    private LocalDateTime fechaHora;
    private String especialidad;

    public CitaMedica(Long id, String paciente, String doctor, LocalDateTime fechaHora, String especialidad) {
        this.id = id;
        this.paciente = paciente;
        this.doctor = doctor;
        this.fechaHora = fechaHora;
        this.especialidad = especialidad;
    }

    // Getters y Setters

    public Long getId(){
        return id;
    }

    public String getPaciente(){
        return paciente;
    }

    public String getDoctor(){
        return doctor;
    }

    public LocalDateTime getFechaHora(){
        return fechaHora;
    }

    public String getEspecialidad(){
        return especialidad;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setNombreProducto(String paciente){
        this.paciente = paciente;
    }

    public void setDestino(String doctor){
        this.doctor = doctor;
    }

    public void setUbicacionActual(LocalDateTime fechaHora){
        this.fechaHora = fechaHora;
    }

    public void setEstadoEnvio(String especialidad){
        this.especialidad = especialidad;
    }

    
}
