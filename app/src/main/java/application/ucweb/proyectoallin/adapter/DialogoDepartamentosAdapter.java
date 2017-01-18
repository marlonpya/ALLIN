package application.ucweb.proyectoallin.adapter;

import android.content.Context;
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

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.fragment.MenuFragment;
import application.ucweb.proyectoallin.model.ItemSimple;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 27/10/2016.
 */
public class DialogoDepartamentosAdapter extends RecyclerView.Adapter<DialogoDepartamentosAdapter.SimpleListVH> implements MDAdapter {

    public interface Callback {
        void onMaterialListItemSelected(MaterialDialog dialog, int index, ItemSimple item);
    }

    private MaterialDialog dialog;
    private List<ItemSimple> mItems;
    private Callback mCallback;
    private Context context;

    public DialogoDepartamentosAdapter(Context context, Callback callback) {
        mItems = new ArrayList<>(4);
        mCallback = callback;
        this.context = context;
    }

    public void add(ItemSimple item) {
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public ItemSimple getItem(int index) {
        return mItems.get(index);
    }

    @Override
    public void setDialog(MaterialDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public DialogoDepartamentosAdapter.SimpleListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DialogoDepartamentosAdapter.SimpleListVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lista_dialogo_departamentos, parent, false), this, context);
    }

    @Override
    public void onBindViewHolder(final DialogoDepartamentosAdapter.SimpleListVH holder, int position) {
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

        @BindView(R.id.iv_row_lista_dialogo_imagen_departamentos) ImageView imagen;
        @BindView(R.id.tv_row_lista_dialogo_titulo_departamentos) TextView titulo;
        @BindString(R.string.busqueda_distrito) String b_distrito;
        @BindString(R.string.busqueda_tipomusica) String b_tipo_musica;
        @BindString(R.string.busqueda_calendario) String b_calendario;
        @BindString(R.string.busqueda_gps) String b_gps;
        /*@BindView(R.id.cl_btn_lista_dialogo) ConstraintLayout boton;*/
        final DialogoDepartamentosAdapter adapter;
        private Context context;

        public SimpleListVH(View itemView, DialogoDepartamentosAdapter adapter, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.adapter = adapter;
            this.context = context;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (adapter.mCallback != null) {
                adapter.mCallback.onMaterialListItemSelected(adapter.dialog, getAdapterPosition(), adapter.getItem(getAdapterPosition()));
                ItemSimple item = adapter.getItem(getAdapterPosition());
                if (item.getTitulo() != null) {
                    MenuFragment.intentAListaDRKER(item.getTitulo(), context);
                }
            }
        }
    }
}