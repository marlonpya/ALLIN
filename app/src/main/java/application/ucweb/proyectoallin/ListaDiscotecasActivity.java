package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoallin.adapter.EstablecimientoRealmAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.model.Establecimiento;
import application.ucweb.proyectoallin.model.zona.Distrito;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class ListaDiscotecasActivity extends BaseActivity {
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.lista_eventos)RealmRecyclerView lista_eventos;
    @BindView(R.id.tvDescripcionToolbar)TextView toolbarListadiscoteca;
    @BindView(R.id.idiv_layout_lista_discoteca)ImageView ivFondoListaDiscoteca;
    public static final String TAG = ListaDiscotecasActivity.class.getSimpleName();
        private Realm realm;
    private EstablecimientoRealmAdapter adapter;
    private RealmResults<Establecimiento> listaEventos;
    private ProgressDialog progressDialog;
    private String nombreDistrito;
    private Establecimiento establecimiento;
    private int tipoLocal;
    private int tipoFiltro;
    private int tipoMusica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_discotecas);
        iniciarLayout();
        /*if (Establecimiento.getUltimoId(1) == 0) {
            cargaDataEventosRealm();
        }*/
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.actualizando));
        nombreDistrito=getIntent().getStringExtra(Constantes.K_S_TITULO_TOOLBAR);
        tipoLocal=getIntent().getIntExtra(Constantes.TIPO_ESTABLECIMIENTO, -1);
        tipoFiltro=getIntent().getIntExtra(Constantes.FILTRO, -1);
        tipoMusica=getIntent().getIntExtra(Constantes.GENERO_MUSICA, -1);
        toolbarListadiscoteca.setText(nombreDistrito);
        vaciarLocalesRealm();
        switch (tipoFiltro){
            case Constantes.FILTRO_DISTRITO: requestLocalXCategoria();break;
            case Constantes.FILTRO_MUSICA: requestLocalXGenero();break;
        }
        //requestLocalXCategoria();
    }

    private void cargarRealmListas() {
        realm = Realm.getDefaultInstance();

        switch (tipoFiltro){
            case Constantes.FILTRO_DISTRITO:
                listaEventos = realm.where(Establecimiento.class).equalTo("distrito", nombreDistrito).findAll(); break;
            case Constantes.FILTRO_MUSICA:
                listaEventos = realm.where(Establecimiento.class).findAll(); break;
        }
        for (int i = 0; i < listaEventos.size() ; i++) {
        }
        adapter = new EstablecimientoRealmAdapter(this, listaEventos);
        lista_eventos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void requestLocalXCategoria() {
        showDialog(progressDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.LOCALES_X_CATEGORIA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jArray = jsonObject.getJSONArray("local");
                            Realm realm = Realm.getDefaultInstance();
                            for (int i = 0; i < jArray.length(); i++) {
                                realm.beginTransaction();
                                Establecimiento local = realm.createObject(Establecimiento.class, Establecimiento.getUltimoId());
                                local.setId_server(jArray.getJSONObject(i).getInt("LOC_ID"));
                                local.setNombre(jArray.getJSONObject(i).getString("LOC_NOMBRE"));
                                local.setDireccion(jArray.getJSONObject(i).getString("LOC_DIRECCION"));
                                local.setLatitud(Double.parseDouble(jArray.getJSONObject(i).getString("LOC_LATITUD")));
                                local.setLongitud(Double.parseDouble(jArray.getJSONObject(i).getString("LOC_LONGITUD")));
                                local.setAforo(jArray.getJSONObject(i).getInt("LOC_AFORO"));
                                local.setNosotros(jArray.getJSONObject(i).getString("LOC_NOSOTROS"));
                                local.setUrl(jArray.getJSONObject(i).getString("LOC_URL"));
                                //local.setGay(jArray.getJSONObject(i).getInt("LOC_GAY") == 1 ? true : false);
                                local.setGay(jArray.getJSONObject(i).getInt("LOC_GAY") == 1);
                                local.setFecha_inicio(jArray.getJSONObject(i).getString("LOC_FEC_INICIO"));
                                local.setFecha_fin(jArray.getJSONObject(i).getString("LOC_FEC_FIN"));
                                local.setDistrito(jArray.getJSONObject(i).getString("LOC_DISTRITO"));
                                local.setProvincia(jArray.getJSONObject(i).getString("LOC_PROVINCIA"));
                                local.setDepartamento(jArray.getJSONObject(i).getString("LOC_DEPARTAMENTO"));
                                local.setPlus(jArray.getJSONObject(i).getInt("LOC_PLUS") == 1);
                                local.setEstado(jArray.getJSONObject(i).getInt("LOC_ESTADO") == 1);
                                local.setRazon_social(jArray.getJSONObject(i).getString("LOC_RAZ_SOCIAL"));
                                local.setRuc(jArray.getJSONObject(i).getString("LOC_RUC"));

                                realm.copyToRealm(local);
                                realm.commitTransaction();
                            }
                            realm.close();
                            //agregarMakers();
                            cargarRealmListas();
                            Log.d(TAG, jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString(), e);
                        }
                        hidepDialog(progressDialog);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(error.toString(), error);
                        hidepDialog(progressDialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CAT_ID", String.valueOf(tipoLocal));
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    private void requestLocalXGenero() {
        showDialog(progressDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.LOCALES_X_GENERO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jArray = jsonObject.getJSONArray("local");
                            Realm realm = Realm.getDefaultInstance();
                            for (int i = 0; i < jArray.length(); i++) {
                                realm.beginTransaction();
                                Establecimiento local = realm.createObject(Establecimiento.class, Establecimiento.getUltimoId());
                                local.setId_server(jArray.getJSONObject(i).getInt("LOC_ID"));
                                local.setNombre(jArray.getJSONObject(i).getString("LOC_NOMBRE"));
                                local.setDireccion(jArray.getJSONObject(i).getString("LOC_DIRECCION"));
                                local.setLatitud(Double.parseDouble(jArray.getJSONObject(i).getString("LOC_LATITUD")));
                                local.setLongitud(Double.parseDouble(jArray.getJSONObject(i).getString("LOC_LONGITUD")));
                                local.setAforo(jArray.getJSONObject(i).getInt("LOC_AFORO"));
                                local.setNosotros(jArray.getJSONObject(i).getString("LOC_NOSOTROS"));
                                local.setUrl(jArray.getJSONObject(i).getString("LOC_URL"));
                                //local.setGay(jArray.getJSONObject(i).getInt("LOC_GAY") == 1 ? true : false);
                                local.setGay(jArray.getJSONObject(i).getInt("LOC_GAY") == 1);
                                local.setFecha_inicio(jArray.getJSONObject(i).getString("LOC_FEC_INICIO"));
                                local.setFecha_fin(jArray.getJSONObject(i).getString("LOC_FEC_FIN"));
                                local.setDistrito(jArray.getJSONObject(i).getString("LOC_DISTRITO"));
                                local.setProvincia(jArray.getJSONObject(i).getString("LOC_PROVINCIA"));
                                local.setDepartamento(jArray.getJSONObject(i).getString("LOC_DEPARTAMENTO"));
                                local.setPlus(jArray.getJSONObject(i).getInt("LOC_PLUS") == 1);
                                local.setEstado(jArray.getJSONObject(i).getInt("LOC_ESTADO") == 1);
                                local.setRazon_social(jArray.getJSONObject(i).getString("LOC_RAZ_SOCIAL"));
                                local.setRuc(jArray.getJSONObject(i).getString("LOC_RUC"));

                                realm.copyToRealm(local);
                                realm.commitTransaction();
                            }
                            realm.close();
                            //agregarMakers();
                            cargarRealmListas();
                            Log.d(TAG, jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString(), e);
                        }
                        hidepDialog(progressDialog);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(error.toString(), error);
                        hidepDialog(progressDialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("GEN_ID", String.valueOf(tipoMusica));
                params.put("CAT_ID", String.valueOf(tipoLocal));
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    private void vaciarLocalesRealm() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Establecimiento> local_lista = realm.where(Establecimiento.class).findAll();
        realm.beginTransaction();
        local_lista.deleteAllFromRealm();
        realm.commitTransaction();
    }

    /*private void cargaDataEventosRealm() {
        realm = Realm.getDefaultInstance();
        String[] strArray = new String[]{"FIESTA BLACK", "SALSA VS REGUETON", "FIESTA DE PISCO", "FIESTA SEMÁFORO"};
        for (int i = 0; i < strArray.length; i++) {
            realm.beginTransaction();
            Establecimiento evento = realm.createObject(Establecimiento.class);
            evento.setId(Establecimiento.getUltimoId());
            evento.setId_server(1);
            evento.setNombre(strArray[i]);
            evento.setFecha_evento("Viernes 07 de Agosto 2015");
            evento.setHora_evento("10:00pm");
            evento.setImagen(R.drawable.concierto);
            evento.setNombre_evento("");
            evento.setTipo(1);
            evento.setAforo(10);
            evento.setCalificacion(5);
            realm.copyToRealm(evento);
            realm.commitTransaction();
            realm.close();
            Log.d(TAG, evento.toString());
        } Log.d(TAG, "cargaDataEventosRealm/");
    }*/

    private void iniciarLayout() {
        setFondoActivity(this, ivFondoListaDiscoteca);
        setToolbarSon(toolbar, this, icono_toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
