package com.example.healthycrops.Data;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.healthycrops.AgregarsenActivity;
import com.example.healthycrops.model.Sensor;
import com.example.healthycrops.model.Tipo;
import com.example.healthycrops.model.Ubicacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Repositorio {
    private static Repositorio instance = null;

    public List<Sensor> sensores;
    public List<Tipo> tiposSensor;
    public List<Ubicacion> ubicaciones;

    protected Repositorio(){
        sensores = new ArrayList<>();
        ubicaciones = new ArrayList<>();

        tiposSensor = new ArrayList<>();
        tiposSensor.add(new Tipo("Humedad"));
        tiposSensor.add(new Tipo("Temperatura"));
        ubicaciones = new ArrayList<>();
        ubicaciones.add(new Ubicacion("Invernadero", "Plantas en invernadero"));
        ubicaciones.add(new Ubicacion("Hidroponico", "Lechugas en hidroponia"));
        ubicaciones.add(new Ubicacion("Arándanos", "Sector de cosecha de arándanos"));
        sensores.add(new Sensor("Sensor De Prueba 1", "Esta es una descripción", 1.4f, tiposSensor.get(1), ubicaciones.get(0)));
        sensores.add(new Sensor("Sensor De Prueba 2", "N/A", 70f, tiposSensor.get(0), ubicaciones.get(1)));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Ubicaciones")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ubicaciones.clear(); // Limpia la lista antes de agregar nuevos elementos

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("sen", document.getId() + " => " + document.getData());
                                try {
                                    Ubicacion ubicacion = document.toObject(Ubicacion.class);
                                    if (ubicacion != null) {
                                        ubicaciones.add(ubicacion);
                                    } else {
                                        Log.w("ub", "Ubicacion deserializado como nulo");
                                    }
                                } catch (Exception e) {
                                    Log.e("ub", "Error al deserializar: " + e.getMessage());
                                }
                            }


                        } else {
                            Log.e("sen", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static synchronized Repositorio getInstance(){
        if (instance == null) {
            instance = new Repositorio();
        }
        return instance;
    }

    public void agregarSensor(Sensor sensor) {
        sensores.add(sensor);
    }

    public void agregarUbicacion(Ubicacion ubicacion) {
        ubicaciones.add(ubicacion);
    }



}
