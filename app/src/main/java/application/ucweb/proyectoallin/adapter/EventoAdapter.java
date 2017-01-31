package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import application.ucweb.proyectoallin.EstablecimientoActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.modelparseable.EventoSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb03 on 27/01/2017.
 */

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {
    private Context context;
    private ArrayList<EventoSimple> eventos;
    private LayoutInflater inflater;

    public EventoAdapter(Context context, ArrayList<EventoSimple> eventos) {
        this.context = context;
        this.eventos = eventos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_evento, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final EventoSimple item = eventos.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE d 'de' yyyy ", new Locale("es", "pe"));

        BaseActivity.setImageConGlideCircular(context, viewHolder.imagen_evento, item.getImagen());
        BaseActivity.setImageConGlideCircular(context, viewHolder.contorno, R.drawable.circulo_con_lineas);
        viewHolder.nombre.setText(item.getNombre());
        Date fecha = item.getFecha_inicio();
        viewHolder.fecha.setText("Fecha: " + (fecha == null ? "" : sdf.format(fecha)));
        viewHolder.hora.setText("Hora: " + (fecha == null ? "" : (fecha.getHours() + " : " + fecha.getMinutes()) + " : " + (fecha.getSeconds() >= 10 ? fecha.getSeconds() : "0" + fecha.getSeconds())));
        viewHolder.discoteca.setText(getNombreLocal(item.getNombre_local()));
        viewHolder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EstablecimientoActivity.class);
                intent.putExtra(Constantes.K_S_TITULO_TOOLBAR, item.getNombre());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_iv_evento_contorno) ImageView contorno;
        @BindView(R.id.row_iv_imagen_evento) ImageView imagen_evento;
        @BindView(R.id.row_tv_evento_nombre)TextView nombre;
        @BindView(R.id.row_tv_evento_fecha)TextView fecha;
        @BindView(R.id.row_tv_evento_hora)TextView hora;
        @BindView(R.id.row_tv_evento_discoteca)TextView discoteca;
        @BindView(R.id.row_btnEventoEspecial) LinearLayout boton;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private static String getNombreLocal(String local) {
        return local == null || local.equalsIgnoreCase("null") || local.isEmpty() ? "Local: Desconocido" : local;
    }
}
