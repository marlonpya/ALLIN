package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.Producto;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 08/11/2016.
 */
public class DetalleCompraAdapter extends RealmBasedRecyclerViewAdapter<Producto, DetalleCompraAdapter.ViewHolder> {
    public DetalleCompraAdapter(
            Context context,
            RealmResults<Producto> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.row_detalle_compra, viewGroup, false));
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {
        final Producto item = realmResults.get(i);
        if (item.isA_carrito()) {
            viewHolder.precio.setText("S/. " + String.valueOf(item.getPrecio_allin()));
            viewHolder.cantidad.setText("("+String.valueOf(item.getCantidad())+") ");
            viewHolder.producto.setText(item.getDescripcion_producto());
        }
    }

    public class ViewHolder extends RealmViewHolder {
        @BindView(R.id.row_detalle_compra_tv_cantidad) TextView cantidad;
        @BindView(R.id.row_detalle_compra_tv_precio) TextView precio;
        @BindView(R.id.row_detalle_compra_tv_producto) TextView producto;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
