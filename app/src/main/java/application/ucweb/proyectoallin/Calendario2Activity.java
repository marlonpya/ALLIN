package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.model.Establecimiento;
import application.ucweb.proyectoallin.util.Constantes;
import io.realm.Realm;
import io.realm.RealmResults;

public class Calendario2Activity extends AppCompatActivity {
    public static final String TAG =Calendario2Activity.class.getSimpleName();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario2);
        iniciarPDialog();

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        caldroidFragment.setMinDate(new Date());
/*
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Establecimiento> local = realm.where(Establecimiento.class).findAll();
        for (int i = 0; i < local.size(); i++){
            if (new Date().before(local.get(i).getFechaFormato())){
                caldroidFragment.setTextColorForDate(R.color.orange_dark, local.get(i).getFechaFormato());
            }
        }*/

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
                    if (getIntent().hasExtra(Constantes.TIPO_ESTABLECIMIENTO)){
                        int tipoLocal=getIntent().getIntExtra(Constantes.TIPO_ESTABLECIMIENTO, -1);
                        mostrarLista(tipoLocal, startDay, endDay);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        };
        caldroidFragment.setCaldroidListener(cl);


        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.activity_calendario2, caldroidFragment);
        t.commit();

    }

    private void mostrarLista(int tipo_local, Date start, Date end){
        Intent intent = new Intent(this, ListaDiscotecasActivity.class);
        intent.putExtra(Constantes.TIPO_ESTABLECIMIENTO, tipo_local);
        intent.putExtra(Constantes.FILTRO, Constantes.FILTRO_CALENDARIO);
        intent.putExtra("START", start.getTime());
        intent.putExtra("END", end.getTime());
        startActivity(intent);

    }


    public void iniciarPDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.actualizando));
    }

    private void requestLocalXCategoria(final int tipo_local) {
        BaseActivity.showDialog(pDialog);
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
                            Log.d(TAG, jsonObject.toString());
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
                        BaseActivity.hidepDialog(pDialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("CAT_ID", String.valueOf(tipo_local));
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }
}
