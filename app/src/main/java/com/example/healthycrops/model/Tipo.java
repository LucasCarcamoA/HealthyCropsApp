package com.example.healthycrops.model;

public class Tipo {
    private String nombre;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tipo)) return false;
        Tipo tipo = (Tipo) obj;
        return nombre.equals(tipo.nombre);
    }

    public Tipo() {
        // Este constructor es necesario para Firestore
    }

    public String toString(){
        return this.nombre;
    }

    public Tipo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
