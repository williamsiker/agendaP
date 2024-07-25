package com.example.agendap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendap.adapter.adapterNota;
import com.example.agendap.model.Nota;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CalendarViewFragment extends BottomSheetDialogFragment {

    private static final String ARG_DATE = "selected_data";

    public static CalendarViewFragment newInstance(CalendarDay date) {
        CalendarViewFragment fragment = new CalendarViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_view_f, container, false);

        if (getArguments() != null) {
            CalendarDay selectedDate = getArguments().getParcelable(ARG_DATE);
            assert selectedDate != null;
            loadNotesForDate(view, selectedDate);
        }

        return view;
    }

    private void loadNotesForDate(View view, CalendarDay date) {
        RecyclerView recyclerView = view.findViewById(R.id.notesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(date.getDate());

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Query query = firestore.collection("Nota")
                .whereEqualTo("fecha", formattedDate);

        FirestoreRecyclerOptions<Nota> options = new FirestoreRecyclerOptions.Builder<Nota>()
                .setQuery(query, Nota.class)
                .build();

        adapterNota adapter = new adapterNota(options, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
