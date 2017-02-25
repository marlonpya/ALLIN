package application.ucweb.proyectoallin.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.Locale;
import java.util.Map;

import application.ucweb.proyectoallin.CartaEstablecimientoActivity;
import application.ucweb.proyectoallin.EncuestaActivity;
import application.ucweb.proyectoallin.ListaCanjeActivity;
import application.ucweb.proyectoallin.ListaCompraActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.adapter.CartaEstablecimientoAdapter;
import application.ucweb.proyectoallin.adapter.CartaEstablecimientoRealmAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Producto;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.modelparseable.ProductoSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class MisPuntosFragment extends Fragment implements IActividad {
    public static final String TAG = MisPuntosFragment.class.getSimpleName();
    @BindView(R.id.lista_puntos) RecyclerView rvlista_puntos;
    @BindView(R.id.spCarta)Spinner spCarta;
    @BindView(R.id.tvResultadoCarta)TextView tvResultadoCarta;
    @BindView(R.id.tvDescripcionToolbar)TextView tvTituloCanjearPF;
    @BindView(R.id.tv_cantidad_productos_carrito) TextView cantidad_productos_carrito;
    @BindView(R.id.tvPuntos) TextView tvPuntos;
    @BindString(R.string.dialogo_antes_de_canjear) String texto_canje;
    private ProgressDialog pDialog;
    private CartaEstablecimientoAdapter adapter;
    private ArrayList<ProductoSimple> lista_productos = new ArrayList<>();
    private ArrayList<ProductoSimple> lista_filtrada = new ArrayList<>();
    //public static ArrayList<ItemCarrito> lista_carrito = new ArrayList<>();
    public static ArrayList<ProductoSimple> lista_carrito = new ArrayList<>();
    public static boolean refrescarVista=false;

    public MisPuntosFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_puntos, container, false);
        ButterKnife.bind(this, view);
        cantidad_productos_carrito.setText(String.valueOf(0));
        iniciarLayout();
        iniciarPDialog();
        requestMisPuntos();
        //dialogoAntesCanjear();
        return view;
    }

    @OnClick(R.id.iv_ir_a_carrito_puntos)
    public void irACarritoPuntos() {
        if (lista_carrito.size()>0) {
            startActivity(new Intent(getActivity().getApplicationContext(), ListaCanjeActivity.class).putExtra(Constantes.ARRAY_S_CARRITO, lista_carrito));
        }
        else {
            Toast.makeText(getActivity(), getString(R.string.carrito_vacio), Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogoAntesCanjear() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.titulo_antes_canjear)
                .setMessage(R.string.dialogo_antes_de_canjear)
                .setCancelable(true)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity().getApplicationContext(), EncuestaActivity.class));
                    }
                })
                .show();
    }

    private void iniciarRV() {
        adapter = new CartaEstablecimientoAdapter(getActivity(), lista_filtrada, cantidad_productos_carrito, MisPuntosFragment.this);
        int cantidad_encontrados = lista_filtrada.size();
        tvResultadoCarta.setText(String.valueOf(cantidad_encontrados + " encontrados"));
        rvlista_puntos.setHasFixedSize(true);
        rvlista_puntos.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvlista_puntos.setAdapter(adapter);
        //int cantidad_carrito = realm.where(Producto.class).equalTo(Producto.A_CARRITO, true).findAll().size();
        //cantidad_productos_carrito.setText(String.valueOf(cantidad_carrito));
        adapter.notifyDataSetChanged();
    }

    @OnItemSelected(R.id.spCarta)
    public void spCartaSeeleccion(int position) {
        int tipo=position-1;
        if (tipo==-1){
            lista_filtrada.clear();
            lista_filtrada.addAll(lista_productos);
        }
        else {
            lista_filtrada.clear();
            for (int i = 0; i < lista_productos.size(); i++) {
                if (lista_productos.get(i).getTipo()==tipo){
                    lista_filtrada.add(lista_productos.get(i));
                }
            }
        }
        iniciarRV();
    }

    @Override
    public boolean isSesion() {
        return Usuario.getUsuario() != null && Usuario.getUsuario().isSesion();
    }

    @Override
    public void iniciarLayout() {
        tvTituloCanjearPF.setText("MIS PUNTOS ALLIN");
        BaseActivity.setSpinnerRosa(getContext(), spCarta, R.array.arrayCarta);
    }

    private void requestPromociones() {
        //BaseActivity.showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.PROMOCIONES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                JSONArray jEventos = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jEventos.length(); i++) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "pe"));
                                    ProductoSimple producto = new ProductoSimple();
                                    producto.setIdServer(jEventos.getJSONObject(i).getInt("PRO_ID"));
                                    producto.setNombre(jEventos.getJSONObject(i).getString("PRO_NOMBRE"));
                                    //producto.setPrecio_normal(Double.parseDouble(jEventos.getJSONObject(i).getString("PRO_PRECIO_NORMAL")));
                                    //producto.setPrecio_allin(Double.parseDouble(jEventos.getJSONObject(i).getString("PRO_PRECIO_ALLIN")));
                                    producto.setPrecio_puntos(jEventos.getJSONObject(i).getInt("PRO_PRECIO_PUNTOS"));
                                    producto.setStock(jEventos.getJSONObject(i).getInt("PRO_STOCK"));
                                    producto.setIdLocal(jEventos.getJSONObject(i).getInt("LOC_ID"));
                                    producto.setIdEvento(jEventos.getJSONObject(i).getInt("EVE_ID"));
                                    producto.setPromocion(jEventos.getJSONObject(i).getString("PRO_PROMOCION"));
                                    producto.setNombre_local(jEventos.getJSONObject(i).getString("LOC_NOMBRE"));
                                    producto.setNombre_evento(jEventos.getJSONObject(i).getString("EVE_NOMBRE"));
                                    if (!jEventos.getJSONObject(i).getString("PRO_CONDICIONES").trim().equals("null") && !jEventos.getJSONObject(i).getString("PRO_CONDICIONES").trim().isEmpty()){
                                        producto.setCondiciones(jEventos.getJSONObject(i).getString("PRO_CONDICIONES"));
                                    }else {
                                        producto.setCondiciones("No hay condiciones");
                                    }
                                    try {
                                        producto.setFecha_inicio(sdf.parse(jEventos.getJSONObject(i).getString("PRO_FEC_INICIO")));
                                        producto.setFecha_fin(sdf.parse(jEventos.getJSONObject(i).getString("PRO_FEC_FIN")));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    producto.setImagen(jEventos.getJSONObject(i).getString("PRO_IMAGEN"));
                                    producto.setTipo(jEventos.getJSONObject(i).getInt("PRO_TIPO"));
                                    producto.setEstado(jEventos.getJSONObject(i).getInt("PRO_ESTADO")==1);
                                    lista_productos.add(producto);
                                }
                                Log.d(TAG, lista_productos.toString());
                                //iniciarRV();
                                spCartaSeeleccion(0);
                            }else {
                                spCartaSeeleccion(0);
                                new AlertDialog.Builder(getContext())
                                        .setTitle(R.string.app_name)
                                        .setMessage(getString(R.string.productos_not_found))
                                        .setCancelable(false)
                                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                //getActivity().onBackPressed();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString(), e);
                        }
                        BaseActivity.hidepDialog(pDialog);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(error.toString(), error);
                        BaseActivity.errorConexion(getContext());
                        BaseActivity.hidepDialog(pDialog);
                    }
                }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    public void requestMisPuntos() {
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
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            Usuario.getUsuario().setPuntos(jsonObject.getInt("puntos"));
                            realm.commitTransaction();
                            tvPuntos.setText(getString(R.string.puntos_consumo) + " " + Usuario.getUsuario().getPuntos());
                            requestPromociones();
                            Log.d(TAG, jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString(), e);
                        }
                        //BaseActivity.hidepDialog(pDialog);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(error.toString(), error);
                        BaseActivity.hidepDialog(pDialog);
                        BaseActivity.errorConexion(getContext());
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

    @Override
    public void iniciarPDialog() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.enviando_peticion));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destroy!");
        clearLists();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refrescarVista){
            clearLists();
            requestMisPuntos();
            refrescarVista = false;
        }
    }

    public void clearLists(){
        lista_productos.clear();
        lista_filtrada.clear();
        lista_carrito.clear();
    }
}
