package com.example.healthycrops;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthycrops.adapters.SensoresAdapter;
import com.example.healthycrops.model.Sensor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SensoresActivity extends AppCompatActivity {


    private RecyclerView sensoresRecyclerView;
    private SensoresAdapter adapter;
    private List<Sensor> sensores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);



        sensoresRecyclerView = findViewById(R.id.list_view);
        sensoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar sensores solo al crear la actividad
        cargarSensores();

        Button btn_agregarsen = findViewById(R.id.agregar_sensor);
        btn_agregarsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SensoresActivity.this, AgregarsenActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarSensores();
    }

    private void cargarSensores() {
        sensores = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Sensores")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            sensores.clear(); // Limpia la lista antes de agregar nuevos elementos

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("sen", document.getId() + " => " + document.getData());
                                try {
                                    Sensor sensor = document.toObject(Sensor.class);
                                    if (sensor != null) { // Verifica que la deserialización no haya fallado
                                        sensores.add(sensor);
                                    } else {
                                        Log.w("sen", "Sensor deserializado como nulo");
                                    }
                                } catch (Exception e) {
                                    Log.e("sen", "Error al deserializar: " + e.getMessage());
                                }
                            }

                            // Establece el adaptador después de llenar la lista
                            adapter = new SensoresAdapter(sensores);
                            sensoresRecyclerView.setAdapter(adapter);
                        } else {
                            Log.e("sen", "Error getting documents: ", task.getException());
                            Toast.makeText(SensoresActivity.this, "Error al obtener datos: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}