package com.example.agendap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipal extends AppCompatActivity {
    ImageButton CerrarSesion, irnotas, irtareas, ircontactos;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView NombrePrn,CorreoPrn;
    ProgressBar progressBarDatos;
    DatabaseReference Usuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("AgendaP");

        NombrePrn = findViewById(R.id.NombrePrn);
        CorreoPrn = findViewById(R.id.CorreoPrn);
        progressBarDatos = findViewById(R.id.PBDatos);

        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        CerrarSesion = findViewById(R.id.CerrarSesion);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalirAplicacion();
            }
        });

        irnotas = findViewById(R.id.irnotas);
        irtareas= findViewById(R.id.irtareas);
        ircontactos = findViewById(R.id.ircontactos);

        irnotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), notasMainActivity.class));
            }
        });

        irtareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TareasMainActivity.class));
            }
        });

        ircontactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ContactosMainActivity.class));
            }
        });


    }

    @Override
    protected void onStart() {
        VIncioSesion();
        super.onStart();
    }

    private void VIncioSesion(){
        if(user != null){
            CargarDatos();
        }else{
            startActivity(new Intent(MenuPrincipal.this,MainActivity.class));
            finish();
        }
    }

    private void CargarDatos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //usuario existente
                if(snapshot.exists()){
                    //desaparecer PBD
                    progressBarDatos.setVisibility(View.GONE);

                    //mostrando datos
                    NombrePrn.setVisibility(View.VISIBLE);
                    CorreoPrn.setVisibility(View.VISIBLE);


                    String nombre = ""+snapshot.child("Nombres").getValue();
                    String correo = ""+snapshot.child("Correo").getValue();

                    //Poniedo Datos
                    NombrePrn.setText(nombre);
                    CorreoPrn.setText(correo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this,MainActivity.class));
        Toast.makeText(this, "Cerrando Sesion con Exito!!", Toast.LENGTH_SHORT).show();
    }
}