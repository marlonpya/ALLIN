package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Producto;
import application.ucweb.proyectoallin.modelparseable.ProductoSimple;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 03/02/2017.
 */

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder>{

    private Context context;
    private ArrayList<ProductoSimple> productos;
    private LayoutInflater inflater;

    public ProductoAdapter(Context context, ArrayList<ProductoSimple> productos){
        this.context = context;
        this.productos = productos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_cajear_puntos, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ProductoSimple item = productos.get(position);
        holder.nombre_promocion.setText(item.getNombre());
        holder.precio_normal.setText("S/."+ String.valueOf(item.getPrecio_normal()));
        holder.precio_allin.setText("S/."+ String.valueOf(item.getPrecio_allin()));
        Glide.with(context).load(item.getImagen()).into(holder.iv_row_imagen_canje);
        BaseActivity.usarGlide(context, R.drawable.iv_row_ic_condicion, holder.iv_row_ic_condiciones);
        BaseActivity.usarGlide(context, R.drawable.iv_row_ic_agregar, holder.iv_row_ic_agregar_a_carrito);
        BaseActivity.usarGlide(context, R.drawable.iv_row_ic_canjear, holder.iv_row_ic_canjear);
        holder.iv_row_ic_condiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holderOptions("Condiciones");
            }
        });
        holder.iv_row_ic_agregar_a_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holderOptions("Agregar a carrito");
            }
        });
        holder.iv_row_ic_canjear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getContext().startActivity(new Intent(getContext().getApplicationContext(), CanjearPuntosActivity.class));
                //holderOptions("Comprar");
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
}
