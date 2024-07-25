package com.example.agendap.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendap.CrearContactoActivity;
import com.example.agendap.R;
import com.example.agendap.model.Contacto;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class contactoadapter extends FirestoreRecyclerAdapter<Contacto, contactoadapter.ViewHolder> {
    private FirebaseFirestore mifarestore = FirebaseFirestore.getInstance();
    Activity activity;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public contactoadapter(@NonNull FirestoreRecyclerOptions<Contacto> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Contacto model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        holder.tvnombre.setText(model.getNombre());
        holder.tvtelefono.setText(model.getTelefono());
        holder.tvcorreo.setText(model.getCorreo());

        holder.cvcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, CrearContactoActivity.class);
                i.putExtra("id_cont", id);
                activity.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewcontactosingle, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvnombre, tvtelefono, tvcorreo;
        CardView cvcont;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvnombre = itemView.findViewById(R.id.tvnombre);
            tvtelefono = itemView.findViewById(R.id.tvnumero);
            tvcorreo = itemView.findViewById(R.id.tvcorreo);
            cvcont = itemView.findViewById(R.id.cvcont);
        }
    }
}
