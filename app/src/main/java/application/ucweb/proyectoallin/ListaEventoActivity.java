package application.ucweb.proyectoallin;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import application.ucweb.proyectoallin.adapter.EventoEspecialRealmAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Establecimiento;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class ListaEventoActivity extends BaseActivity {
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.lista_eventos)RealmRecyclerView lista_eventos;
    @BindView(R.id.tvDescripcionToolbar)TextView tvTituloListaEvento;
    @BindView(R.id.idiv_layout_lista_evento)ImageView ivFondoListaEvento;
    public static final String TAG = ListaEventoActivity.class.getSimpleName();
    private Realm realm;
    private EventoEspecialRealmAdapter adapter;
    private RealmResults<Establecimiento> listaEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_evento);
        iniciarLayout();
        tvTituloListaEvento.setText(getIntent().getStringExtra(Constantes.K_S_TITULO_TOOLBAR));
        if (Establecimiento.getUltimoId(4) == 0) { cargaDataEventosRealm(); }
        cargarRealmListas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarRealmListas();
    }

    private void cargaDataEventosRealm() {
        realm = Realm.getDefaultInstance();
        String[] strArray = new String[]{"FIESTA BLACK", "SALSA VS REGUETON", "FIESTA DE PISCO", "FIESTA SEMÁFORO"};
        for (int i = 0; i < strArray.length; i++) {
            realm.beginTransaction();
            Establecimiento evento = realm.createObject(Establecimiento.class);
            evento.setId(Establecimiento.getUltimoId());
            evento.setId_server(1);
            evento.setNombre_evento(strArray[i]);
            evento.setNombre("Night Life");
            evento.setFecha_inicio("Viernes 07 de Agosto 2015");
            evento.setFecha_fin("10:00pm");
            evento.setTipo_evento(4);
            realm.copyToRealm(evento);
            realm.commitTransaction();
            realm.close();
            Log.d(TAG, evento.toString());
        } Log.d(TAG, "cargaDataEventosRealm/");
    }

    public void iniciarLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
        setFondoActivity(this, ivFondoListaEvento);
    }

    private void cargarRealmListas() {
        realm = Realm.getDefaultInstance();
        listaEventos = realm.where(Establecimiento.class).equalTo(Establecimiento.TIPO_EVENTO, 4).findAll();
        adapter = new EventoEspecialRealmAdapter(this, listaEventos, true, true);
        lista_eventos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }
}
