package application.ucweb.proyectoallin;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import application.ucweb.proyectoallin.adapter.EventoListaRapidaRealmAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Establecimiento;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class ListaRapidaActivity extends BaseActivity {
    public static final String TAG = ListaRapidaActivity.class.getSimpleName();
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.tvDescripcionToolbar) TextView tvListaRapida;
    @BindView(R.id.lstListaRapida)RealmRecyclerView lst_eventos;
    @BindView(R.id.spRegistro)Spinner spRegistro;
    @BindView(R.id.idiv_layout_lista_rapida) ImageView fondo;
    private Realm realm;
    private RealmResults<Establecimiento> listaRapidaEventos;
    private EventoListaRapidaRealmAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rapida);
        iniciarLayout();
        if (Establecimiento.getUltimoId(6) == 0) { cargarDataListaRapida(); }
        cargarRealmListas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarRealmListas();
    }

    private void cargarDataListaRapida() {
        realm = Realm.getDefaultInstance();
        String[] arrayString = new String[]{"FIESTA BLACK", "CONCIERTO LATINO", "RAINBOW PARTY", "CONCIERTO ELECTRO FESTIVAL", "FIESTA DE PISCO", "FIESTA HOLA JOVEN", "FIESTA RED BULL"};
        for (int i = 0; i < arrayString.length; i++) {
            realm.beginTransaction();
            Establecimiento evento = realm.createObject(Establecimiento.class);
            evento.setId(Establecimiento.getUltimoId());
            evento.setId_server(1);
            evento.setNombre_evento(arrayString[i]);
            evento.setFecha_inicio("SAB 08 DE AGOSTO 2015");
            evento.setFecha_fin("10:0");
            evento.setNombre("DISCOTECA NIGHT LIFE");
            evento.setTipo(6);
            realm.copyToRealm(evento);
            realm.commitTransaction();
            realm.close();
            Log.d(TAG, evento.toString());
        }
    }

    private void cargarRealmListas() {
        realm = Realm.getDefaultInstance();
        listaRapidaEventos = realm.where(Establecimiento.class).equalTo(Establecimiento.TIPO_EVENTO, 6).findAll();
        adapter = new EventoListaRapidaRealmAdapter(this, listaRapidaEventos, true, true);
        lst_eventos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void iniciarLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
        setSpinner(this, spRegistro, R.array.arrayFiltrarDiscoteca);
        setFondoActivity(this, fondo);
        tvListaRapida.setText(R.string.lista_rapida);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }

}
