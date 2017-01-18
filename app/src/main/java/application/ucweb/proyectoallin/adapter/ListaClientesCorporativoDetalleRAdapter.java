package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.Cliente;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 31/10/2016.
 */
public class ListaClientesCorporativoDetalleRAdapter extends RealmBasedRecyclerViewAdapter<Cliente, ListaClientesCorporativoDetalleRAdapter.ViewHolder> {
    public ListaClientesCorporativoDetalleRAdapter(
            Context context,
            RealmResults<Cliente> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ListaClientesCorporativoDetalleRAdapter.ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.row_clientes_corporativo_detalle, viewGroup, false));
    }

    @Override
    public void onBindRealmViewHolder(ListaClientesCorporativoDetalleRAdapter.ViewHolder viewHolder, int i) {
        final Cliente item = realmResults.get(i);
        viewHolder.nombre.setText(item.getNombre());
        viewHolder.dni.setText(item.getDni());

    }

    public class ViewHolder extends RealmViewHolder {
        @BindView(R.id.row_clientes_tv_nombre) TextView nombre;
        @BindView(R.id.row_clientes_tv_dni) TextView dni;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
