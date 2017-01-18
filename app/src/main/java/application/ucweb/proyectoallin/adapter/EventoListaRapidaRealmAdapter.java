package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import application.ucweb.proyectoallin.EstablecimientoActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.Establecimiento;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 21/09/2016.
 */
public class EventoListaRapidaRealmAdapter extends RealmBasedRecyclerViewAdapter<Establecimiento, EventoListaRapidaRealmAdapter.ViewHolder> {

    public EventoListaRapidaRealmAdapter(
            Context context,
            RealmResults<Establecimiento> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.row_lista_rapida, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {
        final Establecimiento item = realmResults.get(i);
        viewHolder.tvNomFiesta.setText(item.getNombre_evento());
        viewHolder.tvNomDiscoteca.setText(item.getNombre());
        viewHolder.tvHora.setText("Hora: "+String.valueOf(item.getFecha_inicio()+"pm"));
        viewHolder.tvFecha.setText("FECHA AÑADIDO \n"+item.getFecha_fin());
        viewHolder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EstablecimientoActivity.class);
                intent.putExtra(Constantes.K_S_TITULO_TOOLBAR, item.getNombre_evento());
                getContext().startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RealmViewHolder {
        @BindView(R.id.tvNomFiesta) TextView tvNomFiesta;
        @BindView(R.id.tvNomDiscoteca)TextView tvNomDiscoteca;
        @BindView(R.id.tvHora)TextView tvHora;
        @BindView(R.id.tvFecha)TextView tvFecha;
        @BindView(R.id.row_btnListaRapida) LinearLayout boton;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
