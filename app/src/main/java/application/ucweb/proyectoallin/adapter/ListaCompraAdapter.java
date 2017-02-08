package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import application.ucweb.proyectoallin.ListaCompraActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.modelparseable.ItemCarrito;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 07/02/2017.
 */

public class ListaCompraAdapter extends RecyclerView.Adapter<ListaCompraAdapter.ViewHolder> {
    public static final String TAG = ListaCompraAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<ItemCarrito> productos;
    private LayoutInflater inflater;
    private TextView textView;

    public ListaCompraAdapter(Context context, ArrayList<ItemCarrito> productos, TextView textView) {
        this.context = context;
        this.productos = productos;
        this.inflater = LayoutInflater.from(context);
        this.textView = textView;
    }

    @Override
    public ListaCompraAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_lista_compra, parent, false));
    }

    @Override
    public void onBindViewHolder(final ListaCompraAdapter.ViewHolder holder, int position) {
        final ItemCarrito item = productos.get(position);
        holder.cantidad.setText(String.valueOf(item.getCantidad()));
        holder.producto.setText(item.getNombre());
        holder.precio_normal.setText(String.valueOf(item.getPrecio_normal()));
        holder.precio_allin.setText(String.valueOf(item.getPrecio_allin()));
        holder.cantidad.setText(String.valueOf(item.getCantidad()));
        textView.setText(String.valueOf(getTotal()));
        holder.agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getCantidad() <= 8) {
                    item.setCantidad(item.getCantidad()+1);
                    holder.cantidad.setText(String.valueOf(item.getCantidad()));
                    textView.setText(String.valueOf(getTotal()));
                }
            }
        });
        holder.remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getCantidad() > 0){
                    item.setCantidad(item.getCantidad()-1);
                    holder.cantidad.setText(String.valueOf(item.getCantidad()));
                    textView.setText(String.valueOf(getTotal()));
                }
            }
        });
    }

    public ArrayList<ItemCarrito> getArray() {
        return productos;
    }

    private double getTotal() {
        double total=0;
        for (int i = 0; i < productos.size(); i++) {
            double  subtotal = productos.get(i).getPrecio_allin()*productos.get(i).getCantidad();
            total += subtotal;
        }
        return total;
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_lista_compra_tv_producto) TextView producto;
        @BindView(R.id.row_lista_compra_tv_precio_normal) TextView precio_normal;
        @BindView(R.id.row_lista_compra_tv_precio_allin) TextView precio_allin;
        @BindView(R.id.row_lista_compra_iv_agregar) ImageView agregar;
        @BindView(R.id.row_lista_compra_tv_unidad) TextView cantidad;
        @BindView(R.id.row_lista_compra_iv_remover) ImageView remover;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
