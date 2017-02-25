package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import application.ucweb.proyectoallin.ListaClientesCorporativoDetalleActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.ItemSimple;
import application.ucweb.proyectoallin.modelparseable.PreguntaSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 23/02/2017.
 */

public class ListaClientesCorporativoAdapter extends RecyclerView.Adapter<ListaClientesCorporativoAdapter.ViewHolder> {
    public static final String TAG = ListaClientesCorporativoAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<ItemSimple> fechas;
    private LayoutInflater inflater;

    public ListaClientesCorporativoAdapter(Context context, ArrayList<ItemSimple> fechas){
        this.context = context;
        this.fechas = fechas;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_clientes_corporativo, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ItemSimple fecha = fechas.get(position);
        holder.fecha.setText(fecha.getTitulo());
        holder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ListaClientesCorporativoDetalleActivity.class)
                        .putExtra(Constantes.L_ID_ENLISTA, fecha.getTitulo()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return fechas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_clientes_corporativo_tv_fecha) TextView fecha;
        @BindView(R.id.row_clientes_corporativo_btn_verlista) LinearLayout boton;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
