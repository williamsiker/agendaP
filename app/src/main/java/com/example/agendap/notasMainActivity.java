package com.example.agendap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendap.adapter.adapterNota;
import com.example.agendap.model.Nota;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class notasMainActivity extends AppCompatActivity {
    Button addnota, tohome;
    RecyclerView mirecycle;
    adapterNota miadapter;
    FirebaseFirestore mifirestore;
    ImageButton mainMenu;

    Spinner spinnerVistas;
    FragmentManager fragmentManager;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas_main);
        this.setTitle("Notas");

        mifirestore = FirebaseFirestore.getInstance();
        mirecycle = findViewById(R.id.RVnotas);
        mirecycle.setLayoutManager(new LinearLayoutManager(this));
        Query query = mifirestore.collection("Nota");

        FirestoreRecyclerOptions<Nota> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Nota>().setQuery(query, Nota.class).build();
        miadapter = new adapterNota(firestoreRecyclerOptions, this);
        miadapter.notifyDataSetChanged();
        mirecycle.setAdapter(miadapter);

        addnota = findViewById(R.id.addnota);
        tohome = findViewById(R.id.tohome);
        mainMenu = findViewById(R.id.btnLeft);

        // Configurar el Spinner
        spinnerVistas = findViewById(R.id.spinnerMenu);
        fragmentManager = getSupportFragmentManager();
        setupSpinner();

        addnota.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), CrearNotaActivity.class)));

        tohome.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MenuPrincipal.class)));

        mainMenu.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    // Método para configurar el Spinner
    private void setupSpinner() {
        List<Integer> iconos = new ArrayList<>();
        iconos.add(R.drawable.view_grid); // Cambia estos íconos por los tuyos
        iconos.add(R.drawable.calendar_blank);

        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return iconos.size();
            }

            @Override
            public Object getItem(int position) {
                return iconos.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
                }

                ImageView imageView = (ImageView) convertView;
                imageView.setImageResource(iconos.get(position));

                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
                }

                ImageView imageView = (ImageView) convertView;
                imageView.setImageResource(iconos.get(position));

                return convertView;
            }
        };

        spinnerVistas.setAdapter(adapter);

        spinnerVistas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showDefaultView();
                        break;
                    case 1:
                        showCalendarView();
                        break;
                    // Aquí puedes implementar la lógica para más opciones si es necesario
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Manejar la situación cuando no se selecciona nada en el Spinner
            }
        });
    }

    private void showDefaultView() {
        // Mostrar el RecyclerView y ocultar el fragmento de calendario si estaba visible
        mirecycle.setVisibility(View.VISIBLE);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Remover el fragmento de calendario si existe
        Fragment calendarFragment = fragmentManager.findFragmentByTag("CalendarView");
        if (calendarFragment != null) {
            transaction.remove(calendarFragment);
        }
        transaction.commit();
    }

    // Método para mostrar el fragmento de calendario
    private void showCalendarView() {
        mirecycle.setVisibility(View.GONE);
        CalendarViewNotes calendarView = new CalendarViewNotes();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, calendarView);
        transaction.addToBackStack(null); // Opcional: agregar a la pila de retroceso
        transaction.commit();
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

    @Override
    public void onBackPressed() {
        // Si hay fragmentos en la pila de retroceso, los pop para regresar a la pantalla anterior
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            // Si no hay fragmentos en la pila, regresa a la pantalla de inicio
            Intent intent = new Intent(this, MenuPrincipal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // Finaliza esta actividad para evitar que vuelva a mostrarse al presionar atrás
        }
    }
}