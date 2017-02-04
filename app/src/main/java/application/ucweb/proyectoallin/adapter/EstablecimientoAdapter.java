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
import java.util.Date;
import java.util.Locale;

import application.ucweb.proyectoallin.EventoActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.modelparseable.EstablecimientoSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 27/01/2017.
 */

public class EstablecimientoAdapter extends RecyclerView.Adapter<EstablecimientoAdapter.ViewHolder> {
    private Context context;
    private ArrayList<EstablecimientoSimple> establecimientoSimples;
    private LayoutInflater inflater;

    public EstablecimientoAdapter(Context context, ArrayList<EstablecimientoSimple> establecimientoSimples) {
        this.context = context;
        this.establecimientoSimples = establecimientoSimples;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.row_establecimiento, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EstablecimientoSimple item = this.establecimientoSimples.get(position);
        BaseActivity.setImageConGlideCircular(context, holder.imagen, item.getImagen());
        BaseActivity.setImageConGlideCircular(context, holder.contorno, R.drawable.circulo_con_lineas);
        holder.nombre.setText(item.getNombre());
        Date horaInicio = item.getDateInicio();
        Date horaFin = item.getDateFin();
        SimpleDateFormat sdfHora = new SimpleDateFormat("hh:mm a", new Locale("es", "pe"));
        if (horaInicio!=null) {
            holder.hora.setText("Hora: " + sdfHora.format(horaInicio) + " - " + sdfHora.format(horaFin));
        }
        if (!item.isGay()) holder.gay.setVisibility(View.GONE);
        holder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventoActivity.class);
                intent.putExtra(Constantes.K_S_TITULO_TOOLBAR, item.getNombre());
                intent.putExtra(Constantes.K_L_ID_EVENTO, item.getId_server());
                intent.putExtra(Constantes.OBJ_S_ESTABLECIMIENTO, item);
                context.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.row_iv_establecimiento_contorno) ImageView contorno;
        @BindView(R.id.row_iv_establecimiento_imagen) ImageView imagen;
        @BindView(R.id.row_tv_establecimiento_nombre) TextView nombre;
        @BindView(R.id.row_tv_establecimiento_fecha) TextView fecha;
        @BindView(R.id.row_tv_establecimiento_hora) TextView hora;
        @BindView(R.id.row_btnEstablecimiento) LinearLayout boton;
        @BindView(R.id.ivGay) ImageView gay;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return establecimientoSimples.size();
    }
}
