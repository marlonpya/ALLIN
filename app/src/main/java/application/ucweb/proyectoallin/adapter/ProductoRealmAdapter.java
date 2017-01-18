package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Producto;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 29/09/2016.
 */
public class ProductoRealmAdapter extends RealmBasedRecyclerViewAdapter<Producto, ProductoRealmAdapter.ViewHolder> {

    public ProductoRealmAdapter(
            Context context,
            RealmResults<Producto> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.row_cajear_puntos, viewGroup, false));
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {
        final Producto item = realmResults.get(i);
        viewHolder.nombre_promocion.setText(item.getDescripcion_producto());
        viewHolder.precio_normal.setText("S/."+ String.valueOf(item.getPrecio_normal()));
        viewHolder.precio_allin.setText("S/."+ String.valueOf(item.getPrecio_allin()));
        viewHolder.iv_row_imagen_canje.setImageResource(item.getImagen_producto());
        BaseActivity.usarGlide(getContext(), R.drawable.iv_row_ic_condicion, viewHolder.iv_row_ic_condiciones);
        BaseActivity.usarGlide(getContext(), R.drawable.iv_row_ic_agregar, viewHolder.iv_row_ic_agregar_a_carrito);
        BaseActivity.usarGlide(getContext(), R.drawable.iv_row_ic_canjear, viewHolder.iv_row_ic_canjear);
        viewHolder.iv_row_ic_condiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holderOptions("Condiciones");
            }
        });
        viewHolder.iv_row_ic_agregar_a_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holderOptions("Agregar a carrito");
            }
        });
        viewHolder.iv_row_ic_canjear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getContext().startActivity(new Intent(getContext().getApplicationContext(), CanjearPuntosActivity.class));
                holderOptions("Comprar");
            }
        });
    }

    public class ViewHolder extends RealmViewHolder {
        @BindView(R.id.row_tv_descripcion_promocion) TextView nombre_promocion;
        @BindView(R.id.row_tv_precio_normal) TextView precio_normal;
        @BindView(R.id.row_tv_precio_allin) TextView precio_allin;
        @BindView(R.id.iv_row_imagen_canje) ImageView iv_row_imagen_canje;
        @BindView(R.id.iv_row_ic_condiciones)ImageView iv_row_ic_condiciones;
        @BindView(R.id.iv_row_ic_agregar_a_carrito)ImageView iv_row_ic_agregar_a_carrito;
        @BindView(R.id.iv_row_ic_canjear)ImageView iv_row_ic_canjear;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void holderOptions(String texto){ Toast.makeText(getContext(), texto,Toast.LENGTH_SHORT).show(); }

}
