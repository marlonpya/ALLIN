package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoallin.adapter.ListaCanjeCorporativoAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Corporativo;
import application.ucweb.proyectoallin.model.ItemSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.OnClick;

public class ListaCanjeCorporativo extends BaseActivity implements IActividad {
    public static final String TAG =  ListaClientesCorporativoActivity.class.getSimpleName();

    @BindView(R.id.fondo_lista_canje_corporativo_detalle) ImageView fondo;
    @BindView(R.id.rv_canje_corporativo_detalle) RecyclerView recyclerView;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    private ProgressDialog pDialog;
    private ArrayList<ItemSimple> detalles = new ArrayList<>();
    private ListaCanjeCorporativoAdapter adapter;
    private String codigo;
    private String dni;
    public static ArrayList<Integer> id_detalle_a_canjear = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_canje_corporativo);
        iniciarLayout();
        if (getIntent().hasExtra(Constantes.CODIGO) && getIntent().hasExtra(Constantes.DNI)){
            codigo=getIntent().getStringExtra(Constantes.CODIGO);
            dni = getIntent().getStringExtra(Constantes.DNI);
            getIntent().removeExtra(Constantes.CODIGO);
            getIntent().removeExtra(Constantes.DNI);
            requestDetalleVentaLocal();
        }
    }

    @OnClick(R.id.btnTest)
    public void test(){
        try {
            crearDetalle();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void iniciarRV(){
        adapter = new ListaCanjeCorporativoAdapter(this, detalles);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void requestDetalleVentaLocal() {
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.DETALLE_VENTA_LOCAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")){
                                JSONArray jArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jArray.length(); i++) {
                                    ItemSimple detalle = new ItemSimple();
                                    detalle.setId(jArray.getJSONObject(i).getInt("VENPRO_ID"));
                                    detalle.setTitulo(jArray.getJSONObject(i).getString("PRO_NOMBRE"));
                                    detalle.setTipo(jArray.getJSONObject(i).getInt("VENPRO_ESTADO"));
                                    detalles.add(detalle);
                                }
                                iniciarRV();
                                /*startActivity(new Intent(MenuCorporativo.this, ListaCanjeCorporativo.class)
                                        .putExtra(Constantes.ARRAY_S_DETALLE_CANJE_CORPORATIVO, detalles));*/
                            }else{
                                new AlertDialog.Builder(ListaCanjeCorporativo.this)
                                        .setTitle(getString(R.string.app_name))
                                        .setMessage(jsonObject.getString("message"))
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

                            Log.d(TAG, jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString(), e);
                        }
                        hidepDialog(pDialog);
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
                params.put("codigo", codigo.toUpperCase().trim());
                params.put("dni", String.valueOf(dni).trim());
                params.put("loc_id", String.valueOf(Corporativo.getCorporativo().getId_local()));
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    private Map<String, String> crearDetalle() throws JSONException {
        final String data = "data";
        final String idDetalle = "detalle";

        Map<String, String> params = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < id_detalle_a_canjear.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(idDetalle, String.valueOf(id_detalle_a_canjear.get(i)));
            jsonArray.put(jsonObject);
            params.put(data, jsonArray.toString());
            Log.d(TAG, "size/a_" + String.valueOf(id_detalle_a_canjear.size()));
            Log.d(TAG, "params_" + params.toString());
        }
        return params;
    }

    @Override
    public boolean isSesion() {
        return Corporativo.getCorporativo().isSesion();
    }

    @Override
    public void iniciarLayout() {
        setToolbarSon(toolbar, this);
        setFondoActivity(this, fondo);
    }

    @Override
    public void iniciarPDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.enviando_peticion));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
