package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

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
import application.ucweb.proyectoallin.modelparseable.EventoSimple;
import application.ucweb.proyectoallin.util.Constantes;
import hirondelle.date4j.DateTime;

public class Calendario2Activity extends BaseActivity implements IActividad{
    private static final String TAG = Calendario2Activity.class.getSimpleName();
    private ProgressDialog pDialog;
    private int id;
    private ArrayList<EventoSimple> lista_eventos = new ArrayList<>();
    private boolean isLocal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario2);
        iniciarPDialog();
        id = getIntent().getIntExtra(Constantes.I_EVENTO_DIALOG, -1);
        isLocal = id == -1;
        requestEventos();

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        //caldroidFragment.setMinDate(new Date());
        for (int i = 0; i < lista_eventos.size(); i++) {
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(String.valueOf(lista_eventos.get(i).getFecha_inicio()));
                Log.d(TAG, date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            caldroidFragment.setTextColorForDate(R.color.colorAccent, date);
        }

        CaldroidListener cl = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                SimpleDateFormat y = new SimpleDateFormat("yyyy");
                SimpleDateFormat m = new SimpleDateFormat("MM");
                SimpleDateFormat d = new SimpleDateFormat("dd");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try {
                    Date startDay = sdf.parse(y.format(date) + "-" + m.format(date) + "-" + d.format(date) + " 00:00:00");
                    Date endDay = sdf.parse(y.format(date) + "-" + m.format(date) + "-" + d.format(date) + " 23:59:59");
                    if (getIntent().hasExtra(Constantes.TIPO_ESTABLECIMIENTO)) {
                        int tipoLocal=getIntent().getIntExtra(Constantes.TIPO_ESTABLECIMIENTO, -1);
                        mostrarLista(tipoLocal, startDay, endDay);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        caldroidFragment.setCaldroidListener(cl);
        caldroidFragment.refreshView();

        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.activity_calendario2, caldroidFragment);
        t.commit();

    }

    private HashMap<DateTime, Drawable> getDatez(ArrayList<EventoSimple> eventos) {
        HashMap<DateTime, Drawable> hazh = new HashMap<>();
        for (int i = 0; i < eventos.size(); i++) {
            Date date = eventos.get(i).getFecha_inicio();
            hazh.put(new DateTime(date.toString()), new ColorDrawable(Color.GREEN));
        }
        Log.d(TAG, hazh.toString());
        return hazh;
    }

    private void mostrarLista(int tipo_local, Date start, Date end){
        Intent intent = new Intent(this, ListaDiscotecasActivity.class);
        intent.putExtra(Constantes.TIPO_ESTABLECIMIENTO, tipo_local);
        intent.putExtra(Constantes.FILTRO, Constantes.FILTRO_CALENDARIO);
        intent.putExtra("START", start.getTime());
        intent.putExtra("END", end.getTime());
        startActivity(intent);
    }

    private void requestEventos() {
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.EVENTOS,
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
                                    EventoSimple evento = new EventoSimple();
                                    evento.setId_server(jEventos.getJSONObject(i).getInt("EVE_ID"));
                                    evento.setNombre(jEventos.getJSONObject(i).getString("EVE_NOMBRE"));
                                    evento.setImagen("");
                                    evento.setPrecio(Double.parseDouble(jEventos.getJSONObject(i).getString("EVE_PRECIO")));
                                    evento.setLatitud(jEventos.getJSONObject(i).getString("EVE_LATITUD").isEmpty() ? 0 : Double.parseDouble(jEventos.getJSONObject(i).getString("EVE_LATITUD")));
                                    evento.setLongitud(jEventos.getJSONObject(i).getString("EVE_LONGITUD").isEmpty() ? 0 : Double.parseDouble(jEventos.getJSONObject(i).getString("EVE_LONGITUD")));
                                    evento.setTipo(jEventos.getJSONObject(i).getInt("EVE_TIPO"));
                                    evento.setId_local(jEventos.getJSONObject(i).getInt("LOC_ID"));
                                    evento.setNombre_local("");
                                    try {
                                        evento.setFecha_inicio(sdf.parse(jEventos.getJSONObject(i).getString("EVE_FEC_INICIO")));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    lista_eventos.add(evento);
                                }
                                Log.d(TAG, lista_eventos.toString());
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
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", "3"); //3 es _todo en un método del servicio
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
    public void iniciarLayout() {  }

    @Override
    public void iniciarPDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.actualizando));
    }
}
