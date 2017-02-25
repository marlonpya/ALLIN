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
import android.widget.TextView;
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
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.modelparseable.EstablecimientoSimple;
import application.ucweb.proyectoallin.modelparseable.EventoSimple;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;

public class Calendario2Activity extends BaseActivity implements IActividad{
    public static final String TAG =Calendario2Activity.class.getSimpleName();

    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.tvDescripcionToolbar) TextView tvDescripcionToolbar;
    private ProgressDialog pDialog;
    //private ArrayList<EventoSimple> lista_eventos = new ArrayList<>();
    private ArrayList<Date> lista_fechas = new ArrayList<>();
    private boolean isLocal = false;
    private int tipoLocal;

    private EstablecimientoSimple local;
    private String strFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario2);
        iniciarPDialog();
        setToolbarSon(toolbar, this, icono_toolbar);
        tvDescripcionToolbar.setText(getString(R.string.seleccione_fecha));

        if (getIntent().hasExtra(Constantes.CALENDARIO_APUNTAR_LISTA)){
            local=(EstablecimientoSimple)getIntent().getSerializableExtra(Constantes.OBJ_S_ESTABLECIMIENTO);
            setCalendarApuntarEnLista();
            getIntent().removeExtra(Constantes.CALENDARIO_APUNTAR_LISTA);
        }else {
            tipoLocal = getIntent().getIntExtra(Constantes.TIPO_ESTABLECIMIENTO, -1);
            isLocal = tipoLocal != -1;
            if (!isLocal) {
                if (ConexionBroadcastReceiver.isConect()) {
                    requestFechas();
                }
            }
            else setCalendar();
        }
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

    private void setCalendarApuntarEnLista(){
        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        caldroidFragment.setMinDate(cal.getTime());
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.DAY_OF_MONTH, maxDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        caldroidFragment.setMaxDate(maxDate.getTime());

        CaldroidListener cl = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {

                SimpleDateFormat sfdSimpleDate = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "pe"));
                strFecha=sfdSimpleDate.format(date) + " 00:00:00";
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int diaSemana = c.get(Calendar.DAY_OF_WEEK);
                switch (diaSemana){
                    case 1: if (local.isDomingo()) showDialogApuntarFecha(date); else showDialogNoDisponible(); break;
                    case 2: if (local.isLunes()) showDialogApuntarFecha(date); else showDialogNoDisponible(); break;
                    case 3: if (local.isMartes()) showDialogApuntarFecha(date); else showDialogNoDisponible(); break;
                    case 4: if (local.isMiercoles()) showDialogApuntarFecha(date); else showDialogNoDisponible(); break;
                    case 5: if (local.isJueves()) showDialogApuntarFecha(date); else showDialogNoDisponible(); break;
                    case 6: if (local.isViernes()) showDialogApuntarFecha(date); else showDialogNoDisponible(); break;
                    case 7: if (local.isSabado()) showDialogApuntarFecha(date); else showDialogNoDisponible(); break;
                }
            }
        };
        caldroidFragment.setCaldroidListener(cl);
        caldroidFragment.refreshView();
        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.activity_calendario2, caldroidFragment);
        t.commit();
    }

    private void showDialogApuntarFecha(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d 'de' MMMM, yyyy", new Locale("es", "pe"));
        new AlertDialog.Builder(Calendario2Activity.this)
                .setTitle(R.string.fecha_select)
                .setMessage(sdf.format(date) + getString(R.string.es_correcto))
                .setCancelable(false)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestRegistrarEnLista();
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showDialogNoDisponible(){
        new AlertDialog.Builder(Calendario2Activity.this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.fecha_no_disponible)
                .setCancelable(true)
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
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

    private void requestRegistrarEnLista() {
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.REGISTRAR_EN_LISTA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String mensaje=jsonObject.getString("message");
                            if (jsonObject.getBoolean("status")) {
                                new AlertDialog.Builder(Calendario2Activity.this)
                                        .setTitle(R.string.ya_estas_en_lista_titulo_si)
                                        .setMessage(getString(R.string.ya_estas_en_lista_mensaje_si))
                                        .setIcon(R.drawable.iconobuenarosa)
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
                                new AlertDialog.Builder(Calendario2Activity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(mensaje)
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
                params.put("loc_id", String.valueOf(local.getId_server()));
                params.put("usu_id", String.valueOf(Usuario.getUsuario().getId_server()));
                params.put("fecha", strFecha);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Configuracion.getInstance().addToRequestQueue(request, TAG);
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
