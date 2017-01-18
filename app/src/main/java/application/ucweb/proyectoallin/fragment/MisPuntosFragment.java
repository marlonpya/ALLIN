package application.ucweb.proyectoallin.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import application.ucweb.proyectoallin.EncuestaActivity;
import application.ucweb.proyectoallin.ListaCompraActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.adapter.CartaEstablecimientoRealmAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Producto;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class MisPuntosFragment extends Fragment {
    public static final String TAG = MisPuntosFragment.class.getSimpleName();
    @BindView(R.id.lista_puntos) RealmRecyclerView rvlista_puntos;
    @BindView(R.id.spCarta)Spinner spCarta;
    @BindView(R.id.tvResultadoCarta)TextView tvResultadoCarta;
    @BindView(R.id.tvDescripcionToolbar)TextView tvTituloCanjearPF;
    @BindView(R.id.tv_cantidad_productos_carrito) TextView cantidad_productos_carrito;
    @BindString(R.string.dialogo_antes_de_canjear) String texto_canje;
    private Realm realm;
    private CartaEstablecimientoRealmAdapter adapter;
    private RealmResults<Producto> lista_productos;

    public MisPuntosFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_puntos, container, false);
        ButterKnife.bind(this, view);

        iniciarLayout();
        if (Producto.getUltimoId() == 0) Producto.cargarData();
        iniciarRRV();
        dialogoAntesCanjear();
        return view;
    }

    @OnClick(R.id.iv_ir_a_carrito_puntos)
    public void irACarritoPuntos() {
        startActivity(new Intent(getActivity().getApplicationContext(), ListaCompraActivity.class));
    }

    private void dialogoAntesCanjear() {
        new MaterialDialog.Builder(getActivity())
                .title(getString(R.string.titulo_antes_canjear))
                .content(getString(R.string.dialogo_antes_de_canjear))
                .positiveText(getString(R.string.aceptar))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(getActivity().getApplicationContext(), EncuestaActivity.class));
                    }
                })
                .show();
    }

    private void iniciarRRV() {
        realm = Realm.getDefaultInstance();
        lista_productos = realm.where(Producto.class).equalTo(Producto.PROMOCION, true).findAll();
        adapter = new CartaEstablecimientoRealmAdapter(getActivity(), lista_productos, true, true, cantidad_productos_carrito);
        rvlista_puntos.setAdapter(adapter);
        int cantidad_encontrados = lista_productos.size();
        tvResultadoCarta.setText(String.valueOf(cantidad_encontrados + " encontrados"));
        int cantidad_carrito = realm.where(Producto.class).equalTo(Producto.A_CARRITO, true).findAll().size();
        cantidad_productos_carrito.setText(String.valueOf(cantidad_carrito));
        adapter.notifyDataSetChanged();
    }

    private void iniciarLayout() {
        tvTituloCanjearPF.setText("MIS PUNTOS ALLIN");
        BaseActivity.setSpinnerRosa(getContext(), spCarta, R.array.arrayCarta);
    }

}
