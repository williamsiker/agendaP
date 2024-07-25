package com.example.agendap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agendap.adapter.tareaadapter;
import com.example.agendap.model.Tarea;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TareasMainActivity extends AppCompatActivity {

    Button addtarea, tohome;
    RecyclerView mirecycle;
    tareaadapter miadapter;
    FirebaseFirestore mifirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas_main);
        this.setTitle("Tareas");

        mifirestore = FirebaseFirestore.getInstance();
        mirecycle = findViewById(R.id.RVtareas);
        mirecycle.setLayoutManager(new LinearLayoutManager(this));
        Query query = mifirestore.collection("tarea");

        FirestoreRecyclerOptions<Tarea> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Tarea>().setQuery(query, Tarea.class).build();
        miadapter = new tareaadapter(firestoreRecyclerOptions, this);
        miadapter.notifyDataSetChanged();
        mirecycle.setAdapter(miadapter);

        addtarea = findViewById(R.id.addtarea);

        addtarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CrearTareaActivity.class));
            }
        });

        tohome = findViewById(R.id.tohometarea);

        tohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MenuPrincipal.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        miadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        miadapter.stopListening();
    }
}