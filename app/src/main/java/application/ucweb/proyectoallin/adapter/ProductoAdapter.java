package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import application.ucweb.proyectoallin.CartaEstablecimientoActivity;
import application.ucweb.proyectoallin.DetalleCompraActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Producto;
import application.ucweb.proyectoallin.modelparseable.EstablecimientoSimple;
import application.ucweb.proyectoallin.modelparseable.EventoSimple;
import application.ucweb.proyectoallin.modelparseable.ProductoSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 03/02/2017.
 */

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder>{

    private Context context;
    private ArrayList<ProductoSimple> productos;
    private LayoutInflater inflater;
    private TextView textView;
    private EstablecimientoSimple local;
    private EventoSimple evento;

    public ProductoAdapter(Context context, ArrayList<ProductoSimple> productos, TextView textView, EstablecimientoSimple local){
        this.context = context;
        this.productos = productos;
        this.inflater = LayoutInflater.from(context);
        this.textView = textView;
        this.local = local;
    }

    public ProductoAdapter(Context context, ArrayList<ProductoSimple> productos, TextView textView, EventoSimple evento){
        this.context = context;
        this.productos = productos;
        this.inflater = LayoutInflater.from(context);
        this.textView = textView;
        this.evento = evento;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_cajear_puntos, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ProductoSimple item = productos.get(position);
        holder.nombre_promocion.setText(item.getNombre());
        holder.precio_normal.setText("S/. "+ String.format("%.2f",(item.getPrecio_normal())));
        holder.precio_allin.setText("S/. "+ String.format("%.2f",(item.getPrecio_allin())));
        Glide.with(context).load(item.getImagen()).into(holder.iv_row_imagen_canje);
        BaseActivity.usarGlide(context, R.drawable.iv_row_ic_condicion, holder.iv_row_ic_condiciones);

        BaseActivity.usarGlide(context, R.drawable.iv_row_ic_agregar, holder.iv_row_ic_agregar_a_carrito);
        BaseActivity.usarGlide(context, R.drawable.iv_row_ic_canjear, holder.iv_row_ic_canjear);
        textView.setText(String.valueOf(CartaEstablecimientoActivity.lista_carrito.size()));

        holder.agregarEliminar.setText(context.getString(R.string.row_agregar));
        int idIcon = R.drawable.iv_row_ic_agregar;
        for (int j = 0; j < CartaEstablecimientoActivity.lista_carrito.size(); j++) {
            if (CartaEstablecimientoActivity.lista_carrito.get(j).getIdServer()==item.getIdServer()){
                idIcon=R.drawable.icono_remover;
                holder.agregarEliminar.setText(context.getString(R.string.row_remover));
            }
        }
        BaseActivity.usarGlide(context, idIcon, holder.iv_row_ic_agregar_a_carrito);


        holder.iv_row_ic_condiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.condiciones))
                        .setMessage(item.getCondiciones())
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
        holder.iv_row_ic_agregar_a_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CartaEstablecimientoActivity.lista_carrito.size()==0){
                    item.setCantidad(1);
                    CartaEstablecimientoActivity.lista_carrito.add(item);
                    BaseActivity.usarGlide(context, R.drawable.icono_remover, holder.iv_row_ic_agregar_a_carrito);
                    holder.agregarEliminar.setText(context.getString(R.string.row_remover));
                    Toast.makeText(context, "Se agregó " + item.getNombre(), Toast.LENGTH_SHORT).show();
                    textView.setText(String.valueOf(CartaEstablecimientoActivity.lista_carrito.size()));
                }
                else {
                    boolean existe=false;
                    for (int i = 0; i < CartaEstablecimientoActivity.lista_carrito.size(); i++) {
                        if (CartaEstablecimientoActivity.lista_carrito.get(i).getIdServer()==item.getIdServer()){
                            CartaEstablecimientoActivity.lista_carrito.remove(i);
                            BaseActivity.usarGlide(context, R.drawable.iv_row_ic_agregar, holder.iv_row_ic_agregar_a_carrito);
                            holder.agregarEliminar.setText(context.getString(R.string.row_agregar));
                            Toast.makeText(context, "Se removió " + item.getNombre(), Toast.LENGTH_SHORT).show();
                            textView.setText(String.valueOf(CartaEstablecimientoActivity.lista_carrito.size()));
                            existe=true;
                        }
                    }
                    if (!existe){
                        item.setCantidad(1);
                        CartaEstablecimientoActivity.lista_carrito.add(item);
                        textView.setText(String.valueOf(CartaEstablecimientoActivity.lista_carrito.size()));
                        BaseActivity.usarGlide(context, R.drawable.icono_remover, holder.iv_row_ic_agregar_a_carrito);
                        holder.agregarEliminar.setText(context.getString(R.string.row_remover));
                        Toast.makeText(context, "Se agregó " + item.getNombre(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        holder.iv_row_ic_canjear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getContext().startActivity(new Intent(getContext().getApplicationContext(), CanjearPuntosActivity.class));
                //Toast.makeText(context, "Canjear", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.canjear_ahora))
                        .setMessage("Desea canjear " + item.getNombre() + "?")
                        .setIcon(R.drawable.iconoalertafucsia)
                        .setNegativeButton(context.getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(context.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //requestMisPuntos(item);
                                item.setCantidad(1);
                                ArrayList<ProductoSimple> singleItemCart = new ArrayList<>();
                                singleItemCart.add(item);
                                if (local!=null){
                                    context.startActivity(new Intent(context, DetalleCompraActivity.class)
                                            .putExtra(Constantes.ARRAY_S_CARRITO, singleItemCart)
                                            .putExtra(Constantes.OBJ_S_ESTABLECIMIENTO, local));
                                }else if (evento!=null){
                                    context.startActivity(new Intent(context, DetalleCompraActivity.class)
                                            .putExtra(Constantes.ARRAY_S_CARRITO, singleItemCart)
                                            .putExtra(Constantes.OBJ_S_EVENTO, evento));
                                }
                                dialog.dismiss();
                            }
                        })
                        .show();
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
        @BindView(R.id.tvAgregarEliminar)TextView agregarEliminar;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
