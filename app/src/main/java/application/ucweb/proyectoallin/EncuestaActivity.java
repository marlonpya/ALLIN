package application.ucweb.proyectoallin;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import application.ucweb.proyectoallin.adapter.EncuestaAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Encuesta;
import butterknife.BindView;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class EncuestaActivity extends BaseActivity {
    public static final String TAG = EncuestaActivity.class.getSimpleName();
    @BindView(R.id.rrv_lista_encuesta) RealmRecyclerView rvv_lista_encuesta;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.idiv_layout_encuesta_allin)ImageView ivFondoEncuestaA;
    @BindView(R.id.tvDescripcionToolbar) TextView tvTituloEncuestaA;
    private Realm realm;
    private EncuestaAdapter adapter;
    private RealmResults<Encuesta> lista_encuestas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        iniciaLayout();
        if (Encuesta.getUltimoId() == 0) cargarData();
        cargarRRV();
    }

    private void cargarRRV() {
        realm = Realm.getDefaultInstance();
        lista_encuestas = realm.where(Encuesta.class).findAll();
        adapter = new EncuestaAdapter(this, lista_encuestas, true, true);
        rvv_lista_encuesta.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void cargarData() {
        realm = Realm.getDefaultInstance();
        for (int i = 0; i < 10; i++) {
            realm.beginTransaction();
            Encuesta encuesta = realm.createObject(Encuesta.class);
            encuesta.setId(Encuesta.getUltimoId());
            encuesta.setPregunta("¿ Te gusta nuestra variedad de tragos ?");
            realm.copyToRealm(encuesta);
            realm.commitTransaction();
        }
        realm.close();
    }

    private void iniciaLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
        setFondoActivity(this, ivFondoEncuestaA);
        tvTituloEncuestaA.setText("ENCUESTA");
    }

    @OnClick(R.id.btnRespondidoTodo)
    public void respondidoEncuesta() { onBackPressed(); }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
