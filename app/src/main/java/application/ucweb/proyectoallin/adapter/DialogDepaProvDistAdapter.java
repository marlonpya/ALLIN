package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.ItemSimple;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 23/01/2017.
 */

public class DialogDepaProvDistAdapter extends ArrayAdapter<ItemSimple>{
    private Context context;
    private ArrayList<ItemSimple> itemSimples;
    private LayoutInflater inflater;

    public DialogDepaProvDistAdapter(Context context, ArrayList<ItemSimple> itemSimples){
        super(context, R.layout.row_depaprovdist_dialog_adapter, itemSimples);
        this.context=context;
        this.itemSimples=itemSimples;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DialogDepaProvDistAdapter.ViewHolder viewHolder = null;
        if (convertView != null) viewHolder = (DialogDepaProvDistAdapter.ViewHolder) convertView.getTag();
        else {
            convertView = inflater.inflate(R.layout.row_depaprovdist_dialog_adapter, parent, false);
            viewHolder = new DialogDepaProvDistAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        BaseActivity.usarGlide(context, itemSimples.get(position).getIcono(), viewHolder.imagen);
        viewHolder.texto.setText(itemSimples.get(position).getTitulo());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_row_dialog_adapter_imagen) ImageView imagen;
        @BindView(R.id.tv_row_dialog_adapter_texto) TextView texto;
        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
