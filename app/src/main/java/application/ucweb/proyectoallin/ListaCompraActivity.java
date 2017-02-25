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
import application.ucweb.proyectoallin.modelparseable.EstablecimientoSimple;
import application.ucweb.proyectoallin.modelparseable.EventoSimple;
import application.ucweb.proyectoallin.modelparseable.ProductoSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.OnClick;

public class ListaCompraActivity extends BaseActivity {
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.rrv_lista_compra) RecyclerView recyclerView;
    @BindView(R.id.tv_total_compra) TextView total_compra;
    private ListaCompraAdapter adapter;
    //private MisPuntosCanjeAdapter adapter;
    public static double valor_compra = 0.0;
    private ArrayList<ProductoSimple> carrito = new ArrayList<>();
    private EstablecimientoSimple local;
    private EventoSimple evento;
    private int tipoVista = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compra);
        iniciarLayout();
        carrito=(ArrayList<ProductoSimple>)getIntent().getSerializableExtra(Constantes.ARRAY_S_CARRITO);
        if (getIntent().hasExtra(Constantes.OBJ_S_ESTABLECIMIENTO)){
            local=(EstablecimientoSimple)getIntent().getSerializableExtra(Constantes.OBJ_S_ESTABLECIMIENTO);
            tipoVista=Constantes.VISTA_DE_LOCAL;
        }else if (getIntent().hasExtra(Constantes.OBJ_S_EVENTO)){
            evento=(EventoSimple) getIntent().getSerializableExtra(Constantes.OBJ_S_EVENTO);
            tipoVista=Constantes.VISTA_DE_EVENTO;
        }
        iniciarRV();
    }

    private void iniciarRV() {
        total_compra.setText(String.valueOf(valor_compra));
        adapter = new ListaCompraAdapter(this, carrito, total_compra);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btnAceptarProductos)
    public void aceptarProductos() {
        if (tipoVista==Constantes.VISTA_DE_LOCAL){
            startActivity(new Intent(this, DetalleCompraActivity.class)
                    .putExtra(Constantes.ARRAY_S_CARRITO, adapter.getArray())
                    .putExtra(Constantes.OBJ_S_ESTABLECIMIENTO, local));
        }else if (tipoVista==Constantes.VISTA_DE_EVENTO){
            startActivity(new Intent(this, DetalleCompraActivity.class)
                    .putExtra(Constantes.ARRAY_S_CARRITO, adapter.getArray())
                    .putExtra(Constantes.OBJ_S_EVENTO, evento));
        }
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
