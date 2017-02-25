package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import application.ucweb.proyectoallin.ListaCanjeCorporativo;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.ItemSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 24/02/2017.
 */

public class ListaCanjeCorporativoAdapter extends RecyclerView.Adapter<ListaCanjeCorporativoAdapter.ViewHolder> {

    public static final String TAG = ListaCanjeCorporativoAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<ItemSimple> detalles;
    private LayoutInflater inflater;

    public ListaCanjeCorporativoAdapter(Context context, ArrayList<ItemSimple> detalles){
        this.context = context;
        this.detalles = detalles;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_canje_corporativo_detalle, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemSimple detalle = detalles.get(position);
        holder.tv_nombre_producto.setText(detalle.getTitulo());
        if (detalle.getTipo()==0){
            holder.btn_claim.setEnabled(true);
            holder.tv_nombre_producto.setTextColor(ContextCompat.getColor(context, R.color.colorblanco));
            holder.btn_claim.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_dark));
            detalle.setStatus(false);
            holder.btn_claim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "" + detalle.getId(), Toast.LENGTH_SHORT).show();
                    //Log.v("Amd", ""+detalle.getId());
                    if (!detalle.isStatus()){
                        holder.btn_claim.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                        ListaCanjeCorporativo.id_detalle_a_canjear.add(detalle.getId());
                        Log.v("Amd", "Size: " + ListaCanjeCorporativo.id_detalle_a_canjear.size());
                        detalle.setStatus(true);
                    }else{
                        holder.btn_claim.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_dark));
                        for (int i = 0; i < ListaCanjeCorporativo.id_detalle_a_canjear.size(); i++) {
                            if (ListaCanjeCorporativo.id_detalle_a_canjear.get(i)==detalle.getId()){
                                ListaCanjeCorporativo.id_detalle_a_canjear.remove(i);
                            }
                        }
                        Log.v("Amd", "Size: " + ListaCanjeCorporativo.id_detalle_a_canjear.size());
                        detalle.setStatus(false);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return detalles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_nombre_producto) TextView tv_nombre_producto;
        @BindView(R.id.btn_claim) Button btn_claim;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
