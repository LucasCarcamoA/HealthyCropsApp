package com.example.healthycrops.model;

public class Ubicacion {
    private String nombre;
    private String descripcion;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ubicacion)) return false;
        Ubicacion ubicacion = (Ubicacion) obj;
        return nombre.equals(ubicacion.nombre);
    }

    public Ubicacion(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    public Ubicacion() {
        // Este constructor es necesario para Firestore
    }

    public String toString(){
        return this.nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
