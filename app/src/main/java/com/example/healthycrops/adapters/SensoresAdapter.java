package com.example.healthycrops.adapters;

import com.example.healthycrops.R;
import com.example.healthycrops.model.Sensor;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class SensoresAdapter extends RecyclerView.Adapter<SensoresAdapter.ViewHolder> {

    private List<Sensor> sensores;
    private FirebaseFirestore db;

    public SensoresAdapter(List<Sensor> sensores) {
        this.sensores = sensores;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lsensores, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTextViewNombre().setText(sensores.get(position).getNombre());
        holder.getTextViewDescripcion().setText(sensores.get(position).getDescripcion());
        float idealValor = sensores.get(position).getIdeal();
        holder.getTextViewIdeal().setText(String.valueOf(idealValor));
        holder.getTextViewTipo().setText(sensores.get(position).getTipo().getNombre());
        holder.getTextViewUbicacion().setText(sensores.get(position).getUbicacion().getNombre());
    }

    @Override
    public int getItemCount() {
        return sensores.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNombre;
        private TextView textViewDescripcion;
        private TextView textViewIdeal;
        private TextView textViewTipo;
        private TextView textViewUbicacion;

        public ViewHolder(@NonNull View view) {
            super(view);
            textViewNombre = view.findViewById(R.id.textViewRecyclerNombre);
            textViewDescripcion = view.findViewById(R.id.textViewRecyclerDescripcion);
            textViewIdeal = view.findViewById(R.id.textViewRecyclerIdeal);
            textViewTipo = view.findViewById(R.id.textViewRecyclerTipo);
            textViewUbicacion = view.findViewById(R.id.textViewRecyclerUbicacion);
        }

        public TextView getTextViewNombre() {
            return textViewNombre;
        }

        public TextView getTextViewDescripcion() {
            return textViewDescripcion;
        }

        public TextView getTextViewIdeal() {
            return textViewIdeal;
        }

        public TextView getTextViewTipo() {
            return textViewTipo;
        }

        public TextView getTextViewUbicacion() {
            return textViewUbicacion;
        }
    }
}
