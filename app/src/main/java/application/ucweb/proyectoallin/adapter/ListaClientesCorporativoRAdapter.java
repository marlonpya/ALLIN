package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.Cliente;
import application.ucweb.proyectoallin.model.EnLista;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 31/10/2016.
 */
public class ListaClientesCorporativoRAdapter extends RealmBasedRecyclerViewAdapter<EnLista, ListaClientesCorporativoRAdapter.ViewHolder> {
    public ListaClientesCorporativoRAdapter(
            Context context,
            RealmResults<EnLista> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.row_clientes_corporativo, viewGroup, false));
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {
        final EnLista item = realmResults.get(i);
        viewHolder.fecha.setText(item.getFecha());
        /*viewHolder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), ListaClientesCorporativoDetalleActivity.class)
                .putExtra(Constantes.L_ID_ENLISTA, item.getFecha()));
            }
        });*/
    }

    public class ViewHolder extends RealmViewHolder {
        @BindView(R.id.row_clientes_corporativo_tv_fecha) TextView fecha;
        @BindView(R.id.row_clientes_corporativo_btn_verlista) LinearLayout boton;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
