package com.example.healthycrops;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthycrops.Data.Repositorio;
import com.example.healthycrops.model.Sensor;
import com.example.healthycrops.model.Tipo;
import com.example.healthycrops.model.Ubicacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgregarubiActivity extends AppCompatActivity {

    private List<Ubicacion> ubicaciones;
    private EditText ubicacionEditText, descripcionEditText;
    private Ubicacion ubicacion;

    private Button buscarUbiButton;
    private Button modificarUbiButton;
    private Button eliminarUbiButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarubi);

        ubicaciones = Repositorio.getInstance().ubicaciones;
        Button ingresarUbicacionButton = findViewById(R.id.agregarUbicacionButton);
        ubicacionEditText = findViewById(R.id.nombreUbiEditText);
        descripcionEditText = findViewById(R.id.descripcionUbiEditText);

        buscarUbiButton = findViewById(R.id.buscarUbicacionButton);
        modificarUbiButton = findViewById(R.id.modificarUbicacionButton);
        eliminarUbiButton = findViewById(R.id.eliminarUbicacionButton);

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        ingresarUbicacionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarUbicacionAlRepositorio();
            }
        });

        buscarUbiButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String nombre = ubicacionEditText.getText().toString();

                db.collection("Ubicaciones").whereEqualTo("nombre", nombre).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    if (task.getResult().isEmpty()) {
                                        Toast.makeText(AgregarubiActivity.this, "No se encontró ninguna ubicación con ese nombre", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {

                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            if (doc.get("nombre").equals(nombre)){
                                                ubicacion = doc.toObject(Ubicacion.class);
                                            }

                                            // ELIMINAR XD
                                            eliminarUbiButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    db.collection("Ubicaciones").document(doc.getId())
                                                            .delete()
                                                            .addOnSuccessListener(aVoid -> {
                                                                Toast.makeText(AgregarubiActivity.this, "Ubicacion eliminada", Toast.LENGTH_SHORT).show();
                                                                ubicacionEditText.setText("");
                                                                descripcionEditText.setText("");
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Toast.makeText(AgregarubiActivity.this, "Error al eliminar la ubicacion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            });
                                                }
                                            });

                                            // MODIFICAR XD

                                            modificarUbiButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    String id = doc.getId();
                                                    String nuevoNombre = ubicacionEditText.getText().toString();
                                                    String nuevaDescripcion = descripcionEditText.getText().toString();

                                                    if (nuevoNombre.isEmpty() || nuevoNombre.length() < 5 || nuevoNombre.length() > 15) {
                                                        Toast.makeText(AgregarubiActivity.this, "El nuevo nombre debe tener entre 5 y 15 carácteres", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }


                                                    // Crear un mapa con los nuevos valores
                                                    Map<String, Object> updates = new HashMap<>();
                                                    updates.put("nombre", nuevoNombre);
                                                    updates.put("descripcion", nuevaDescripcion);

                                                    DocumentReference docref = db.collection("Ubicaciones").document(id);

                                                    docref.update(updates)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(AgregarubiActivity.this, "Ubicacion actualizada", Toast.LENGTH_LONG).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(AgregarubiActivity.this, "Error al actualizar la ubicacion: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                }
                                            });



                                        }
                                        descripcionEditText.setText(ubicacion.getDescripcion());
                                    }}
                            }
                        });
            }
        });


    }
    private void agregarUbicacionAlRepositorio(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String nombre = ubicacionEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese el nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nombre.length() < 5 || nombre.length() > 15) {
            Toast.makeText(this, "El nombre debe tener entre 5 y 15 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!descripcion.isEmpty() && descripcion.length() > 30) {
            Toast.makeText(this, "La descripción debe tener un máximo de 30 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        if (descripcion.isEmpty()) {
            descripcion = "N/A";
        }

        Ubicacion nuevaUbicacion = new Ubicacion(nombre, descripcion);

        db.collection("Ubicaciones").document()
                .set(nuevaUbicacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AgregarubiActivity.this, "Ingreso exitoso",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AgregarubiActivity.this, "Error de Ingreso",
                                Toast.LENGTH_LONG).show();
                    }
                });

        Repositorio.getInstance().agregarUbicacion(nuevaUbicacion);

        Toast.makeText(this, "Ubicación agergada: " + nuevaUbicacion.getNombre(), Toast.LENGTH_SHORT).show();

        finish();

    }
}