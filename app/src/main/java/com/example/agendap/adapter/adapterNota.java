    package com.example.agendap.adapter;

    import android.app.Activity;
    import android.content.Intent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.appcompat.view.menu.MenuView;
    import androidx.cardview.widget.CardView;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.agendap.CrearNotaActivity;
    import com.example.agendap.R;
    import com.example.agendap.model.Nota;
    import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
    import com.firebase.ui.firestore.FirestoreRecyclerOptions;
    import com.google.firebase.firestore.DocumentSnapshot;
    import com.google.firebase.firestore.FirebaseFirestore;

    public class adapterNota extends FirestoreRecyclerAdapter<Nota, adapterNota.ViewHolder> {
        private FirebaseFirestore mifarestore = FirebaseFirestore.getInstance();
        Activity activity;
        /**
         * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
         * FirestoreRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public adapterNota(@NonNull FirestoreRecyclerOptions<Nota> options, Activity activity) {
            super(options);
            this.activity = activity;
        }

        @Override
        protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Nota model) {
            DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
            final String id = documentSnapshot.getId();

            holder.tvtitulo.setText(model.getTittle());
            holder.tvdes.setText(model.getDetalle());
            holder.tvfecha.setText(model.getFecha());

            holder.cvnotas.setOnClickListener(view -> {
                Intent i = new Intent(activity, CrearNotaActivity.class);
                i.putExtra("id_not", id);
                activity.startActivity(i);
            });

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewnotassigle, parent, false);
            return new ViewHolder(v);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvtitulo, tvdes, tvfecha;
            CardView cvnotas;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tvtitulo = itemView.findViewById(R.id.tvtitulo);
                tvdes = itemView.findViewById(R.id.tvdes);
                tvfecha = itemView.findViewById(R.id.tvfecha);
                cvnotas = itemView.findViewById(R.id.cvnotas);
            }
        }
    }
