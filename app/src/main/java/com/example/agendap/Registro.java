package com.example.agendap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {
    EditText NombreET,CorreoET,ConstraseñaET,ConfirmarContET;
    Button RegistrarUsuario;
    TextView TengoCuentaTXT;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    //
    String Nombre = "", Correo = "", Password = "", Confirmarpass = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registrar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        NombreET = findViewById(R.id.NombreET);
        CorreoET = findViewById(R.id.CorreoET);
        ConstraseñaET = findViewById(R.id.ConstraseñaET);
        ConfirmarContET = findViewById(R.id.ConfirmarContET);
        RegistrarUsuario = findViewById(R.id.RegistrarUsuario);
        TengoCuentaTXT = findViewById(R.id.TengoCuentaTXT);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setTitle("Espere un momento");
        progressDialog.setCanceledOnTouchOutside(false);

        RegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validacion de Datos
                ValidarDatos();
            }
        });

        TengoCuentaTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Login Activity
                startActivity(new Intent(Registro.this,Login.class));
            }
        });
    }

    private void ValidarDatos(){
        Nombre = NombreET.getText().toString();
        Correo = CorreoET.getText().toString();
        Password = ConstraseñaET.getText().toString();
        Confirmarpass = ConfirmarContET.getText().toString();

        if(TextUtils.isEmpty(Nombre)){
            Toast.makeText(this, "Ingrese su Nombre", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(Correo).matches()){
            Toast.makeText(this, "Ingrese su Correo", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Ingrese su Contraseña", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Confirmarpass)){
            Toast.makeText(this, "Confirme su Contraseña", Toast.LENGTH_SHORT).show();
        }else if(!Password.equals(Confirmarpass)){
            Toast.makeText(this, "Las Contraseñas no son iguales", Toast.LENGTH_SHORT).show();
        }else{
            CrearCuenta();
        }
    }

    private void CrearCuenta() {
        progressDialog.setMessage("Creando su cuenta...");
        progressDialog.show();

        //Usuario en FB
        firebaseAuth.createUserWithEmailAndPassword(Correo,Password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Existoso
                        GuardarInfo();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void GuardarInfo() {
        progressDialog.setTitle("Guardando su informacion");
        progressDialog.dismiss();
        //id de la autenticacion FB
        String uid = firebaseAuth.getUid();

        //configurar los datos
        HashMap<String,String> Datos = new HashMap<>();
        Datos.put("uid",uid);
        Datos.put("Correo",Correo);
        Datos.put("Nombres",Nombre);
        Datos.put("Contraseña",Password);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, "Cuenta Creada con Exito", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registro.this,MenuPrincipal.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}