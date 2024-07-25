package com.example.agendap;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class NoteDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private final CalendarDay date;

    public NoteDecorator(Context context, int drawableRes, int color, CalendarDay date) {
        this.drawable = ContextCompat.getDrawable(context, drawableRes);
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            // Ajustar el tamaño del icono aquí
            int sizeInPixels = context.getResources().getDimensionPixelSize(R.dimen.icon_size); // Definir el tamaño deseado en dimens.xml
            drawable.setBounds(0, 0, sizeInPixels, sizeInPixels);
        }
        this.date = date;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date); // Decorar solo si el día es igual a la fecha actual
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }
}

