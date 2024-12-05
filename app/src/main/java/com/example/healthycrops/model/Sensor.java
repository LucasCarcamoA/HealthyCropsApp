package com.example.healthycrops.model;

import java.util.List;

public class Sensor {
    private String id;
    private String nombre = "";
    private String descripcion = "";
    private float ideal = 0f;
    //private List<Registro> registros;
    private Tipo tipo;
    private Ubicacion ubicacion;

    public Sensor() {
        // Este constructor es necesario para Firestore
    }

    public Sensor(String nombre, String descripcion, float ideal, Tipo tipo, Ubicacion ubicacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ideal = ideal;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
    }

    public String getId() {
        return id; // Getter para el ID
    }

    public void setId(String id) {
        this.id = id; // Setter para el ID
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

    public float getIdeal() {
        return ideal;
    }

    public void setIdeal(float ideal) {
        this.ideal = ideal;
    }

    //public List<Registro> getRegistros() {
    //    return registros;
   // }

    //public void setRegistros(List<Registro> registros) {
    //    this.registros = registros;
    //}

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
}
