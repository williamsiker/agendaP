package com.example.agendap;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class CalendarViewNotes extends Fragment {

    private MaterialCalendarView calendarView;
    private FirebaseFirestore firestore;
    private Set<CalendarDay> noteDays;
    private SimpleDateFormat dateFormat;

    public CalendarViewNotes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.calendar_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        firestore = FirebaseFirestore.getInstance();
        noteDays = new HashSet<>();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fetchNoteDates();

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                if (selected) {
                    // Mostrar CalendarViewFragment como un diálogo
                    CalendarViewFragment fragment = CalendarViewFragment.newInstance(date);
                    fragment.show(requireFragmentManager(), "CalendarViewFragment");
                }
            }
        });
        // Escucha los cambios de fecha seleccionada
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                if (selected) {
                    // Mostrar CalendarViewFragment como un diálogo
                    CalendarViewFragment fragment = CalendarViewFragment.newInstance(date);
                    fragment.show(requireFragmentManager(), "CalendarViewFragment");
                }
            }
        });
    }

    private void fetchNoteDates() {
        firestore.collection("Nota")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            // Supongamos que cada documento tiene un campo "fecha" que es una cadena
                            String dateStr = document.getString("fecha");
                            CalendarDay date = convertStringToCalendarDay(dateStr);
                            if (date != null) {
                                noteDays.add(date);
                            }
                        }
                        addDecorators();
                    } else {
                        Log.e("CalendarViewNotes", "Error getting documents.", task.getException());
                    }
                });
    }

    private CalendarDay convertStringToCalendarDay(String dateStr) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(dateStr));
            return CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            Log.e("CalendarViewNotes", "Error parsing date string.", e);
            return null;
        }
    }

    private void addDecorators() {
        for (CalendarDay day : noteDays) {
            calendarView.addDecorator(new NoteDecorator(requireContext(), R.drawable.square_outline, Color.RED, day));
        }
    }
}
