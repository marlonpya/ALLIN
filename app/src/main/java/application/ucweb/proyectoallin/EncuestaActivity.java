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

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import application.ucweb.proyectoallin.adapter.EncuestaAdapter;
import application.ucweb.proyectoallin.adapter.PreguntaAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Encuesta;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.modelparseable.PreguntaSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class EncuestaActivity extends BaseActivity implements IActividad {
    public static final String TAG = EncuestaActivity.class.getSimpleName();
    @BindView(R.id.rrv_lista_encuesta) RecyclerView rvv_lista_encuesta;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.idiv_layout_encuesta_allin)ImageView ivFondoEncuestaA;
    @BindView(R.id.tvDescripcionToolbar) TextView tvTituloEncuestaA;
    private PreguntaAdapter adapter;
    private ProgressDialog pDialog;
    private ArrayList<PreguntaSimple> lista_preguntas = new ArrayList<>();
    private int encuestaId=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        iniciaLayout();
        iniciarPDialog();
        encuestaId=getIntent().getIntExtra(Constantes.ID_ENCUESTA, -1);
        if (encuestaId!=-1){
            requestPreguntas();
        }
    }

    private void cargarRV() {
        adapter = new PreguntaAdapter(this, lista_preguntas);
        rvv_lista_encuesta.setHasFixedSize(true);
        rvv_lista_encuesta.setLayoutManager(new LinearLayoutManager(EncuestaActivity.this));
        rvv_lista_encuesta.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void iniciaLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
        setFondoActivity(this, ivFondoEncuestaA);
        tvTituloEncuestaA.setText("ENCUESTA");
    }

    @OnClick(R.id.btnRespondidoTodo)
    public void respondidoEncuesta() {
        //onBackPressed();
        if (doEnviar()){
            requestRegistrarRespuestas();

        }
        else {
            Toast.makeText(this, getString(R.string.por_favor_responda_todo), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean doEnviar() {
        boolean enviar = true;
        for (int i = 0; i < adapter.getArray().size(); i++) {
            if (!adapter.getArray().get(i).isRespondio())
            enviar=false;
        }
        return enviar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void requestRegistrarRespuestas() {
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.REGISTRAR_RESPUESTA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                new AlertDialog.Builder(EncuestaActivity.this)
                                    .setTitle(R.string.app_name)
                                    .setMessage(getString(R.string.encuesta_envio_exito))
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
                                new AlertDialog.Builder(EncuestaActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(getString(R.string.encuesta_envio_error))
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
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usu_id", String.valueOf(Usuario.getUsuario().getId_server()));
                params.put("enc_id", String.valueOf(encuestaId));
                for (int i = 0; i < adapter.getArray().size(); i++) {
                    params.put("res_"+(i+1), String.valueOf(adapter.getArray().get(i).getRespuesta()));
                }
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    private void requestPreguntas() {
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.PREGUNTA_X_ENCUESTA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                JSONArray jPreguntas = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jPreguntas.length(); i++) {

                                    PreguntaSimple pregunta = new PreguntaSimple();
                                    pregunta.setIdServer(jPreguntas.getJSONObject(i).getInt("PRE_ID"));
                                    pregunta.setIdEncuesta(jPreguntas.getJSONObject(i).getInt("ENC_ID"));
                                    pregunta.setPregunta(jPreguntas.getJSONObject(i).getString("PRE_PREGUNTA"));
                                    pregunta.setRespondio(false);
                                    lista_preguntas.add(pregunta);
                                }
                                Log.d(TAG, lista_preguntas.toString());
                                cargarRV();
                            }else {
                                new AlertDialog.Builder(EncuestaActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(getString(R.string.preguntas_not_found))
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
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(encuestaId));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    @Override
    public boolean isSesion() {
        return false;
    }

    @Override
    public void iniciarLayout() {

    }

    @Override
    public void iniciarPDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.enviando_peticion));
    }
}
