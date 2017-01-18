package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Compra;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 29/09/2016.
 */
public class MisComprasHistorialAdapter extends RealmBasedRecyclerViewAdapter<Compra, MisComprasHistorialAdapter.ViewHolder> {

    public MisComprasHistorialAdapter(
            Context context,
            RealmResults<Compra> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.row_historial_fragment, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {
        final Compra item = realmResults.get(i);
        BaseActivity.usarGlide(getContext(), R.drawable.plusmorado, viewHolder.iv_plusmorado);
        BaseActivity.usarGlide(getContext(), R.drawable.tacho, viewHolder.iv_row_tacho);
        viewHolder.nombre_discoteca.setText(item.getDrkere_compra());
        viewHolder.detalle_uno.setText(item.getLista_productos().get(0).getDescripcion_producto());
        viewHolder.detalle_dos.setText(item.getLista_productos().get(1).getDescripcion_producto());
        viewHolder.fecha_inicio.setText("Compró: "+item.getFecha_compra());
        viewHolder.fecha_fin.setText("Caducó: "+item.getFecha_vencimiento());
        viewHolder.pago.setText("Total S/."+String.valueOf(item.getPrecio_total()));
    }

    public class ViewHolder extends RealmViewHolder {
        @BindView(R.id.nombre_discoteca) TextView nombre_discoteca;
        @BindView(R.id.detalle_uno)TextView detalle_uno;
        @BindView(R.id.detalle_dos)TextView detalle_dos;
        @BindView(R.id.fecha_inicio)TextView fecha_inicio;
        @BindView(R.id.fecha_fin)TextView fecha_fin;
        @BindView(R.id.pago)TextView pago;
        @BindView(R.id.iv_plusmorado)ImageView iv_plusmorado;
        @BindView(R.id.iv_row_tacho)ImageView iv_row_tacho;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
