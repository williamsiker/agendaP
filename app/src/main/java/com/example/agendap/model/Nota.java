package com.example.agendap.model;

public class Nota {
    String tittle, detalle, fecha;

    public Nota(){
    }

    public Nota(String tittle, String detalle, String fecha) {
        this.tittle = tittle;
        this.detalle = detalle;
        this.fecha = fecha;
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
}
