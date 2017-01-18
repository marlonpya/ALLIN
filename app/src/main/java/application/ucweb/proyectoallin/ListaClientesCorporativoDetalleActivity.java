package application.ucweb.proyectoallin;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import application.ucweb.proyectoallin.adapter.ListaClientesCorporativoDetalleRAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Cliente;
import application.ucweb.proyectoallin.model.EnLista;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

import static application.ucweb.proyectoallin.aplicacion.BaseActivity.setToolbarSon;

public class ListaClientesCorporativoDetalleActivity extends BaseActivity {
    public static final String TAG = ListaClientesCorporativoDetalleActivity.class.getSimpleName();
    @BindView(R.id.fondo_lista_clientes_corporativo_detalle) ImageView fondo;
    @BindView(R.id.rrv_clientes_corporativo_detalle) RealmRecyclerView realmRecyclerView;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    private Realm realm;
    private ListaClientesCorporativoDetalleRAdapter adapter;
    private RealmResults<EnLista> listas;
    private RealmResults<Cliente> clientes;
    private String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes_corporativo_detalle);
        iniciarLayout();
        iniciarRRV();
    }

    private void iniciarRRV() {
        if (getIntent().hasExtra(Constantes.L_ID_ENLISTA)) {
            fecha = getIntent().getStringExtra(Constantes.L_ID_ENLISTA);
            realm = Realm.getDefaultInstance();
            //clientes = realm.where(EnLista.class).equalTo("clientes.id", extra_id).findAll();
            listas = realm.where(EnLista.class).equalTo("clientes.nombre", fecha).findAll();
            clientes = listas.where().findFirst().getClientes().sort("id");
            adapter = new ListaClientesCorporativoDetalleRAdapter(this, clientes, true, true);
            Log.d(TAG, clientes.toString());
        } else { System.err.print("no recibió id"); }
    }

    private void iniciarLayout() {
        setToolbarSon(toolbar, this);
        setFondoActivity(this, fondo);
    }

}
