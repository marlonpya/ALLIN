package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import application.ucweb.proyectoallin.adapter.MisPuntosCanjeAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.fragment.MisPuntosFragment;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Producto;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.modelparseable.ProductoSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;

public class ListaCanjeActivity extends BaseActivity implements IActividad{
    public static final String TAG = ListaCanjeActivity.class.getSimpleName();
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.rv_lista_promociones) RecyclerView recyclerView;
    @BindView(R.id.tv_total_canje) TextView total_compra;
    @BindView(R.id.idiv_layout_canje)ImageView idiv_layout_canje;
    @BindView(R.id.tvPuntos) TextView tvPuntos;
    private ProgressDialog pDialog;
    private int idVenta=-1;
    private int counter = -1;

    private MisPuntosCanjeAdapter adapter;
    public static double valor_compra = 0.0;
    ArrayList<ProductoSimple> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_canje);
        iniciarLayout();
        iniciarPDialog();
        tvPuntos.setText("PUNTOS DISPONIBLES: " + Usuario.getUsuario().getPuntos());
        items=(ArrayList<ProductoSimple>)getIntent().getSerializableExtra(Constantes.ARRAY_S_CARRITO);
        counter=items.size()-1;
        iniciarRRV();
    }

    private void iniciarRRV() {
        total_compra.setText(String.valueOf(valor_compra));
        adapter = new MisPuntosCanjeAdapter(this, items, total_compra);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isSesion() {
        return Usuario.getUsuario() != null && Usuario.getUsuario().isSesion();
    }

    @Override
    public void iniciarLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
        setFondoActivity(this, idiv_layout_canje);
    }

    @Override
    public void iniciarPDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.enviando_peticion));
        pDialog.setCancelable(false);
    }

    private void requestMisPuntos() {
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.MIS_PUNTOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("puntos")>=adapter.getTotal()){
                                //requestRegistrarCanje();
                                crearVenta();
                            }
                            else {
                                hidepDialog(pDialog);
                                Toast.makeText(ListaCanjeActivity.this, "Ud. no tiene suficientes puntos", Toast.LENGTH_SHORT).show();
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
                        VolleyLog.e(error.toString(), error);
                        hidepDialog(pDialog);
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

    @OnClick(R.id.btnCanjear)
    public void canjearPuntos(){
        if (items.size()>0){
            new AlertDialog.Builder(ListaCanjeActivity.this)
                    .setTitle(R.string.app_name)
                    .setMessage(getString(R.string.canje_preguntar))
                    .setCancelable(false)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            requestMisPuntos();
                        }
                    }).setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
                //crearVenta();
        }
        else Toast.makeText(this, R.string.no_agrego_items, Toast.LENGTH_SHORT).show();
    }

    private Map<String, String> crearDetalle() throws JSONException {
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
        for (int i = 0; i < items.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(cantidad, String.valueOf(1));
            jsonObject.put(precio, String.valueOf(items.get(i).getPrecio_puntos()));
            jsonObject.put(tipo, String.valueOf(items.get(i).getTipo()));
            jsonObject.put(idProd, String.valueOf(items.get(i).getIdServer()));
            jsonObject.put(estado, String.valueOf(0));
            if (items.get(i).getIdEvento()!=0){
                jsonObject.put(ubicacion, Constantes.PRODUCTO_DE_EVENTO);
                jsonObject.put(idlocal, items.get(i).getIdEvento());
            }else if (items.get(i).getIdLocal()!=0){
                jsonObject.put(ubicacion, Constantes.PRODUCTO_DE_LOCAL);
            }
            jsonArray.put(jsonObject);
            params.put(data, jsonArray.toString());
            Log.d(TAG, "size/a_" + String.valueOf(items .size()));
            Log.d(TAG, "params_" + params.toString());
        }
        JSONObject jObj = new JSONObject();
        jObj.put("usu_id", Usuario.getUsuario().getId_server());
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "pe"));
        Calendar c = Calendar.getInstance();*/
        jObj.put("nombre", Usuario.getUsuario().getNombre());
        jsonArray.put(jObj);
        params.put("venta", jObj.toString());
        return params;
    }

    private void crearVenta() throws JSONException {
        //showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.REGISTRAR_CANJE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            //contar si hubieron items sin stock
                            int num=0;
                            if (object.getBoolean("faltostock")){
                                JSONArray jNoStock = object.getJSONArray("nostock");
                                for (int i = 0; i < jNoStock.length(); i++) {
                                    num++;
                                }
                            }
                            if (object.getInt("codigo")==200) {
                                hidepDialog(pDialog);
                                MisPuntosFragment.lista_carrito.clear();
                                MisPuntosFragment.refrescarVista=true;

                                if (num>0){
                                    new AlertDialog.Builder(ListaCanjeActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(num + " de " + items.size() + " item(s) no procesado(s) por falta de stock")
                                        .setCancelable(false)
                                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                onBackPressed();
                                            }
                                        })
                                        .show();
                                }else {
                                    new AlertDialog.Builder(ListaCanjeActivity.this)
                                            .setTitle(R.string.app_name)
                                            .setMessage(getString(R.string.canje_exito))
                                            .setCancelable(false)
                                            .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    onBackPressed();
                                                }
                                            })
                                            .show();
                                }
                            }
                            if (object.getInt("codigo")==-3){
                                new AlertDialog.Builder(ListaCanjeActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage("No se pudo procesar por falta de puntos")
                                        .setCancelable(false)
                                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                onBackPressed();
                                            }
                                        })
                                        .show();
                            }
                            else {
                                hidepDialog(pDialog);
                            }
                        } catch (JSONException e) {
                            hidepDialog(pDialog);
                            e.printStackTrace();
                        }
                        Log.d(TAG, "json_"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog(pDialog);
                        if (error != null) { VolleyLog.d(error.toString()); }
                        new AlertDialog.Builder(ListaCanjeActivity.this)
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
                    params = crearDetalle();
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
}
