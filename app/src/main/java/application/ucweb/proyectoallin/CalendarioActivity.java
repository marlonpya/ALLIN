package application.ucweb.proyectoallin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.util.Constantes;
import application.ucweb.proyectoallin.util.RowCalendario;
import butterknife.BindColor;
import butterknife.BindView;

public class CalendarioActivity extends BaseActivity implements OnDateSelectedListener{
    public static final String TAG = CalendarioActivity.class.getSimpleName();
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.calendarView) MaterialCalendarView mcv;
    @BindColor(R.color.colorAccent2) int rosado;
    private List<CalendarDay> days = new ArrayList<>();
    private int tipo_busqueda;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        iniciarLayout();
        mcv.setOnDateChangedListener(this);
        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2016, 0, 0))
                .setMaximumDate(CalendarDay.from(2100, 0, 0))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        days.add(CalendarDay.from(2016, 11, 2));
        days.add(CalendarDay.from(2016, 11, 3));
        days.add(CalendarDay.from(2016, 11, 4));
        mcv.addDecorator(new RowCalendario(this, rosado, days));

        tipo_busqueda = getIntent().getIntExtra(Constantes.BUSQUEDA_CALENDARIO, -1);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        if (isDateExist(date)) {
            String contenido = String.valueOf(date.getYear()+"-"+date.getMonth()+"-"+date.getDay());
            /*final String fecha = date.toString();
            char contenido = 0;
            for (int j = 0; j < date.toString().length(); j++) {
                if (fecha.charAt(j) == '{')
                    contenido += fecha.charAt(j);
            }
            Log.d(TAG, String.valueOf(contenido));*/
            switch (tipo_busqueda) {
                case 1: irALista(true, String.valueOf(contenido)); break;
                case 2: irALista(true, String.valueOf(contenido)); break;
                case 3: irALista(true, String.valueOf(contenido)); break;
                case 4: irALista(false, String.valueOf(contenido)); break;
                case 5: irALista(true, String.valueOf(contenido)); break;
                case 6: irALista(true, String.valueOf(contenido)); break;
            }
        } else { Toast.makeText(getApplicationContext(), "NINGÚN EVENTO REGISTRADO", Toast.LENGTH_SHORT).show(); }
    }

    private boolean isDateExist(CalendarDay day) {
        boolean resultado = false;
        for( int i = 0; i < days.size(); i++) {
            if (day.equals(days.get(i)))
                resultado = true;
        }
        return resultado;
    }

    private void irALista(boolean estado, String texto) {
        if (estado) {
            startActivity(new Intent(this, ListaDiscotecasActivity.class)
                    .putExtra(Constantes.K_S_TITULO_TOOLBAR, texto));
        } else {
            startActivity(new Intent(this, ListaEventoActivity.class)
                    .putExtra(Constantes.K_S_TITULO_TOOLBAR, texto));
        }
    }

    private void iniciarLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
    }
}
