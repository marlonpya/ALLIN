package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import application.ucweb.proyectoallin.EncuestaActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.modelparseable.PreguntaSimple;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 16/02/2017.
 */

public class PreguntaAdapter extends RecyclerView.Adapter<PreguntaAdapter.ViewHolder>{
    public static final String TAG = PreguntaAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<PreguntaSimple> preguntas;
    private LayoutInflater inflater;

    public PreguntaAdapter(Context context, ArrayList<PreguntaSimple> preguntas){
        this.context = context;
        this.preguntas = preguntas;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_encuesta, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PreguntaSimple item = preguntas.get(position);
        holder.pregunta.setText(item.getPregunta());

        holder.si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EncuestaActivity.lista_preguntas.get(position).setRespuesta(1);
                Log.v("Amd", position+"");
                item.setRespondio(true);
                item.setRespuesta(1);
            }
        });

        holder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Amd", position+"");
                item.setRespondio(true);
                item.setRespuesta(0);
            }
        });

    }
    public ArrayList<PreguntaSimple> getArray() {
        return preguntas;
    }

    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_tv_pregunta_encuesta) TextView pregunta;
        @BindView(R.id.row_db_si) RadioButton si;
        @BindView(R.id.row_db_no) RadioButton no;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
