package application.ucweb.proyectoallin.adapter;

import android.app.ProgressDialog;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import application.ucweb.proyectoallin.ListaCanjeActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.fragment.MisPuntosFragment;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.modelparseable.ProductoSimple;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 06/02/2017.
 */

public class CartaEstablecimientoAdapter extends RecyclerView.Adapter<CartaEstablecimientoAdapter.ViewHolder>{
    public static final String TAG = CartaEstablecimientoAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<ProductoSimple> productos;
    private LayoutInflater inflater;
    private TextView textView;
    private ProgressDialog pDialog;
    private MisPuntosFragment fragment;

    //private ArrayList<ItemCarrito> carrito = new ArrayList<>();

    public CartaEstablecimientoAdapter(Context context, ArrayList<ProductoSimple> productos, TextView textView, MisPuntosFragment fragment){
        this.context = context;
        this.productos = productos;
        this.inflater = LayoutInflater.from(context);
        this.textView = textView;
        this.fragment = fragment;
    }
    @Override
    public CartaEstablecimientoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_promocion, parent, false));
    }

    @Override
    public void onBindViewHolder(final CartaEstablecimientoAdapter.ViewHolder holder, int position) {

        pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getString(R.string.enviando_peticion));
        pDialog.setCancelable(false);

        final ProductoSimple item = productos.get(position);

        holder.promocion.setText(item.getNombre());
        holder.puntos_consumo.setText(item.getPrecio_puntos() + "pts Consumo");
        Glide.with(context).load(item.getImagen()).into(holder.imagen);
        //BaseActivity.usarGlide(getContext(), item.getImagen_producto(), viewHolder.imagen);
        BaseActivity.usarGlide(context, R.drawable.iv_row_ic_condicion, holder.icono_condiciones);

        textView.setText(String.valueOf(MisPuntosFragment.lista_carrito.size()));

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

        holder.icono_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MisPuntosFragment.lista_carrito.size()==0){
                    MisPuntosFragment.lista_carrito.add(item);
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
                        MisPuntosFragment.lista_carrito.add(item);
                        textView.setText(String.valueOf(MisPuntosFragment.lista_carrito.size()));
                        BaseActivity.usarGlide(context, R.drawable.icono_remover, holder.icono_agregar);
                        holder.agregarEliminar.setText(context.getString(R.string.row_remover));
                        Toast.makeText(context, "Se agregó " + item.getNombre(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        holder.icono_canjear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                requestMisPuntos(item);
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

    private Map<String, String> crearDetalle(ProductoSimple item) throws JSONException {
        final String data = "detalle";
        final String cantidad = "cantidad";
        final String precio = "precio";
        final String tipo = "tipo";
        final String idProd = "pro_id";
        final String estado = "estado";
        final String ubicacion = "ubicacion";
        final String idlocal = "idlocal";

        Map<String, String> params = new HashMap<>();
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(cantidad, String.valueOf(1));
        jsonObject.put(precio, String.valueOf(item.getPrecio_puntos()));
        jsonObject.put(tipo, String.valueOf(item.getTipo()));
        jsonObject.put(idProd, String.valueOf(item.getIdServer()));
        jsonObject.put(estado, String.valueOf(0));
        if (item.getIdEvento()!=0){
            jsonObject.put(ubicacion, Constantes.PRODUCTO_DE_EVENTO);
            jsonObject.put(idlocal, item.getIdEvento());
        }else if (item.getIdLocal()!=0){
            jsonObject.put(ubicacion, Constantes.PRODUCTO_DE_LOCAL);
        }
        jsonArray.put(jsonObject);
        params.put(data, jsonArray.toString());
        Log.d(TAG, "params_" + params.toString());

        JSONObject jObj = new JSONObject();
        jObj.put("usu_id", Usuario.getUsuario().getId_server());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "pe"));
        Calendar c = Calendar.getInstance();
        final String fecha  = sdf.format(c.getTime());
        final String fechaVenta = sdf.format(c.getTime());
        final String fechaVencimiento = sdf.format(c.getTime());
        jObj.put("nombre", Usuario.getUsuario().getNombre());
        //jObj.put("fecha", fecha);
        //jObj.put("fecha_venta", fechaVenta);
        //jObj.put("fecha_vencimiento", fechaVencimiento);
        jsonArray.put(jObj);
        params.put("venta", jObj.toString());
        return params;
    }

    private void crearVenta(final ProductoSimple item) throws JSONException {
        //showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.REGISTRAR_CANJE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getInt("estadoventa")==1) {
                                BaseActivity.hidepDialog(pDialog);
                                fragment.clearLists();
                                fragment.requestMisPuntos();
                                new AlertDialog.Builder(context)
                                        .setTitle(R.string.app_name)
                                        .setMessage(context.getString(R.string.canje_exito))
                                        .setCancelable(false)
                                        .setPositiveButton(context.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }else {
                                BaseActivity.hidepDialog(pDialog);
                                new AlertDialog.Builder(context)
                                        .setTitle(R.string.app_name)
                                        .setMessage(R.string.item_no_stock)
                                        .setCancelable(false)
                                        .setPositiveButton(context.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                //onBackPressed();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            BaseActivity.hidepDialog(pDialog);
                            e.printStackTrace();
                        }
                        Log.d(TAG, "json_"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        BaseActivity.hidepDialog(pDialog);
                        if (error != null) { VolleyLog.d(error.toString()); }
                        new AlertDialog.Builder(context)
                                .setTitle("Error de Conexión")
                                .setMessage("Hubo un error y no se pudo procesar la solicitud.Por favor, inténtelo de nuevo.")
                                .setPositiveButton("OK", null)
                                .create().show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                try {
                    params = crearDetalle(item);
                    Log.d(TAG, "DATA-ENVIADA_" + params.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    private void requestMisPuntos(final ProductoSimple item) {
        BaseActivity.showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.MIS_PUNTOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("puntos")>=item.getPrecio_puntos()){
                                //requestRegistrarCanje();
                                crearVenta(item);
                            }
                            else {
                                BaseActivity.hidepDialog(pDialog);
                                Toast.makeText(context, R.string.no_suficiente_puntos, Toast.LENGTH_SHORT).show();
                            }
                            Log.d(TAG, jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString(), e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        BaseActivity.hidepDialog(pDialog);
                        VolleyLog.e(error.toString(), error);
                        new AlertDialog.Builder(context)
                                .setTitle("Error de Conexión")
                                .setMessage("Hubo un error y no se pudo procesar la solicitud.Por favor, inténtelo de nuevo.")
                                .setPositiveButton("OK", null)
                                .create().show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(Usuario.getUsuario().getId_server()));
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

}
