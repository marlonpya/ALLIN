package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import application.ucweb.proyectoallin.EstablecimientoActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Establecimiento;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 23/09/2016.
 */
public class EventoEspecialRealmAdapter extends RealmBasedRecyclerViewAdapter<Establecimiento, EventoEspecialRealmAdapter.ViewHolder>{

    public EventoEspecialRealmAdapter(
            Context context,
            RealmResults<Establecimiento> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public EventoEspecialRealmAdapter.ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.row_evento, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindRealmViewHolder(EventoEspecialRealmAdapter.ViewHolder viewHolder, int i) {
        final Establecimiento item = realmResults.get(i);
        BaseActivity.setImageConGlideCircular(getContext(), viewHolder.imagen_evento, item.getImagen());
        BaseActivity.setImageConGlideCircular(getContext(), viewHolder.contorno, R.drawable.circulo_con_lineas);
        viewHolder.nombre.setText(item.getNombre_evento());
        viewHolder.fecha.setText("Fecha: "+ item.getFecha_inicio());
        viewHolder.hora.setText("Hora: "+ item.getFecha_fin());
        viewHolder.discoteca.setText("Discoteca: Night Life");
        viewHolder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EstablecimientoActivity.class);
                intent.putExtra(Constantes.K_S_TITULO_TOOLBAR, item.getNombre_evento());
                getContext().startActivity(intent);
            }
        });
    }

    public static class ViewHolder extends RealmViewHolder {
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
}
