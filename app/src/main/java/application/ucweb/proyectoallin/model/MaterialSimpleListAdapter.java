package application.ucweb.proyectoallin.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDAdapter;

import java.util.ArrayList;
import java.util.List;

import application.ucweb.proyectoallin.CalendarioActivity;
import application.ucweb.proyectoallin.MapaActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.fragment.MenuFragment;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 27/10/2016.
 */
public class MaterialSimpleListAdapter extends RecyclerView.Adapter<MaterialSimpleListAdapter.SimpleListVH> implements MDAdapter {

    public interface Callback {
        void onMaterialListItemSelected(MaterialDialog dialog, int index, ItemSimple item);
    }

    private MaterialDialog dialog;
    private List<ItemSimple> mItems;
    private Callback mCallback;
    private Context context;

    public MaterialSimpleListAdapter(Context context, Callback callback) {
        mItems = new ArrayList<>(4);
        mCallback = callback;
        this.context = context;
    }

    public void add(ItemSimple item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public ItemSimple getItem(int index) {
        return mItems.get(index);
    }

    @Override
    public void setDialog(MaterialDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public SimpleListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleListVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lista_dialogo_establecimiento, parent, false), this, context);
    }

    @Override
    public void onBindViewHolder(final SimpleListVH holder, int position) {
        if (dialog != null) {
            final ItemSimple item = mItems.get(position);
            if (item.getIcono() != -1) {
                holder.imagen.setBackgroundResource(item.getIcono());
            } else {
                holder.imagen.setVisibility(View.GONE);
            }
            holder.titulo.setTextColor(dialog.getBuilder().getItemColor());
            holder.titulo.setText(item.getTitulo());
            dialog.setTypeface(holder.titulo, dialog.getBuilder().getRegularFont());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class SimpleListVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_row_lista_dialogo_imagen) ImageView imagen;
        @BindView(R.id.tv_row_lista_dialogo_titulo) TextView titulo;
        @BindString(R.string.busqueda_gps) String b_gps;
        @BindString(R.string.busqueda_distrito) String b_distrito;
        @BindString(R.string.busqueda_tipomusica) String b_tipo_musica;
        @BindString(R.string.busqueda_calendario) String b_calendario;
        final MaterialSimpleListAdapter adapter;
        private Context context;

        public SimpleListVH(View itemView, MaterialSimpleListAdapter adapter, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.adapter = adapter;
            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (adapter.mCallback != null){
                adapter.mCallback.onMaterialListItemSelected(adapter.dialog, getAdapterPosition(), adapter.getItem(getAdapterPosition()));
                ItemSimple item = adapter.getItem(getAdapterPosition());
                if (item.getTitulo().equals(b_distrito)) {
                    //MenuFragment.dialogoListaDepartamentos2(context);
                } else if (item.getTitulo().equals(b_tipo_musica)) {
                    //MenuFragment.dialogoTipoDeMusica2(context, context.getString(R.string.elija_busquedad));
                } else if(item.getTitulo().equals(b_calendario)) {
                    context.startActivity(new Intent(context, CalendarioActivity.class)
                    .putExtra(Constantes.BUSQUEDA_CALENDARIO, Constantes.I_DISCOTECAS));
                } else if(item.getTitulo().equals(b_gps)) {
                    context.startActivity(new Intent(context, MapaActivity.class));
                }
            }
        }
    }
}