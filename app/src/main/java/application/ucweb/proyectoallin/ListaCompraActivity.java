package application.ucweb.proyectoallin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import application.ucweb.proyectoallin.adapter.ListaCompraAdapter;
import application.ucweb.proyectoallin.adapter.MisPuntosCanjeAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.modelparseable.ItemCarrito;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.OnClick;

public class ListaCompraActivity extends BaseActivity {
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.rrv_lista_compra) RecyclerView recyclerView;
    @BindView(R.id.tv_total_compra) TextView total_compra;
    //private Realm realm;
    //private ListaCompraAdapter adapter;
    private MisPuntosCanjeAdapter adapter;
    //private RealmResults<Producto> productos;
    public static double valor_compra = 0.0;
    ArrayList<ItemCarrito> test = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compra);
        iniciarLayout();
        test=(ArrayList<ItemCarrito>)getIntent().getSerializableExtra(Constantes.ARRAY_S_CARRITO);
        iniciarRRV();
    }

    private void iniciarRRV() {
        total_compra.setText(String.valueOf(valor_compra));
        adapter = new MisPuntosCanjeAdapter(this, test, total_compra);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btnAceptarProductos)
    public void aceptarProductos() {
        //Log.d("Amd", adapter.getArray().toString());

        //startActivity(new Intent(this, DetalleCompraActivity.class).putExtra(Constantes.ARRAY_S_CARRITO, adapter.getArray()));
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
