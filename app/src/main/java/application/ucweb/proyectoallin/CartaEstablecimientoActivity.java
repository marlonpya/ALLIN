package application.ucweb.proyectoallin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import application.ucweb.proyectoallin.adapter.ProductoRealmAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Producto;
import butterknife.BindString;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class CartaEstablecimientoActivity extends BaseActivity {
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.lista_puntos) RealmRecyclerView rvlista_puntos;
    @BindView(R.id.spCarta)Spinner spCarta;
    @BindView(R.id.tvDescripcionToolbar)TextView tvTituloCartaDiscoteca;
    @BindView(R.id.tvResultadoCartaDiscoteca)TextView tvResultadoCartaDiscoteca;
    @BindString(R.string.dialogo_condiciones_compra) String condiciones_compra;
    public static final String TAG = CartaEstablecimientoActivity.class.getSimpleName();
    private Realm realm;
    private ProductoRealmAdapter adapter;
    private RealmResults<Producto> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta_establecimiento);
        iniciarLayout();
        tvTituloCartaDiscoteca.setText(R.string.carta_discoteca);
        if (Producto.getUltimoId() == 0) { Producto.cargarData(); Producto.cargarData(); }
        iniciarRRV();
        dialogo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciarRRV();
    }

    private void dialogo() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.carta_discoteca))
                .content("*No válido para eventos especiales (VER CALENDARIO).\n*Incluye entrada libre para el cliente.")
                .iconRes(R.drawable.iconoalertafucsia)
                .positiveText(getString(R.string.aceptar))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();;
    }

    private void iniciarRRV() {
        realm = Realm.getDefaultInstance();
        lista = realm.where(Producto.class).findAll();
        Log.d(TAG, lista.toString());
        adapter = new ProductoRealmAdapter(this, lista, true, true);
        rvlista_puntos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /*public static void cargarDataProductos() {
        Realm realm = Realm.getDefaultInstance();
        String[] arrayString1 = new String[]{"Botella Chivas Regal" ,"Balde de 10 cervezas Miler" ,"1 Botella Cuatro Gallos Especial" ,"1 Gran Russkaya"};
        int[] arrayInt = new int[]{R.drawable.imagen_chivas, R.drawable.imagen_kit_miler, R.drawable.imagen_cuatro_gallos, R.drawable.imagen_russkaya};
        for (int i = 0; i < arrayString1.length; i++) {
            realm.beginTransaction();
            Producto producto = realm.createObject(Producto.class);
            producto.setId(Producto.getUltimoId());
            producto.setDescripcion_producto(arrayString1[i]);
            producto.setImagen_producto(arrayInt[i]);
            producto.setPrecio_normal(350);
            producto.setPrecio_allin(262.5);
            realm.copyToRealm(producto);
            realm.commitTransaction();
            realm.close();
            Log.d(TAG, producto.toString());
        }
    }*/

    private void iniciarLayout() {
        setSpinnerRosa(this, spCarta, R.array.arrayCarta);
        setToolbarSon(toolbar, this, icono_toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
