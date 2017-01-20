package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.ItemSimple;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static application.ucweb.proyectoallin.fragment.MenuFragment.intentAListaDRKER;

/**
 * Created by ucweb02 on 27/10/2016.
 */

public class DialogoTipoMusicaAdapter extends RecyclerView.Adapter<DialogoTipoMusicaAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ItemSimple> lista;

    public DialogoTipoMusicaAdapter(Context context, ArrayList<ItemSimple> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_lista_dialogo_establecimiento, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ItemSimple item = lista.get(position);
        BaseActivity.usarGlide(context, item.getIcono(), holder.imagen);
        holder.titulo.setText(item.getTitulo());
        holder.boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Amd", "DialogTipoMusicAdap");
                intentAListaDRKER(item.getTitulo(), context, -1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_row_lista_dialogo_imagen) ImageView imagen;
        @BindView(R.id.tv_row_lista_dialogo_titulo) TextView titulo;
        @BindView(R.id.cl_btn_lista_dialogo) ConstraintLayout boton;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
