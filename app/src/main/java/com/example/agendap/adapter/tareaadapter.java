package com.example.agendap.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import com.example.agendap.CrearTareaActivity;
import com.example.agendap.R;
import com.example.agendap.model.Tarea;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class tareaadapter extends FirestoreRecyclerAdapter<Tarea, tareaadapter.ViewHolder> {
    private FirebaseFirestore mifarestore = FirebaseFirestore.getInstance();
    Activity activity;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public tareaadapter(@NonNull FirestoreRecyclerOptions<Tarea> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Tarea model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.tittle.setText(model.getTittle());
        holder.detalle.setText(model.getDetalle());

        String auxfecha = model.getFecha().substring(0,5);
        holder.fecha.setText(auxfecha);

        holder.hora.setText(model.getHora());

        holder.cvtar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, CrearTareaActivity.class);
                i.putExtra("id_tar", id);
                activity.startActivity(i);
            }
        });

        holder.btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarTarea(id);

            }
        });
    }

    private void eliminarTarea(String id) {
        mifarestore.collection("tarea").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String tag = documentSnapshot.getString("id_tarea");
                WorkManager.getInstance(activity).cancelAllWorkByTag(tag);
                mifarestore.collection("tarea").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "Error al Eliminar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewtareasingle, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tittle, detalle, fecha, hora;
        ImageView btneliminar;
        CardView cvtar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tittle = itemView.findViewById(R.id.tittle_m);
            detalle = itemView.findViewById(R.id.detalle_m);
            fecha = itemView.findViewById(R.id.fecha);
            hora = itemView.findViewById(R.id.hora);
            btneliminar = itemView.findViewById(R.id.btneliminar);
            cvtar = itemView.findViewById(R.id.cvtar);
        }
    }
}
