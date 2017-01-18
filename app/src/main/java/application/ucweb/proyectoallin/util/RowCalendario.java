package application.ucweb.proyectoallin.util;

import android.content.Context;
import android.view.View;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import application.ucweb.proyectoallin.R;

/**
 * Created by ucweb02 on 02/11/2016.
 */
public class RowCalendario implements DayViewDecorator {
    private final int color;
    private final HashSet<CalendarDay> dates;
    private Context context;

    public RowCalendario(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    public RowCalendario(Context context, int color, HashSet<CalendarDay> dates) {
        this.color = color;
        this.dates = dates;
        this.context = context;
    }

    public RowCalendario(Context context, int color, List<CalendarDay> days) {
        this.color = color;
        this.dates = new HashSet<>(days);
        this.context = context;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.addSpan(new DotSpan(5, color));
        view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.e_row_calendario));
    }

}
