package com.example.agendap.model;

public class Tarea {
    String tittle, detalle, fecha, hora;
    public Tarea(){
    }
    public Tarea(String tittle, String detalle, String fecha, String hora) {
        this.tittle = tittle;
        this.detalle = detalle;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}

