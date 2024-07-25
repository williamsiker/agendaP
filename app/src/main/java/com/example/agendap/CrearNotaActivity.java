package com.example.agendap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CrearNotaActivity extends AppCompatActivity {

    Button btncalendario, addnota;
    TextView tvfecha;
    EditText ettitulo, etdescripcion;
    private FirebaseFirestore mifirestore;

    Calendar actual = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();

    private int dia, mes, anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nota);
        this.setTitle("Crear Nota");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra("id_not");
        mifirestore = FirebaseFirestore.getInstance();

        btncalendario = findViewById(R.id.Btn_Calendario);
        addnota = findViewById(R.id.addNota);
        tvfecha = findViewById(R.id.Fecha);
        ettitulo = findViewById(R.id.titulo);
        etdescripcion = findViewById(R.id.descripcion);

        btncalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anio = actual.get(Calendar.YEAR);
                mes = actual.get(Calendar.MONTH);
                dia = actual.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        calendar.set(Calendar.DAY_OF_MONTH, d);
                        calendar.set(Calendar.MONTH, m);
                        calendar.set(Calendar.YEAR, y);

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate = format.format(calendar.getTime());
                        tvfecha.setText(strDate);
                    }
                },anio,mes,dia);
                datePickerDialog.show();
            }
        });

        if(id == null || id.isEmpty()){
            addnota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String titulo, descripcion, fecha;
                    titulo = ettitulo.getText().toString();
                    descripcion = etdescripcion.getText().toString();
                    fecha = tvfecha.getText().toString();

                    almacenar(titulo, descripcion, fecha);

                    //startActivity(new Intent(getApplicationContext(), notasMainActivity.class));
                    getSupportFragmentManager().popBackStack();
                    Toast.makeText(getApplicationContext(),"Nota Guardada.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            addnota.setText("Actualizar");
            getNota(id);
            addnota.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String titulo, descripcion, fecha;
                    titulo = ettitulo.getText().toString();
                    descripcion = etdescripcion.getText().toString();
                    fecha = tvfecha.getText().toString();

                    actualizar(titulo, descripcion, fecha, id);

                    //startActivity(new Intent(getApplicationContext(), notasMainActivity.class));
                    getSupportFragmentManager().popBackStack();

                    Toast.makeText(getApplicationContext(),"Nota Actualizada.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void getNota(String id) {
        mifirestore.collection("Nota").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fechaB = documentSnapshot.getString("fecha");
                String tituloB = documentSnapshot.getString("tittle");
                String detalleB = documentSnapshot.getString("detalle");

                tvfecha.setText(fechaB);
                ettitulo.setText(tituloB);
                etdescripcion.setText(detalleB);

                assert fechaB != null;
                int diab = Integer.parseInt(fechaB.substring(0,2));
                int mesb = Integer.parseInt(fechaB.substring(3,5));
                int aniob = Integer.parseInt(fechaB.substring(6,10));

                calendar.set(Calendar.DAY_OF_MONTH, diab);
                calendar.set(Calendar.MONTH, mesb);
                calendar.set(Calendar.YEAR, aniob);
            }
        });
    }

    private void actualizar(String titulo, String descripcion, String fecha, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("tittle", titulo);
        map.put("detalle", descripcion);
        map.put("fecha", fecha);

        mifirestore.collection("Nota").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al Actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void almacenar(String titulo, String descripcion, String fecha) {
        Map<String, Object> map = new HashMap<>();
        map.put("tittle", titulo);
        map.put("detalle", descripcion);
        map.put("fecha", fecha);

        mifirestore.collection("Nota").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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
        //startActivity(new Intent(getApplicationContext(), notasMainActivity.class));
        //getSupportFragmentManager().popBackStack();
        onBackPressed();
        return false;
    }
}