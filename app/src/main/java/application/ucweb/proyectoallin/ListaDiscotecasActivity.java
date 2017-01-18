package application.ucweb.proyectoallin;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import application.ucweb.proyectoallin.adapter.EstablecimientoRealmAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Establecimiento;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_discotecas);
        iniciarLayout();
        /*if (Establecimiento.getUltimoId(1) == 0) {
            cargaDataEventosRealm();
        }*/
        toolbarListadiscoteca.setText(getIntent().getStringExtra(Constantes.K_S_TITULO_TOOLBAR));
        cargarRealmListas();
    }

    private void cargarRealmListas() {
        realm = Realm.getDefaultInstance();
        listaEventos = realm.where(Establecimiento.class).equalTo(Establecimiento.TIPO_EVENTO, 1).findAll();
        adapter = new EstablecimientoRealmAdapter(this, listaEventos);
        lista_eventos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
            evento.setTipo_evento(1);
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
