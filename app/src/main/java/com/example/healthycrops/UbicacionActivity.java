package com.example.healthycrops;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthycrops.Data.Repositorio;
import com.example.healthycrops.adapters.UbicacionesAdapter;
import com.example.healthycrops.model.Ubicacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UbicacionActivity extends AppCompatActivity {

    private RecyclerView ubicacionesRecyclerView;
    private UbicacionesAdapter adapter;
    private List<Ubicacion> ubicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        ubicacionesRecyclerView = findViewById(R.id.lista_ubicacion);
        ubicacionesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializa la lista de ubicaciones
        ubicaciones = new ArrayList<>();
        actualizarListaUbicaciones();

        Button btn_ubicacion = findViewById(R.id.op_agregar_ubi);
        btn_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UbicacionActivity.this, AgregarubiActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Puedes volver a actualizar la lista si es necesario
        actualizarListaUbicaciones();
    }

    private void actualizarListaUbicaciones() {
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
                                    if (ubicacion != null) { // Verifica que la deserialización no haya fallado
                                        ubicaciones.add(ubicacion); // Agrega la ubicación a la lista
                                    } else {
                                        Log.w("sen", "Ubicación deserializada como nula");
                                    }
                                } catch (Exception e) {
                                    Log.e("sen", "Error al deserializar: " + e.getMessage());
                                }
                            }

                            // Establece el adaptador después de llenar la lista
                            adapter = new UbicacionesAdapter(ubicaciones);
                            ubicacionesRecyclerView.setAdapter(adapter);
                        } else {
                            Log.e("sen", "Error getting documents: ", task.getException());
                            Toast.makeText(UbicacionActivity.this, "Error al obtener datos: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}