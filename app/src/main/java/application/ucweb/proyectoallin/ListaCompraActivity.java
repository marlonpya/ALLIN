package application.ucweb.proyectoallin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import application.ucweb.proyectoallin.adapter.ListaCompraA;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Producto;
import butterknife.BindView;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class ListaCompraActivity extends BaseActivity {
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.rrv_lista_compra) RealmRecyclerView recyclerView;
    @BindView(R.id.tv_total_compra) TextView total_compra;
    private Realm realm;
    private ListaCompraA adapter;
    private RealmResults<Producto> productos;
    public static double valor_compra = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compra);
        iniciarLayout();
        iniciarRRV();
    }

    private void iniciarRRV() {
        total_compra.setText(String.valueOf(valor_compra));
        realm = Realm.getDefaultInstance();
        productos = realm.where(Producto.class).equalTo(Producto.A_CARRITO, true).findAll();
        adapter = new ListaCompraA(this, productos, true, true, total_compra);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btnAceptarProductos)
    public void aceptarProductos() {
        startActivity(new Intent(this, DetalleCompraActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void iniciarLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
    }
}
