package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.modelparseable.EstablecimientoSimple;
import application.ucweb.proyectoallin.modelparseable.EventoSimple;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;

public class Calendario2Activity extends BaseActivity implements IActividad{
    public static final String TAG =Calendario2Activity.class.getSimpleName();

    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    private ProgressDialog pDialog;
    //private ArrayList<EventoSimple> lista_eventos = new ArrayList<>();
    private ArrayList<Date> lista_fechas = new ArrayList<>();
    private boolean isLocal = false;
    private int tipoLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario2);
        iniciarPDialog();
        setToolbarSon(toolbar, this, icono_toolbar);
        tipoLocal = getIntent().getIntExtra(Constantes.TIPO_ESTABLECIMIENTO, -1);
        isLocal = tipoLocal != -1;
        if (!isLocal) {
            if (ConexionBroadcastReceiver.isConect()) {
                requestFechas();
            }
        }
        else setCalendar();
    }

    private void setCalendar(){
        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        caldroidFragment.setMinDate(new Date());

        if (!isLocal){
            for (int i = 0; i < lista_fechas.size(); i++) {
                caldroidFragment.setTextColorForDate(R.color.colorAccent, lista_fechas.get(i));
            }
        }
        CaldroidListener cl = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int diaSemana = c.get(Calendar.DAY_OF_WEEK);
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM, yyyy", new Locale("es", "pe"));
                if (isLocal) {
                    mostrarListaLocales(tipoLocal, diaSemana, sdf.format(date));
                }
                else {
                    SimpleDateFormat sdfFullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "pe"));
                    SimpleDateFormat sfdSimpleDate = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "pe"));
                    for (int i = 0; i < lista_fechas.size(); i++) {
                        try {
                            Date simpleDate = sdfFullDate.parse(sfdSimpleDate.format(lista_fechas.get(i)) + " 00:00:00");
                            if (simpleDate.compareTo(date)==0){
                                mostrarListaEventos(date, sdf.format(date));
                                break;
                            }
                        } catch (ParseException e) {e.printStackTrace();}
                    }
                }
            }
        };
        caldroidFragment.setCaldroidListener(cl);
        caldroidFragment.refreshView();
        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.activity_calendario2, caldroidFragment);
        t.commit();
    }

    private void mostrarListaEventos(Date fecha,  String s_fecha){
        Intent intent = new Intent(this, ListaEventoActivity.class);
        intent.putExtra(Constantes.I_EVENTO_DIALOG, 3);
        intent.putExtra(Constantes.FECHA, fecha.getTime());
        intent.putExtra(Constantes.FILTRO, Constantes.FILTRO_CALENDARIO);
        intent.putExtra(Constantes.S_EVENTO_TOOLBAR, s_fecha.toUpperCase());
        startActivity(intent);
    }

    private void mostrarListaLocales(int tipo_local, int diaSemana, String s_fecha){

        Intent intent = new Intent(this, ListaDiscotecasActivity.class);
        intent.putExtra(Constantes.TIPO_ESTABLECIMIENTO, tipo_local);
        intent.putExtra(Constantes.FILTRO, Constantes.FILTRO_CALENDARIO);
        intent.putExtra(Constantes.DIA, diaSemana);
        intent.putExtra(Constantes.K_S_TITULO_TOOLBAR, s_fecha.toUpperCase());
        startActivity(intent);
    }

    @Override
    public boolean isSesion() {
        return false;
    }

    @Override
    public void iniciarLayout() { }

    @Override
    public void iniciarPDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.enviando_peticion));
    }

    private void requestFechas() {
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.FECHAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                Date fecha = new Date();
                                JSONArray jEventos = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jEventos.length(); i++) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "pe"));
                                    try {
                                        fecha = sdf.parse(jEventos.getJSONObject(i).getString("EVE_FEC_INICIO"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    lista_fechas.add(fecha);
                                }
                                Log.d(TAG, lista_fechas.toString());
                                setCalendar();
                            }else {
                                new AlertDialog.Builder(Calendario2Activity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(getString(R.string.eventos_not_found))
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
        );
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }
}
