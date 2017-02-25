package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import application.ucweb.proyectoallin.adapter.ListaClientesCorporativoDetalleAdapter;
import application.ucweb.proyectoallin.adapter.ListaClientesCorporativoDetalleRAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Cliente;
import application.ucweb.proyectoallin.model.Corporativo;
import application.ucweb.proyectoallin.model.EnLista;
import application.ucweb.proyectoallin.modelparseable.UsuarioSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

import static application.ucweb.proyectoallin.aplicacion.BaseActivity.setToolbarSon;

public class ListaClientesCorporativoDetalleActivity extends BaseActivity implements IActividad{
    public static final String TAG = ListaClientesCorporativoDetalleActivity.class.getSimpleName();
    @BindView(R.id.fondo_lista_clientes_corporativo_detalle) ImageView fondo;
    @BindView(R.id.rrv_clientes_corporativo_detalle) RecyclerView recyclerView;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    private ListaClientesCorporativoDetalleAdapter adapter;
    private ArrayList<UsuarioSimple> usuarios = new ArrayList<>();
    private String fecha;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes_corporativo_detalle);
        iniciarLayout();
        iniciarPDialog();
        if (getIntent().hasExtra(Constantes.L_ID_ENLISTA)) {
            fecha = getIntent().getStringExtra(Constantes.L_ID_ENLISTA);
            getIntent().removeExtra(Constantes.L_ID_ENLISTA);
            Log.v("Amd", "Local");
            requestUsuariosLocal();
        } else {
            //System.err.print("no recibió id");
            Log.v("Amd", "Evento");
            requestUsuariosEvento();
        }
    }

    private void iniciarRV() {
        adapter = new ListaClientesCorporativoDetalleAdapter(this, usuarios);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void requestUsuariosEvento() {
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.USUARIOS_X_EVENTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getBoolean("status")) {
                                JSONArray jArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jArray.length(); i++) {
                                    UsuarioSimple usuario = new UsuarioSimple();
                                    usuario.setIdServer(jArray.getJSONObject(i).getInt("USU_ID"));
                                    usuario.setNombre(jArray.getJSONObject(i).getString("USU_NOMBRE"));
                                    usuario.setApellido(jArray.getJSONObject(i).getString("USU_APE_PATERNO"));
                                    usuario.setDni(jArray.getJSONObject(i).getInt("USU_DNI_CARNET"));
                                    usuarios.add(usuario);
                                }
                                iniciarRV();
                            }else {
                                new AlertDialog.Builder(ListaClientesCorporativoDetalleActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(getString(R.string.usuarios_not_found))
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
                        errorConexion(ListaClientesCorporativoDetalleActivity.this);
                        hidepDialog(pDialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(Corporativo.getCorporativo().getId_evento()));
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    private void requestUsuariosLocal() {
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.USUARIOS_X_FECHA_LOCAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                JSONArray jArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jArray.length(); i++) {
                                    UsuarioSimple usuario = new UsuarioSimple();
                                    usuario.setIdServer(jArray.getJSONObject(i).getInt("USU_ID"));
                                    usuario.setNombre(jArray.getJSONObject(i).getString("USU_NOMBRE"));
                                    usuario.setApellido(jArray.getJSONObject(i).getString("USU_APE_PATERNO"));
                                    usuario.setDni(jArray.getJSONObject(i).getInt("USU_DNI_CARNET"));
                                    usuarios.add(usuario);
                                }
                                iniciarRV();
                            }else {
                                new AlertDialog.Builder(ListaClientesCorporativoDetalleActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(getString(R.string.usuarios_not_found))
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
                        errorConexion(ListaClientesCorporativoDetalleActivity.this);
                        hidepDialog(pDialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fecha", fecha);
                params.put("id", String.valueOf(Corporativo.getCorporativo().getId_local()));
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
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
