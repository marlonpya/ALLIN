package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import application.ucweb.proyectoallin.ListaCompraActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.Producto;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 03/11/2016.
 */
public class ListaCompraA extends RealmBasedRecyclerViewAdapter<Producto, ListaCompraA.ViewHolder>{
    public static final String TAG = ListaCompraA.class.getSimpleName();
    private TextView textView;

    public ListaCompraA(
            Context context,
            RealmResults<Producto> realmResults,
            boolean automaticUpdate,
            boolean animateResults,
            TextView textView) {
        super(context, realmResults, automaticUpdate, animateResults);
        this.textView = textView;
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.row_lista_compra, viewGroup, false));
    }

    @Override
    public void onBindRealmViewHolder(final ViewHolder viewHolder, final int i) {
        final Producto item = realmResults.get(i);
        final Realm realm = Realm.getDefaultInstance();
        viewHolder.cantidad.setText(String.valueOf(item.getCantidad()));
        viewHolder.producto.setText(item.getDescripcion_producto());
        viewHolder.precio_normal.setText(String.valueOf(item.getPrecio_normal()));
        viewHolder.precio_allin.setText(String.valueOf(item.getPrecio_allin()));
        viewHolder.agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getCantidad() <= 10) {
                    int cantidad_compra = Integer.valueOf(viewHolder.cantidad.getText().toString());
                    if (cantidad_compra <= 10) {
                        cantidad_compra ++;
                        viewHolder.cantidad.setText(String.valueOf(cantidad_compra));
                        double precio = item.getPrecio_allin();
                        ListaCompraActivity.valor_compra =+ getTotal(precio, cantidad_compra);
                        textView.setText(String.valueOf(ListaCompraActivity.valor_compra));
                        realm.beginTransaction();
                        item.setId(item.getId());
                        item.setCantidad(item.getCantidad() + 1);
                        realm.commitTransaction();
                        Log.d(TAG, item.toString());
                    }
                }
                /*
                int cantidad_actual = Integer.valueOf(viewHolder.cantidad.getText().toString());
                if (cantidad_actual < 10) {
                    cantidad_actual ++;
                    viewHolder.cantidad.setText(String.valueOf(cantidad_actual));
                    double precio = item.getPrecio_allin();
                    ListaCompraActivity.valor_compra =+ getTotal(precio, cantidad_actual);
                    textView.setText(String.valueOf(ListaCompraActivity.valor_compra));
                }*/
            }
        });
        viewHolder.remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getCantidad() > 0) {
                    int cantidad_actual = Integer.valueOf(viewHolder.cantidad.getText().toString());
                    if (cantidad_actual > 0) {
                        cantidad_actual--;
                        viewHolder.cantidad.setText(String.valueOf((cantidad_actual)));
                        double precio = item.getPrecio_allin();
                        ListaCompraActivity.valor_compra = -getTotal(precio, cantidad_actual);
                        textView.setText(String.valueOf(ListaCompraActivity.valor_compra));
                        realm.beginTransaction();
                        item.setCantidad(item.getCantidad() - 1);
                        realm.commitTransaction();
                        Log.d(TAG, item.toString());
                    }
                }
            }
        });
    }

    public static double getTotal(Double precio, int cantidad) {
        return precio * cantidad;
    }

    public class ViewHolder extends RealmViewHolder {
        @BindView(R.id.row_lista_compra_tv_producto) TextView producto;
        @BindView(R.id.row_lista_compra_tv_precio_normal) TextView precio_normal;
        @BindView(R.id.row_lista_compra_tv_precio_allin) TextView precio_allin;
        @BindView(R.id.row_lista_compra_iv_agregar) ImageView agregar;
        @BindView(R.id.row_lista_compra_tv_unidad) TextView cantidad;
        @BindView(R.id.row_lista_compra_iv_remover) ImageView remover;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cantidad.setText(""+0);
        }
    }
}
