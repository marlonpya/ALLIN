package application.ucweb.proyectoallin;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import application.ucweb.proyectoallin.adapter.DetalleCompraAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Producto;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class DetalleCompraActivity extends BaseActivity {
    @BindView(R.id.rrv_lista_detalle_compra) RealmRecyclerView realmRecyclerView;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.tv_aceptar_terminos_compra) TextView aceptar_terminos;
    @BindView(R.id.fondo_detalle_compra) ImageView fondo;
    @BindView(R.id.tvDescripcionToolbar) TextView toolbar_descripcion;
    @BindView(R.id.tv_total_compra_detalle) TextView total_compra;
    private Realm realm;
    private DetalleCompraAdapter adapter;
    private RealmResults<Producto> productos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_compra);
        inciarLayout();
        iniciarRRV();
    }

    private void inciarLayout() {
        setFondoActivity(this, fondo);
        setToolbarSon(toolbar, this, icono_toolbar);
        toolbar_descripcion.setText(getString(R.string.medio_pago));
        aceptar_terminos.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private void iniciarRRV() {
        realm = Realm.getDefaultInstance();
        productos = realm.where(Producto.class).equalTo(Producto.A_CARRITO, true).findAll();
        adapter = new DetalleCompraAdapter(this, productos, true, true);
        realmRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        double cantidad = 0.0;
        for (Producto producto : productos){
            cantidad =+ (producto.getPrecio_allin() * producto.getCantidad());
        }
        total_compra.setText("S/. "+String.valueOf(cantidad - 15.00));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
