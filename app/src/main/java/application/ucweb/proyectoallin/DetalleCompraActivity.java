package application.ucweb.proyectoallin;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import application.ucweb.proyectoallin.adapter.DetalleCompraAdapter;
import application.ucweb.proyectoallin.adapter.DetalleCompraAdapter2;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.modelparseable.EstablecimientoSimple;
import application.ucweb.proyectoallin.modelparseable.EventoSimple;
import application.ucweb.proyectoallin.modelparseable.ProductoSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class DetalleCompraActivity extends BaseActivity {
    @BindView(R.id.rrv_lista_detalle_compra) RecyclerView recyclerView;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.tv_aceptar_terminos_compra) TextView aceptar_terminos;
    @BindView(R.id.fondo_detalle_compra) ImageView fondo;
    @BindView(R.id.tvDescripcionToolbar) TextView toolbar_descripcion;
    @BindView(R.id.tv_total_compra_detalle) TextView total_compra;
    @BindView(R.id.tv_nombre) TextView tv_nombre;
    @BindView(R.id.tv_direccion) TextView tv_direccion;
    @BindView(R.id.tv_fecha) TextView tv_fecha;
    private DetalleCompraAdapter2 adapter;
    private ArrayList<ProductoSimple> carrito = new ArrayList<>();
    private EstablecimientoSimple local;
    private EventoSimple evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_compra);
        if (getIntent().hasExtra(Constantes.ARRAY_S_CARRITO)){
            Log.v("Amd", "hay carrito");
            carrito = (ArrayList<ProductoSimple>)getIntent().getSerializableExtra(Constantes.ARRAY_S_CARRITO);
        }
        if (getIntent().hasExtra(Constantes.OBJ_S_ESTABLECIMIENTO)){
            local = (EstablecimientoSimple)getIntent().getSerializableExtra(Constantes.OBJ_S_ESTABLECIMIENTO);
            tv_nombre.setText(local.getNombre());
            tv_direccion.setText(local.getDireccion());
            Date horaInicio = local.getFecha_inicio();
            Date horaFin = local.getFecha_fin();
            SimpleDateFormat sdfHora = new SimpleDateFormat("hh:mm a", new Locale("es", "pe"));
            if (horaInicio!=null) {
                tv_fecha.setText("Hora: " + sdfHora.format(horaInicio) + " - " + sdfHora.format(horaFin));
            }else {tv_fecha.setText("");}

        }else if (getIntent().hasExtra(Constantes.OBJ_S_EVENTO)){
            evento = (EventoSimple) getIntent().getSerializableExtra(Constantes.OBJ_S_EVENTO);
            tv_nombre.setText(evento.getNombre());
            tv_direccion.setText(evento.getDireccion());
            Date fecInicio = evento.getFecha_inicio();
            Date fecFin = evento.getFecha_fin();
            //SimpleDateFormat sdfHora = new SimpleDateFormat("hh:mm a", new Locale("es", "pe"));
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE d 'de' MMM yyyy ", new Locale("es", "pe"));
            tv_fecha.setText("Fecha: " + sdf.format(fecInicio) + " - " + sdf.format(fecFin));

        }

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
        adapter = new DetalleCompraAdapter2(this, carrito);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < carrito.size(); i++) {
            BigDecimal subtotal = carrito.get(i).getPrecio_allin().multiply(new BigDecimal(carrito.get(i).getCantidad()));
            total = total.add(subtotal);
        }
        total_compra.setText("S/. "+String.format("%.2f",(total.add(new BigDecimal(15)))));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
