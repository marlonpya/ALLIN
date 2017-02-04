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
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoallin.adapter.EstablecimientoAdapter;
import application.ucweb.proyectoallin.adapter.EstablecimientoRealmAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Establecimiento;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.model.zona.Distrito;
import application.ucweb.proyectoallin.modelparseable.EstablecimientoSimple;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ListaDiscotecasActivity extends BaseActivity{
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.lista_eventos) RecyclerView lista_eventos;
    @BindView(R.id.tvDescripcionToolbar)TextView toolbarListadiscoteca;
    @BindView(R.id.idiv_layout_lista_discoteca)ImageView ivFondoListaDiscoteca;
    public static final String TAG = ListaDiscotecasActivity.class.getSimpleName();
    @BindView(R.id.layout_activity_lista_discoteca) RelativeLayout layout;
    private EstablecimientoAdapter adapter;
    private ArrayList<EstablecimientoSimple> tempList = new ArrayList<>();
    private ArrayList<EstablecimientoSimple> listaLocales = new ArrayList<>();
    private ProgressDialog progressDialog;
        private String toolbarTitle;
        private Establecimiento establecimiento;
    private int tipoLocal;
    private int tipoFiltro;
    private int tipoMusica;
    private int diaSemana;
    private Date dayEnd;
    private Date date;

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
        progressDialog.setMessage(getString(R.string.enviando_peticion));
        toolbarTitle=getIntent().getStringExtra(Constantes.K_S_TITULO_TOOLBAR);
        tipoLocal=getIntent().getIntExtra(Constantes.TIPO_ESTABLECIMIENTO, -1);
        tipoFiltro=getIntent().getIntExtra(Constantes.FILTRO, -1);
        tipoMusica=getIntent().getIntExtra(Constantes.GENERO_MUSICA, -1);
        diaSemana = getIntent().getIntExtra(Constantes.DIA, -1);/*
        dayStart = new Date(getIntent().getLongExtra("START", -1));
        dayEnd = new Date(getIntent().getLongExtra("END", -1));
        date = new Date(getIntent().getLongExtra("DATE", -1));*/
        //Log.v("Amd", dayStart.toString());
        //Log.v("Amd", dayEnd.toString());
        toolbarListadiscoteca.setText(toolbarTitle);
        switch (tipoFiltro){
            case Constantes.FILTRO_DISTRITO:
                if (ConexionBroadcastReceiver.isConect()) requestLocalXCategoria();
                else ConexionBroadcastReceiver.showSnack(layout, this);
                break;
            case Constantes.FILTRO_MUSICA:
                if (ConexionBroadcastReceiver.isConect())requestLocalXGenero();
                else ConexionBroadcastReceiver.showSnack(layout, this);
                break;
            case Constantes.FILTRO_CALENDARIO:
                if (ConexionBroadcastReceiver.isConect())requestLocalXCategoria();
                else ConexionBroadcastReceiver.showSnack(layout, this);
                break;
        }
        //requestLocalXCategoria();
    }

    private void cargarListas() {
        switch (tipoFiltro){
            case Constantes.FILTRO_DISTRITO:
                filtrarPorDistrito();
                //listaEventos = realm.where(Establecimiento.class).equalTo("distrito", nombreDistrito).findAll(); break;
            case Constantes.FILTRO_MUSICA:
                iniciarRV();
                //listaEventos = realm.where(Establecimiento.class).findAll(); break;
            case Constantes.FILTRO_CALENDARIO:
                filtrarPorCalendario();
                //getListaLocalesXCalendario();

        }
    }

    private void filtrarPorCalendario(){
        for (int i = 0; i < tempList.size(); i++) {
            switch (diaSemana){
                case 1: if (tempList.get(i).isDomingo()) listaLocales.add(tempList.get(i)); break;
                case 2: if (tempList.get(i).isLunes()) listaLocales.add(tempList.get(i)); break;
                case 3: if (tempList.get(i).isMartes()) listaLocales.add(tempList.get(i)); break;
                case 4: if (tempList.get(i).isMiercoles()) listaLocales.add(tempList.get(i)); break;
                case 5: if (tempList.get(i).isJueves()) listaLocales.add(tempList.get(i)); break;
                case 6: if (tempList.get(i).isViernes()) listaLocales.add(tempList.get(i)); break;
                case 7: if (tempList.get(i).isSabado()) listaLocales.add(tempList.get(i)); break;
            }
        }
        iniciarRV();
    }

    private void filtrarPorDistrito(){
        for (int i = 0; i < tempList.size(); i++) {
            if (tempList.get(i).getDistrito().equals(toolbarTitle)){
                listaLocales.add(tempList.get(i));
            }
        }iniciarRV();
    }

    private void showNotFoundDialog(){
        new AlertDialog.Builder(ListaDiscotecasActivity.this)
                .setTitle(R.string.app_name)
                .setMessage(getString(R.string.establecimientos_not_found))
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
    private void iniciarRV(){
        if (listaLocales.size()>0){
            adapter = new EstablecimientoAdapter(this, listaLocales);
            lista_eventos.setHasFixedSize(true);
            lista_eventos.setLayoutManager(new LinearLayoutManager(this));
            lista_eventos.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else showNotFoundDialog();
    }

/*

    private void getListaLocalesXCalendario(){
        RealmResults<Establecimiento> tempList = realm.where(Establecimiento.class).findAll();
        ArrayList<Establecimiento> listE = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            //TODO comparar fechas
            listE.add(tempList.get(i));
        }
        //listaEventos=listE;
    }
*/


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
                            if (jsonObject.getBoolean("status")) {
                                JSONArray jArray = jsonObject.getJSONArray("local");
                                for (int i = 0; i < jArray.length(); i++) {
                                    EstablecimientoSimple local = new EstablecimientoSimple();
                                    local.setId_server(jArray.getJSONObject(i).getInt("LOC_ID"));
                                    local.setNombre(jArray.getJSONObject(i).getString("LOC_NOMBRE"));
                                    local.setDireccion(jArray.getJSONObject(i).getString("LOC_DIRECCION"));
                                    local.setLatitud(Double.parseDouble(jArray.getJSONObject(i).getString("LOC_LATITUD")));
                                    local.setLongitud(Double.parseDouble(jArray.getJSONObject(i).getString("LOC_LONGITUD")));
                                    local.setAforo(jArray.getJSONObject(i).getInt("LOC_AFORO"));
                                    local.setNosotros(jArray.getJSONObject(i).getString("LOC_NOSOTROS"));
                                    local.setUrl(jArray.getJSONObject(i).getString("LOC_URL"));
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
                                    local.setLunes(jArray.getJSONObject(i).getInt("LOC_LUNES") == 1);
                                    local.setMartes(jArray.getJSONObject(i).getInt("LOC_MARTES") == 1);
                                    local.setMiercoles(jArray.getJSONObject(i).getInt("LOC_MIERCOLES") == 1);
                                    local.setJueves(jArray.getJSONObject(i).getInt("LOC_JUEVES") == 1);
                                    local.setViernes(jArray.getJSONObject(i).getInt("LOC_VIERNES") == 1);
                                    local.setSabado(jArray.getJSONObject(i).getInt("LOC_SABADO")== 1);
                                    local.setDomingo(jArray.getJSONObject(i).getInt("LOC_DOMINGO")== 1);
                                    local.setPrecio(jArray.getJSONObject(i).getDouble("LOC_PRECIO"));
                                    tempList.add(local);
                                }
                                cargarListas();
                                Log.d(TAG, jsonObject.toString());
                            }else {
                                showNotFoundDialog();
                            }
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
                            for (int i = 0; i < jArray.length(); i++) {
                                EstablecimientoSimple local = new EstablecimientoSimple();
                                local.setId_server(jArray.getJSONObject(i).getInt("LOC_ID"));
                                local.setNombre(jArray.getJSONObject(i).getString("LOC_NOMBRE"));
                                local.setDireccion(jArray.getJSONObject(i).getString("LOC_DIRECCION"));
                                local.setLatitud(Double.parseDouble(jArray.getJSONObject(i).getString("LOC_LATITUD")));
                                local.setLongitud(Double.parseDouble(jArray.getJSONObject(i).getString("LOC_LONGITUD")));
                                local.setAforo(jArray.getJSONObject(i).getInt("LOC_AFORO"));
                                local.setNosotros(jArray.getJSONObject(i).getString("LOC_NOSOTROS"));
                                local.setUrl(jArray.getJSONObject(i).getString("LOC_URL"));
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
                                local.setLunes(jArray.getJSONObject(i).getInt("LOC_LUNES")== 1);
                                local.setMartes(jArray.getJSONObject(i).getInt("LOC_MARTES")== 1);
                                local.setMiercoles(jArray.getJSONObject(i).getInt("LOC_MIERCOLES")== 1);
                                local.setJueves(jArray.getJSONObject(i).getInt("LOC_JUEVES")== 1);
                                local.setViernes(jArray.getJSONObject(i).getInt("LOC_VIERNES")== 1);/*
                                local.setSabado(jArray.getJSONObject(i).getInt("LOC_SABADO")== 1);
                                local.setDomingo(jArray.getJSONObject(i).getInt("LOC_DOMINGO")== 1);*/
                                local.setPrecio(jArray.getJSONObject(i).getDouble("LOC_PRECIO"));
                                listaLocales.add(local);
                            }
                            cargarListas();
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

    public void iniciarLayout() {
        setFondoActivity(this, ivFondoListaDiscoteca);
        setToolbarSon(toolbar, this, icono_toolbar);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
