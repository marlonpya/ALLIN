package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.modelparseable.ItemCarrito;
import application.ucweb.proyectoallin.modelparseable.ProductoSimple;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 06/02/2017.
 */

public class DetalleCompraAdapter2 extends RecyclerView.Adapter<DetalleCompraAdapter2.ViewHolder>{

    private Context context;
    private ArrayList<ItemCarrito> productos;
    private LayoutInflater inflater;

    public DetalleCompraAdapter2(Context context, ArrayList<ItemCarrito> productos) {
        this.context = context;
        this.productos = productos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public DetalleCompraAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_detalle_compra, parent, false));
    }

    @Override
    public void onBindViewHolder(DetalleCompraAdapter2.ViewHolder holder, int position) {
        final ItemCarrito item = productos.get(position);
        //if (item.isA_carrito()) {
            holder.precio.setText("S/. " + String.valueOf(item.getPrecio_allin()*item.getCantidad()));
            holder.cantidad.setText("("+String.valueOf(item.getCantidad())+") ");
            holder.producto.setText(item.getNombre());
        //}
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_detalle_compra_tv_cantidad) TextView cantidad;
        @BindView(R.id.row_detalle_compra_tv_precio) TextView precio;
        @BindView(R.id.row_detalle_compra_tv_producto) TextView producto;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
