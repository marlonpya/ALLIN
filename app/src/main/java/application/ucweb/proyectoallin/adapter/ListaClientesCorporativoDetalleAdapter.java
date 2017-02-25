package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.ItemSimple;
import application.ucweb.proyectoallin.modelparseable.UsuarioSimple;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 23/02/2017.
 */

public class ListaClientesCorporativoDetalleAdapter extends RecyclerView.Adapter<ListaClientesCorporativoDetalleAdapter.ViewHolder> {

    public static final String TAG = ListaClientesCorporativoDetalleAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<UsuarioSimple> usuarios;
    private LayoutInflater inflater;

    public ListaClientesCorporativoDetalleAdapter(Context context, ArrayList<UsuarioSimple> usuarios){
        this.context = context;
        this.usuarios = usuarios;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_clientes_corporativo_detalle, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UsuarioSimple usuario = usuarios.get(position);
        holder.nombre.setText(usuario.getNombre() + " " + usuario.getApellido());
        holder.dni.setText(String.valueOf(usuario.getDni()));
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_clientes_tv_nombre) TextView nombre;
        @BindView(R.id.row_clientes_tv_dni) TextView dni;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
