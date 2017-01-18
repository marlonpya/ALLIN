package application.ucweb.proyectoallin;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import application.ucweb.proyectoallin.adapter.ListaClientesCorporativoRAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.EnLista;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ListaClientesCorporativoActivity extends BaseActivity {
    @BindView(R.id.rrv_clientes_corporativo) RealmRecyclerView realmRecyclerView;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.fondo_lista_clientes_corporativo) ImageView fondo;
    private Realm realm;
    private ListaClientesCorporativoRAdapter adapter;
    private RealmResults<EnLista> fechas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes_corporativo);
        iniciarLayout();
        iniciarRRV();
        if (EnLista.getUltimoId() == 0) EnLista.cargarData();
    }

    private void iniciarRRV() {
        realm = Realm.getDefaultInstance();
        fechas = realm.where(EnLista.class).findAll().sort(EnLista.ID, Sort.DESCENDING);
        adapter = new ListaClientesCorporativoRAdapter(this, fechas, true, true);
        realmRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void iniciarLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
        setFondoActivity(this, fondo);
    }
}
