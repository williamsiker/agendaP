package com.example.agendap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.WorkManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.UUID;

public class CrearTareaActivity extends AppCompatActivity {

    Button selectfecha, selecthora, guardar, eliminar;
    TextView tvfecha, tvhora;
    EditText ettarea, etdetalles;
    private FirebaseFirestore mifirestore;

    Calendar actual = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();

    private int minutos, hora, dia, mes, anio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);
        this.setTitle("Crear Tarea");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra("id_tar");
        mifirestore = FirebaseFirestore.getInstance();

        selectfecha = findViewById(R.id.BTN_SELECFECHA);
        selecthora = findViewById(R.id.BTN_SELECHORA);
        guardar = findViewById(R.id.BTN_GUARNOTI);
        eliminar = findViewById(R.id.BTN_ELIMNOTI);
        tvfecha = findViewById(R.id.TV_FECHASELEC);
        tvhora = findViewById(R.id.TV_HORASELEC);
        ettarea = findViewById(R.id.ettarea);
        etdetalles = findViewById(R.id.etdetalles);

        selectfecha.setOnClickListener(new View.OnClickListener() {
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

        selecthora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hora = actual.get(Calendar.HOUR_OF_DAY);
                minutos = actual.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        calendar.set(Calendar.HOUR_OF_DAY,h);
                        calendar.set(Calendar.MINUTE,m);

                        tvhora.setText(String.format("%02d:%02d", h, m));
                    }
                },hora, minutos, true);
                timePickerDialog.show();
            }
        });

        if(id == null || id == ""){
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tag = generateKey();
                    Long alerttime = calendar.getTimeInMillis() - System.currentTimeMillis();
                    int random = (int)(Math.random()*50 + 1);

                    //Data data = GuardarData("Notificacion workmanager", "soy un detalle", random);
                    String tareatitle = ettarea.getText().toString(), tareadetalle = etdetalles.getText().toString();

                    Data data = GuardarData(tareatitle, tareadetalle, random);
                    Workmanagernoti.Guardarnoti(alerttime, data, tag);

                    almacenar(tareatitle, tareadetalle, tag);

                    startActivity(new Intent(getApplicationContext(), TareasMainActivity.class));

                    Toast.makeText(getApplicationContext(),"Alarma Guardada.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            guardar.setText("Actualizar");
            getTarea(id);
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mifirestore.collection("tarea").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String tag = documentSnapshot.getString("id_tarea");
                            EliminarNoti(tag);
                            Long alerttime = calendar.getTimeInMillis() - System.currentTimeMillis();
                            int random = (int)(Math.random()*50 + 1);

                            //Data data = GuardarData("Notificacion workmanager", "soy un detalle", random);
                            String tareatitle = ettarea.getText().toString(), tareadetalle = etdetalles.getText().toString();

                            Data data = GuardarData(tareatitle, tareadetalle, random);
                            Workmanagernoti.Guardarnoti(alerttime, data, tag);
                            editartarea(tareatitle, tareadetalle, id);
                        }
                    });

                    startActivity(new Intent(getApplicationContext(), TareasMainActivity.class));

                    Toast.makeText(getApplicationContext(),"Alarma Guardada.", Toast.LENGTH_SHORT).show();
                }
            });
        }



        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EliminarNoti("tag1");
            }
        });
    }

    private void editartarea(String titulo, String detalle, String id){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = format.format(calendar.getTime());
        String horaet = tvhora.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("tittle", titulo);
        map.put("detalle", detalle);
        map.put("fecha", strDate);
        map.put("hora", horaet);

        mifirestore.collection("tarea").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void EliminarNoti(String tag){
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
        Toast.makeText(getApplicationContext(),"Alarma Eliminada.", Toast.LENGTH_SHORT).show();
    }

    private String generateKey(){
        return UUID.randomUUID().toString();
    }

    private Data GuardarData(String titulo, String detalle, int id_noti){

        return new Data.Builder()
                .putString("titulo",titulo)
                .putString("detalle",detalle)
                .putInt("id_noti", id_noti).build();
    }

    private void almacenar(String tittle, String detalle, String id_noti){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = format.format(calendar.getTime());
        String horaet = tvhora.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("tittle", tittle);
        map.put("detalle", detalle);
        map.put("id_tarea", id_noti);
        map.put("fecha", strDate);
        map.put("hora",  horaet);

        mifirestore.collection("tarea").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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

    private void getTarea(String id){
        mifirestore.collection("tarea").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fechaB = documentSnapshot.getString("fecha");
                String horaB = documentSnapshot.getString("hora");
                String tituloB = documentSnapshot.getString("tittle");
                String detalleB = documentSnapshot.getString("detalle");

                ettarea.setText(tituloB);
                etdetalles.setText(detalleB);
                tvfecha.setText(fechaB);
                tvhora.setText(horaB);

                int dia = Integer.parseInt(fechaB.substring(0,2));
                int mes = Integer.parseInt(fechaB.substring(3,5));
                int anio = Integer.parseInt(fechaB.substring(6,10));
                int hora = Integer.parseInt(horaB.substring(0,2));
                int minuto = Integer.parseInt(horaB.substring(3,5));

                calendar.set(Calendar.DAY_OF_MONTH, dia);
                calendar.set(Calendar.MONTH, mes);
                calendar.set(Calendar.YEAR, anio);
                calendar.set(Calendar.HOUR_OF_DAY,hora);
                calendar.set(Calendar.MINUTE,minuto);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(getApplicationContext(), TareasMainActivity.class));
        return false;
    }
}