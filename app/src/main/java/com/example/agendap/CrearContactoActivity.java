package com.example.agendap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CrearContactoActivity extends AppCompatActivity {

    Button addcontacto;
    EditText etnombre, ettelefono, etcorreo, etdireccion;
    private FirebaseFirestore mifirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_contacto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra("id_cont");
        mifirestore = FirebaseFirestore.getInstance();

        addcontacto = findViewById(R.id.btnguardarcontacto);
        etnombre = findViewById(R.id.etnombre);
        ettelefono = findViewById(R.id.ettelefono);
        etcorreo = findViewById(R.id.etcorreo);
        etdireccion = findViewById(R.id.etdireccion);

        if (id == null || id == "") {
            addcontacto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nom, tel, corr, dir;
                    nom = etnombre.getText().toString();
                    tel = ettelefono.getText().toString();
                    corr = etcorreo.getText().toString();
                    dir = etdireccion.getText().toString();
                    almacenar(nom, tel, corr, dir);

                    startActivity(new Intent(getApplicationContext(), ContactosMainActivity.class));

                    Toast.makeText(getApplicationContext(),"Contacto Guardado.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            addcontacto.setText("Actualizar");
            getContacto(id);
            addcontacto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nom, tel, corr, dir;
                    nom = etnombre.getText().toString();
                    tel = ettelefono.getText().toString();
                    corr = etcorreo.getText().toString();
                    dir = etdireccion.getText().toString();

                    actualizar(nom, tel, corr, dir, id);

                    startActivity(new Intent(getApplicationContext(), ContactosMainActivity.class));

                    Toast.makeText(getApplicationContext(),"Contacto Guardado.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void actualizar(String nom, String tel, String corr, String dir, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nom);
        map.put("telefono", tel);
        map.put("correo", corr);
        map.put("dirreccion", dir);

        mifirestore.collection("contacto").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error al guardar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getContacto(String id) {
        mifirestore.collection("contacto").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nombreB = documentSnapshot.getString("nombre");
                String telB = documentSnapshot.getString("telefono");
                String correoB = documentSnapshot.getString("correo");
                String direccionB = documentSnapshot.getString("dirreccion");

                etnombre.setText(nombreB);
                ettelefono.setText(telB);
                etcorreo.setText(correoB);
                etdireccion.setText(direccionB);
            }
        });
    }

    private void almacenar(String nom, String tel, String corr, String dir) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", nom);
        map.put("telefono", tel);
        map.put("correo", corr);
        map.put("dirreccion", dir);

        mifirestore.collection("contacto").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), ContactosMainActivity.class));
        return false;
    }
}