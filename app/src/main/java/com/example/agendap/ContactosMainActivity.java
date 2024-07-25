package com.example.agendap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agendap.adapter.contactoadapter;
import com.example.agendap.model.Contacto;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ContactosMainActivity extends AppCompatActivity {

    Button addcontacto, tohomecont;
    RecyclerView mirecycle;
    contactoadapter miadapter;
    FirebaseFirestore mifirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_main);
        this.setTitle("Contactos");

        mifirestore = FirebaseFirestore.getInstance();
        mirecycle = findViewById(R.id.RVcontactos);
        mirecycle.setLayoutManager(new LinearLayoutManager(this));
        Query query = mifirestore.collection("contacto");

        FirestoreRecyclerOptions<Contacto> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Contacto>().setQuery(query, Contacto.class).build();
        miadapter = new contactoadapter(firestoreRecyclerOptions, this);
        miadapter.notifyDataSetChanged();
        mirecycle.setAdapter(miadapter);

        addcontacto = findViewById(R.id.addcontacto);

        addcontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CrearContactoActivity.class));
            }
        });

        tohomecont = findViewById(R.id.tohomecont);

        tohomecont.setOnClickListener(new View.OnClickListener() {
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