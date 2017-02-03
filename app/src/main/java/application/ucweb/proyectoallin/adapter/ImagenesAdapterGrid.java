package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import application.ucweb.proyectoallin.GalleriaDetalleActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.Imagen;
import application.ucweb.proyectoallin.model.ImagenParceable;
import application.ucweb.proyectoallin.model.ItemSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 01/02/2017.
 */

public class ImagenesAdapterGrid extends RecyclerView.Adapter<ImagenesAdapterGrid.ViewHolder> {

    private Context context;
    private ArrayList<ItemSimple> imagenes;
    private LayoutInflater inflater;

    public ImagenesAdapterGrid(Context context, ArrayList<ItemSimple> imagenes){
        this.context = context;
        this.imagenes = imagenes;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.row_imagen_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ItemSimple itemImagen = imagenes.get(position);
        final int posicion = position;
        Glide.with(context).load(itemImagen.getTitulo())
                .thumbnail(0.5f)
                .override(200, 200)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imagen);
        final ArrayList<ImagenParceable> data = new ArrayList<>();

        for (int j = 0; j < imagenes.size(); j++) {
            ImagenParceable imageModel = new ImagenParceable();
            imageModel.setName("Image " + j);
            imageModel.setUrl(imagenes.get(j).getTitulo());
            data.add(imageModel);
            //Log.d(TAG, String.valueOf(j)+data.toString());
        }

        holder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GalleriaDetalleActivity.class);
                intent.putExtra(Constantes.K_I_GALLERIA_DATA, data);
                intent.putExtra(Constantes.K_I_GALLERIA_POSICION, posicion);
                context.startActivity(intent);
                //Log.d(TAG, data.toString());
                //Log.d(TAG, String.valueOf(posicion));
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagenes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_iv_imagen_grid) ImageView imagen;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
