package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.load.engine.Resource;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.model.Establecimiento;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import io.realm.Realm;
import io.realm.RealmResults;

public class MapaActivity extends BaseActivity implements OnMapReadyCallback {
    public static final String TAG = MapaActivity.class.getSimpleName();
    private RealmResults<Establecimiento> establecimientos;
    private Realm realm;
    private GoogleMap mGoogleMap;
    private int tipo_establecimiento;
    private LocationManager locManager;
    private MyLocationListener myLocationListener;

    private Location ubicacionActual = new Location("");
    private ProgressDialog progressDialog;
    private boolean fijarMapa = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        realm = Realm.getDefaultInstance();


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.actualizando));

        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        myLocationListener = new MyLocationListener();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().hasExtra(Constantes.I_TIP_ESTABLECIMIENTO_MAPA)) {
            tipo_establecimiento = getIntent().getIntExtra(Constantes.I_TIP_ESTABLECIMIENTO_MAPA, -1);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ConexionBroadcastReceiver.isConect()){
            if (getIntent().hasExtra("TIPO")){
                switch (getIntent().getIntExtra("TIPO", -1)){
                    case 0: mostrarLocales(); break;
                    case 1: addSingleMarker(); break;
                }
            }
        }
        mGoogleMap.setMyLocationEnabled(true);
    }

    public void mostrarLocales(){
        vaciarLocalesRealm();
        requestLocalXCategoria();
    }

    public void addSingleMarker(){
        double lat, lon;
        lat=getIntent().getDoubleExtra("LAT", -1);
        lon=getIntent().getDoubleExtra("LON", -1);
        LatLng latLngTda = new LatLng(lat, lon);
        mGoogleMap.addMarker(new MarkerOptions()
                .position(latLngTda)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_allin)));
        //fijarMapa();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngTda, 17));
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
                            agregarMakers();
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
                params.put("CAT_ID", String.valueOf(tipo_establecimiento));
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }



    private void agregarMakers() {
        int idMarkerIcon=-1;
        switch (tipo_establecimiento){
            case Establecimiento.DISCOTECA: idMarkerIcon=R.drawable.marker_disco; break;
            case Establecimiento.RESTOBAR: idMarkerIcon=R.drawable.marker_restobar; break;
            case Establecimiento.KARAOKE: idMarkerIcon=R.drawable.marker_karaoke; break;
        }
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Establecimiento> local = realm.where(Establecimiento.class).findAll();
        for (int i = 0; i < local.size(); i++) {
            if (local.get(i).isEstado())
            {
                LatLng latLngTda = new LatLng(local.get(i).getLatitud(), local.get(i).getLongitud());
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(latLngTda)
                        .icon(BitmapDescriptorFactory.fromResource(idMarkerIcon))
                        .title(local.get(i).getNombre())).setTag(local.get(i));
            }
        }
    }
    private void vaciarLocalesRealm() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Establecimiento> local_lista = realm.where(Establecimiento.class).findAll();
        realm.beginTransaction();
        local_lista.deleteAllFromRealm();
        realm.commitTransaction();
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            progressDialog.show();
            ubicacionActual.setLatitude(location.getLatitude());
            ubicacionActual.setLongitude(location.getLongitude());
            if (fijarMapa) fijarMapa();
            progressDialog.hide();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    private void comenzarLocacion() {
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
        locManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, myLocationListener);
    }

    private void detenerLocacion() {
        locManager.removeUpdates(myLocationListener);
    }


    private void fijarMapa() {
        if (getIntent().hasExtra("TIPO")) {
            if (getIntent().getIntExtra("TIPO", -1) == 0) {
                LatLng latLng = new LatLng(ubicacionActual.getLatitude(), ubicacionActual.getLongitude());
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            }
        }
        fijarMapa = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        comenzarLocacion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        comenzarLocacion();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detenerLocacion();
    }
}
