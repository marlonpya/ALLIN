package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.util.Log;
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
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 23/09/2016.
 */
public class CartaEstablecimientoRealmAdapter extends RealmBasedRecyclerViewAdapter<Producto, CartaEstablecimientoRealmAdapter.ViewHolder> {
    public static final String TAG = CartaEstablecimientoRealmAdapter.class.getSimpleName();
    private TextView textView;
    public CartaEstablecimientoRealmAdapter(
            Context context,
            RealmResults<Producto> realmResults,
            boolean automaticUpdate,
            boolean animateResults,
            TextView textView) {
        super(context, realmResults, automaticUpdate, animateResults);
        this.textView = textView;
    }

    @Override
    public CartaEstablecimientoRealmAdapter.ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.row_promocion, viewGroup, false));
    }

    @Override
    public void onBindRealmViewHolder(CartaEstablecimientoRealmAdapter.ViewHolder viewHolder, int i) {
        if (textView.getText() == null) textView.setText(""+0);
        final Producto item = realmResults.get(i);
        final Realm realm = Realm.getDefaultInstance();
        viewHolder.promocion.setText(item.getDescripcion_producto());
        viewHolder.puntos_consumo.setText(item.getPuntos_allin() + "pts Consumo");
        viewHolder.imagen.setImageResource(item.getImagen_producto());
        //BaseActivity.usarGlide(getContext(), item.getImagen_producto(), viewHolder.imagen);
        BaseActivity.usarGlide(getContext(), R.drawable.iv_row_ic_condicion, viewHolder.icono_condiciones);
        BaseActivity.usarGlide(getContext(), R.drawable.iv_row_ic_agregar, viewHolder.icono_agregar);
        BaseActivity.usarGlide(getContext(), R.drawable.iv_row_ic_canjear, viewHolder.icono_canjear);

        viewHolder.icono_condiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { holderOptions("Condiciones");}
        });
        viewHolder.icono_canjear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holderOptions("Canjear");
            }
        });
        viewHolder.icono_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isA_carrito()) {
                    realm.beginTransaction();
                    item.setId(item.getId());
                    item.setA_carrito(true);
                    realm.commitTransaction();
                    Log.d(TAG, item.toString());
                    holderOptions("Agregado a carrito");
                    int cantidad = Integer.parseInt(textView.getText().toString());
                    textView.setText(String.valueOf(cantidad + 1));
                }else{
                    holderOptions("Ya fue agregado");
                }
                notifyDataSetChanged();
            }
        });
//        viewHolder.icono_canjear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { getContext().startActivity(new Intent(getContext().getApplicationContext(), CanjearPuntosActivity.class));            }
//        });
    }

    public static class ViewHolder extends RealmViewHolder {
        @BindView(R.id.row_tv_promocion) TextView promocion;
        @BindView(R.id.row_tv_puntos_consumo)TextView puntos_consumo;
        @BindView(R.id.iv_row_imagen_canje)ImageView imagen;
        @BindView(R.id.iv_row_ic_condiciones)ImageView icono_condiciones;
        @BindView(R.id.iv_row_ic_agregar_a_carrito)ImageView icono_agregar;
        @BindView(R.id.iv_row_ic_canjear)ImageView icono_canjear;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void holderOptions(String texto){
        Toast.makeText(getContext(), texto,Toast.LENGTH_SHORT).show();
    }

}
