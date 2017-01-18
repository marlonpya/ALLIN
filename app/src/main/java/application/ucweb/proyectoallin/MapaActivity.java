package application.ucweb.proyectoallin;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Establecimiento;
import application.ucweb.proyectoallin.util.Constantes;
import io.realm.Realm;
import io.realm.RealmResults;

public class MapaActivity extends BaseActivity implements OnMapReadyCallback {
    private RealmResults<Establecimiento> establecimientos;
    private Realm realm;
    private GoogleMap mMap;
    private int tipo_establecimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        realm = Realm.getDefaultInstance();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().hasExtra(Constantes.I_TIP_ESTABLECIMIENTO_MAPA)) {
            tipo_establecimiento = getIntent().getIntExtra(Constantes.I_TIP_ESTABLECIMIENTO_MAPA, -1);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-12.0888899, -77.0800761);
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_allin)).position(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((sydney),16));

        /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(41.889, -87.622), 16));*/

        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_disco))

                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(-12.0838899, -77.0804761)));

        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_karaoke))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(-12.0848899, -77.0808761)));

        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_restobar))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(-12.0851899, -77.0802261)));
    }
}
