package application.ucweb.proyectoallin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.adapter.MisComprasHistorialAdapter;
import application.ucweb.proyectoallin.model.Compra;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class MisComprasHistorialFragment extends Fragment {
    public static final String TAG = MisComprasHistorialFragment.class.getSimpleName();
    @BindView(R.id.lista_historial) RealmRecyclerView lista_historial;
    private Realm realm;
    private MisComprasHistorialAdapter adapter;
    private RealmResults<Compra> lista_compras;

    public MisComprasHistorialFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_compras_historial, container, false);
        ButterKnife.bind(this, view);

        iniciarRealmRV();
        return view;
    }

    private void iniciarRealmRV() {
        realm = Realm.getDefaultInstance();
        lista_compras = realm.where(Compra.class).findAll();
        adapter = new MisComprasHistorialAdapter(getActivity(), lista_compras, true, true);
        lista_historial.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
