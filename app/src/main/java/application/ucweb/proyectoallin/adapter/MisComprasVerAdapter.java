package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Compra;
import application.ucweb.proyectoallin.model.Producto;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 26/09/2016.
 */
public class MisComprasVerAdapter extends RealmBasedRecyclerViewAdapter<Compra, MisComprasVerAdapter.ViewHolder> {

    public MisComprasVerAdapter(
            Context context,
            RealmResults<Compra> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.row_mis_compras, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {
        final Compra item = realmResults.get(i);
        BaseActivity.usarGlide(getContext(), R.drawable.plusrosa, viewHolder.iv_plusrosa);
        viewHolder.fecha_inicio.setText(item.getFecha_compra());
        viewHolder.fecha_fin.setText("VENCE: "+item.getFecha_vencimiento());
        viewHolder.nombre_discoteca.setText(item.getDrkere_compra());
        viewHolder.nombre_evento.setText("EVENTO: "+item.getEvento_compra());
        viewHolder.detalle_uno.setText(item.getLista_productos().get(0).getDescripcion_producto());
        viewHolder.detalle_dos.setText(item.getLista_productos().get(1).getDescripcion_producto());
        viewHolder.pago.setText("S/."+String.valueOf(item.getPrecio_total()));
    }

    public class ViewHolder extends RealmViewHolder {
        @BindView(R.id.fecha_inicio) TextView fecha_inicio;
        @BindView(R.id.fecha_fin)TextView fecha_fin;
        @BindView(R.id.nombre_discoteca)TextView nombre_discoteca;
        @BindView(R.id.nombre_evento)TextView nombre_evento;
        @BindView(R.id.detalle_uno)TextView detalle_uno;
        @BindView(R.id.detalle_dos)TextView detalle_dos;
        @BindView(R.id.pago)TextView pago;
        @BindView(R.id.iv_plusrosa)ImageView iv_plusrosa;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
