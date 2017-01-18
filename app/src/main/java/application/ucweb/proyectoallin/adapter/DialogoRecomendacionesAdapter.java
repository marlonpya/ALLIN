package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDAdapter;

import java.util.ArrayList;
import java.util.List;

import application.ucweb.proyectoallin.ListaDiscotecasActivity;
import application.ucweb.proyectoallin.ListaEventoActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.ItemSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 28/10/2016.
 */
public class DialogoRecomendacionesAdapter  extends RecyclerView.Adapter<DialogoRecomendacionesAdapter.SimpleListVH> implements MDAdapter {
    public static final String TAG = DialogoRecomendacionesAdapter.class.getSimpleName();

    public interface Callback {
        void onMaterialListItemSelected(MaterialDialog dialog, int index, ItemSimple item);
    }

    private MaterialDialog dialog;
    private List<ItemSimple> mItems;
    private DialogoDepartamentosAdapter.Callback mCallback;
    private Context context;

    public DialogoRecomendacionesAdapter(Context context, DialogoDepartamentosAdapter.Callback callback) {
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
    public DialogoRecomendacionesAdapter.SimpleListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DialogoRecomendacionesAdapter.SimpleListVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lista_dialogo_establecimiento, parent, false), this, context);
    }

    @Override
    public void onBindViewHolder(final DialogoRecomendacionesAdapter.SimpleListVH holder, int position) {
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
        @BindString(R.string.recomendados_lugar) String lugares;
        final DialogoRecomendacionesAdapter adapter;
        private Context context;

        public SimpleListVH(View itemView, DialogoRecomendacionesAdapter adapter, Context context) {
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
                if (item.getTitulo().equals(lugares)) {
                    Intent intent = new Intent(context.getApplicationContext(), ListaDiscotecasActivity.class);
                    intent.putExtra(Constantes.K_S_TITULO_TOOLBAR, item.getTitulo().toUpperCase());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context.getApplicationContext(), ListaEventoActivity.class);
                    intent.putExtra(Constantes.K_S_TITULO_TOOLBAR, item.getTitulo().toUpperCase());
                    context.startActivity(intent);
                }
                Log.d(TAG, "posicion->"+String.valueOf(getAdapterPosition()));
            }
        }
    }
}
