package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.fragment.MisPuntosFragment;
import application.ucweb.proyectoallin.modelparseable.ItemCarrito;
import application.ucweb.proyectoallin.modelparseable.ProductoSimple;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 06/02/2017.
 */

public class CartaEstablecimientoAdapter extends RecyclerView.Adapter<CartaEstablecimientoAdapter.ViewHolder>{

    private Context context;
    private ArrayList<ProductoSimple> productos;
    private LayoutInflater inflater;
    private TextView textView;

    //private ArrayList<ItemCarrito> carrito = new ArrayList<>();

    public CartaEstablecimientoAdapter(Context context, ArrayList<ProductoSimple> productos, TextView textView){
        this.context = context;
        this.productos = productos;
        this.inflater = LayoutInflater.from(context);
        this.textView = textView;
    }
    @Override
    public CartaEstablecimientoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_promocion, parent, false));
    }

    @Override
    public void onBindViewHolder(final CartaEstablecimientoAdapter.ViewHolder holder, int position) {
        final ProductoSimple item = productos.get(position);

        holder.promocion.setText(item.getNombre());
        holder.puntos_consumo.setText(item.getPrecio_puntos() + "pts Consumo");
        Glide.with(context).load(item.getImagen()).into(holder.imagen);
        //BaseActivity.usarGlide(getContext(), item.getImagen_producto(), viewHolder.imagen);
        BaseActivity.usarGlide(context, R.drawable.iv_row_ic_condicion, holder.icono_condiciones);
        holder.agregarEliminar.setText(context.getString(R.string.row_agregar));
        int idIcon = R.drawable.iv_row_ic_agregar;
        for (int j = 0; j < MisPuntosFragment.lista_carrito.size(); j++) {
            if (MisPuntosFragment.lista_carrito.get(j).getIdServer()==item.getIdServer()){
                idIcon=R.drawable.icono_remover;
                holder.agregarEliminar.setText(context.getString(R.string.row_remover));
            }
        }
        BaseActivity.usarGlide(context, idIcon, holder.icono_agregar);

        BaseActivity.usarGlide(context, R.drawable.iv_row_ic_canjear, holder.icono_canjear);

        holder.icono_condiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.condiciones))
                        .setMessage("producto.getCondiciones()")
                        .setIcon(R.drawable.iconoalertafucsia)
                        .setPositiveButton(context.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        holder.icono_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MisPuntosFragment.lista_carrito.size()==0){
                    ItemCarrito itemCarrito = new ItemCarrito();
                    itemCarrito.setIdServer(item.getIdServer());
                    itemCarrito.setNombre(item.getNombre());
                    itemCarrito.setPrecio_normal(item.getPrecio_normal());
                    itemCarrito.setPrecio_allin(item.getPrecio_allin());
                    itemCarrito.setPrecio_puntos(item.getPrecio_puntos());
                    itemCarrito.setIdLocal(item.getIdLocal());
                    itemCarrito.setIdEvento(item.getIdEvento());
                    //itemCarrito.setCantidad(1);
                    MisPuntosFragment.lista_carrito.add(itemCarrito);
                    BaseActivity.usarGlide(context, R.drawable.icono_remover, holder.icono_agregar);
                    holder.agregarEliminar.setText(context.getString(R.string.row_remover));
                    Toast.makeText(context, "Se agregó " + item.getNombre(), Toast.LENGTH_SHORT).show();
                    textView.setText(String.valueOf(MisPuntosFragment.lista_carrito.size()));
                }
                else {
                    boolean existe=false;
                    for (int i = 0; i < MisPuntosFragment.lista_carrito.size(); i++) {
                        if (MisPuntosFragment.lista_carrito.get(i).getIdServer()==item.getIdServer()){
                            MisPuntosFragment.lista_carrito.remove(i);
                            BaseActivity.usarGlide(context, R.drawable.iv_row_ic_agregar, holder.icono_agregar);
                            holder.agregarEliminar.setText(context.getString(R.string.row_agregar));
                            Toast.makeText(context, "Se removió " + item.getNombre(), Toast.LENGTH_SHORT).show();
                            textView.setText(String.valueOf(MisPuntosFragment.lista_carrito.size()));
                            existe=true;
                        }
                    }
                    if (!existe){
                        ItemCarrito itemCarrito = new ItemCarrito();
                        itemCarrito.setIdServer(item.getIdServer());
                        itemCarrito.setNombre(item.getNombre());
                        itemCarrito.setPrecio_normal(item.getPrecio_normal());
                        itemCarrito.setPrecio_allin(item.getPrecio_allin());
                        itemCarrito.setPrecio_puntos(item.getPrecio_puntos());
                        itemCarrito.setIdLocal(item.getIdLocal());
                        itemCarrito.setIdEvento(item.getIdEvento());
                        //itemCarrito.setCantidad(1);
                        MisPuntosFragment.lista_carrito.add(itemCarrito);
                        textView.setText(String.valueOf(MisPuntosFragment.lista_carrito.size()));
                        BaseActivity.usarGlide(context, R.drawable.icono_remover, holder.icono_agregar);
                        holder.agregarEliminar.setText(context.getString(R.string.row_remover));
                        Toast.makeText(context, "Se agregó " + item.getNombre(), Toast.LENGTH_SHORT).show();
                    }
                }
                Log.v("Amd", MisPuntosFragment.lista_carrito.size()+"");
            }
        });
        holder.icono_canjear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Canjear", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_tv_promocion) TextView promocion;
        @BindView(R.id.row_tv_puntos_consumo)TextView puntos_consumo;
        @BindView(R.id.iv_row_imagen_canje)ImageView imagen;
        @BindView(R.id.iv_row_ic_condiciones)ImageView icono_condiciones;
        @BindView(R.id.iv_row_ic_agregar_a_carrito)ImageView icono_agregar;
        @BindView(R.id.iv_row_ic_canjear)ImageView icono_canjear;
        @BindView(R.id.tvAgregarEliminar)TextView agregarEliminar;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
