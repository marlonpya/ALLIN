package application.ucweb.proyectoallin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.adapter.MisComprasVerAdapter;
import application.ucweb.proyectoallin.model.Compra;
import application.ucweb.proyectoallin.model.Producto;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class MisComprasVerFragment extends Fragment {
    public static final String TAG = MisComprasVerFragment.class.getSimpleName();
    @BindView(R.id.lista_mis_compras) RealmRecyclerView lista_mis_compras;
    private Realm realm;
    private MisComprasVerAdapter adapter;
    private RealmResults<Compra> lista_compras;

    public MisComprasVerFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_mis_compras, container, false);
        ButterKnife.bind(this, view);

        if (Compra.getUltimoId() == 0) { cargar(); }
        iniciarRealmRV();
        return view;
    }

    private void iniciarRealmRV() {
        realm = Realm.getDefaultInstance();
        lista_compras = realm.where(Compra.class).findAll();
        adapter = new MisComprasVerAdapter(getActivity(), lista_compras, true, true);
        lista_mis_compras.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void cargar() {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Producto producto1 = realm.createObject(Producto.class, Producto.getUltimoId());
        producto1.setDescripcion_producto("Six de 6 latas de Red Bull");
        producto1.setA_carrito(false);
        realm.copyToRealm(producto1);
        realm.commitTransaction();

        realm.beginTransaction();
        Producto producto2 = realm.createObject(Producto.class, Producto.getUltimoId());
        producto2.setDescripcion_producto("Balde de 10 cervezas Milers");
        producto1.setA_carrito(false);
        realm.copyToRealm(producto2);
        realm.commitTransaction();

        for (int i = 0; i < 10; i++) {
            realm.beginTransaction();
            Compra compra = realm.createObject(Compra.class, Compra.getUltimoId());
            compra.setFecha_compra("07.05.2015");
            compra.setFecha_vencimiento("07.09.2015");
            compra.setEvento_compra("SALSA VS REGUETON");
            compra.setDrkere_compra("DISCOTECA NIGHT LIFE");
            compra.setPrecio_total(270);
            compra.getLista_productos().add(producto1);
            compra.getLista_productos().add(producto2);
            realm.copyToRealm(compra);
            realm.commitTransaction();
            Log.d(TAG, compra.toString());
        }
        realm.close();
    }

}
