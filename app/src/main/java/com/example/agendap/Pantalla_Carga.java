package com.example.agendap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Pantalla_Carga extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);

        firebaseAuth = FirebaseAuth.getInstance();
        int Tiempo = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               /* startActivity(new Intent(Pantalla_Carga.this,MainActivity.class));
                finish();*/
                VerificarUsuario();
            }
        },Tiempo);
    }

    private void VerificarUsuario(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser == null){
            startActivity(new Intent(Pantalla_Carga.this,MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(Pantalla_Carga.this, MenuPrincipal.class));
            finish();
        }
    }
}