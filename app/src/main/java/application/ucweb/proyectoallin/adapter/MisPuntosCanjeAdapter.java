package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.fragment.MisPuntosFragment;
import application.ucweb.proyectoallin.modelparseable.ProductoSimple;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 07/02/2017.
 */

public class MisPuntosCanjeAdapter extends RecyclerView.Adapter<MisPuntosCanjeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ProductoSimple> promociones;
    private LayoutInflater inflater;
    private TextView textView;

    public MisPuntosCanjeAdapter(Context context, ArrayList<ProductoSimple> promociones, TextView textView) {
        this.context = context;
        this.promociones = promociones;
        this.inflater = LayoutInflater.from(context);
        this.textView = textView;
    }

    @Override
    public MisPuntosCanjeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_objeto_canje, parent, false));
    }

    @Override
    public void onBindViewHolder(MisPuntosCanjeAdapter.ViewHolder holder, int position) {
        final ProductoSimple item = promociones.get(position);
        holder.promocion.setText(item.getNombre());
        holder.puntos.setText(item.getPrecio_puntos() + " pts");
        textView.setText(getTotal() + " pts");
        if (item.getIdEvento()!=0){
            holder.tipo.setText("@Evento: " + item.getNombre_evento());
        }else if (item.getIdLocal()!=0){
            holder.tipo.setText("@Local: " + item.getNombre_local());
        }
    }

    public int getTotal(){
        int total = 0;
        for (int i = 0; i < promociones.size(); i++) {
            total += promociones.get(i).getPrecio_puntos();
        }
        return total;
    }

    @Override
    public int getItemCount() {
        return promociones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_promocion_nombre) TextView promocion;
        @BindView(R.id.tv_puntos_costo) TextView puntos;
        @BindView(R.id.tv_tipo) TextView tipo;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
