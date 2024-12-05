package com.example.healthycrops;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgregarsenActivity extends AppCompatActivity {


    private Sensor sensor;
    private Spinner tipoSensorSpinner;
    private Spinner ubicacionSensorSpinner;
    private List<Sensor> sensores;
    private List<Tipo> tiposSensor;
    private List<Ubicacion> ubicaciones;
    private EditText nombreSensorEditText, descripcionEditText, valorIdealEditText;

    private Button buscarSensorButton;
    private Button eliminarSensorButton;
    private Button modificarSensorButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarsen);

        sensores = Repositorio.getInstance().sensores;
        tiposSensor = Repositorio.getInstance().tiposSensor;
        ubicaciones = Repositorio.getInstance().ubicaciones;
        ubicacionSensorSpinner = findViewById(R.id.ubicacionSensorSpinner);
        Button ingresarSensorButton = findViewById(R.id.agregarsen_Button);
        tipoSensorSpinner = findViewById(R.id.tipoSensorSpinner);
        nombreSensorEditText = findViewById(R.id.nombreSensor);
        descripcionEditText = findViewById(R.id.descripcionSensor);
        valorIdealEditText = findViewById(R.id.idealSensor);

        buscarSensorButton = findViewById(R.id.buscarSen_Button);
        eliminarSensorButton = findViewById(R.id.eliminarSen_Button);
        modificarSensorButton = findViewById(R.id.modificarSen_Button);





        buscarSensorButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String nombre = nombreSensorEditText.getText().toString();

                db.collection("Sensores").whereEqualTo("nombre", nombre).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    if (task.getResult().isEmpty()) {
                                        Toast.makeText(AgregarsenActivity.this, "No se encontró ningún sensor con ese nombre", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else {

                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        if (doc.get("nombre").equals(nombre)){
                                            sensor = doc.toObject(Sensor.class);
                                        }

                                        // ELIMINAR XD
                                        eliminarSensorButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                db.collection("Sensores").document(doc.getId())
                                                        .delete()
                                                        .addOnSuccessListener(aVoid -> {
                                                            Toast.makeText(AgregarsenActivity.this, "Sensor eliminado", Toast.LENGTH_SHORT).show();

                                                            nombreSensorEditText.setText("");
                                                            descripcionEditText.setText("");
                                                            valorIdealEditText.setText("");
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Toast.makeText(AgregarsenActivity.this, "Error al eliminar el sensor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        });
                                            }
                                        });

                                        // MODIFICAR XD

                                        modificarSensorButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String id = doc.getId();
                                                String nuevoNombre = nombreSensorEditText.getText().toString();
                                                String nuevaDescripcion = descripcionEditText.getText().toString();
                                                String nuevoValorIdeal = valorIdealEditText.getText().toString();

                                                if (nuevoValorIdeal.isEmpty() || Float.parseFloat(nuevoValorIdeal) <= 0) {
                                                    Toast.makeText(AgregarsenActivity.this, "No se acepta valor 0 para modificar", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                // Extraer el tipo y ubicación seleccionados
                                                Tipo nuevoTipo = (Tipo) tipoSensorSpinner.getSelectedItem(); // Asegúrate de que el Spinner tenga objetos Tipo
                                                Ubicacion nuevaUbicacion = (Ubicacion) ubicacionSensorSpinner.getSelectedItem(); // Asegúrate de que el Spinner tenga objetos Ubicacion

                                                // Crear un mapa con los nuevos valores
                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("nombre", nuevoNombre);
                                                updates.put("descripcion", nuevaDescripcion);
                                                updates.put("ideal", Float.parseFloat(nuevoValorIdeal)); // Asegúrate de manejar excepciones aquí si es necesario

                                                if (nuevoTipo != null) {
                                                    updates.put("tipo", nuevoTipo); // Agrega el tipo al mapa
                                                } else {
                                                    Toast.makeText(AgregarsenActivity.this, "No se seleccionó un Tipo válido", Toast.LENGTH_SHORT).show();
                                                    return; // Salir si no hay tipo válido
                                                }

                                                if (nuevaUbicacion != null) {
                                                    updates.put("ubicacion", nuevaUbicacion); // Agrega la ubicación al mapa
                                                } else {
                                                    Toast.makeText(AgregarsenActivity.this, "No se seleccionó una Ubicación válida", Toast.LENGTH_SHORT).show();
                                                    return; // Salir si no hay ubicación válida
                                                }

                                                DocumentReference docref = db.collection("Sensores").document(id);

                                                // Realiza una única actualización con todos los campos

                                                docref.update(updates)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(AgregarsenActivity.this, "Sensor actualizado", Toast.LENGTH_LONG).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(AgregarsenActivity.this, "Error al actualizar el sensor: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                            }
                                        });



                                    }

                                    descripcionEditText.setText(sensor.getDescripcion());
                                    valorIdealEditText.setText(String.valueOf(sensor.getIdeal())); //con String.valueOf transformo en string el getIdeal float

                                    Tipo tipoSeleccionado = sensor.getTipo();
                                    ArrayAdapter<Tipo> adapter = (ArrayAdapter<Tipo>) tipoSensorSpinner.getAdapter();
                                    int position = adapter.getPosition(tipoSeleccionado);
                                    tipoSensorSpinner.setSelection(position);

                                    Ubicacion ubicacionSeleccionada = sensor.getUbicacion();
                                    ArrayAdapter<Ubicacion> adapter1 = (ArrayAdapter<Ubicacion>) ubicacionSensorSpinner.getAdapter();
                                    int positionUbi = adapter1.getPosition(ubicacionSeleccionada);
                                    ubicacionSensorSpinner.setSelection(positionUbi);

                                }}
                            }
                        });
            }
        });


        ArrayAdapter<Tipo> adapter = new ArrayAdapter<>(this, R.layout.tipo_sensor_spinner_item, tiposSensor);
        tipoSensorSpinner.setAdapter(adapter);
        ArrayAdapter<Ubicacion> adapter1 = new ArrayAdapter<>(this, R.layout.tipo_sensor_spinner_item, ubicaciones);
        ubicacionSensorSpinner.setAdapter(adapter1);

        ingresarSensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                agregarSensorAlRepositorio();
            }
        });
    }

    private void agregarSensorAlRepositorio() {



        String nombre = nombreSensorEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString();
        String ideal = valorIdealEditText.getText().toString();
        Tipo tipoSeleccionado = (Tipo) tipoSensorSpinner.getSelectedItem();
        Ubicacion ubicacionSeleccionada = (Ubicacion) ubicacionSensorSpinner.getSelectedItem();


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

        float idealValue;
        try {
            idealValue = Float.parseFloat(ideal);
            if (idealValue <= 0){
                Toast.makeText(this, "El valor ideal debe ser positivo", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor ideal no válido", Toast.LENGTH_SHORT).show();
            return;
        }



        Sensor nuevoSensor = new Sensor(nombre, descripcion, idealValue, tipoSeleccionado, ubicacionSeleccionada);



        db.collection("Sensores").document()
                        .set(nuevoSensor).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AgregarsenActivity.this, "Ingreso exitoso" ,
                                Toast.LENGTH_LONG).show();


                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AgregarsenActivity.this, "Error de Ingreso",
                                        Toast.LENGTH_LONG).show();
                            }
                        });




        Repositorio.getInstance().agregarSensor(nuevoSensor);

        Toast.makeText(this,  "Sensor agregado: " + nuevoSensor.getNombre(), Toast.LENGTH_SHORT).show();

        finish();
    }
}
