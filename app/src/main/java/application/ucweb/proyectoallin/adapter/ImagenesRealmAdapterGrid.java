package application.ucweb.proyectoallin.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import application.ucweb.proyectoallin.GalleriaDetalleActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.Imagen;
import application.ucweb.proyectoallin.model.ImagenParceable;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by ucweb02 on 29/09/2016.
 */
public class ImagenesRealmAdapterGrid extends RealmBasedRecyclerViewAdapter<Imagen, ImagenesRealmAdapterGrid.ViewHolder> {
    public static final String TAG = ImagenesRealmAdapterGrid.class.getSimpleName();

    public ImagenesRealmAdapterGrid(
            Context context,
            RealmResults<Imagen> realmResults,
            boolean automaticUpdate,
            boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.row_imagen_grid, viewGroup, false));
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {
        final Imagen item = realmResults.get(i);
        final int posicion = i;
        Glide.with(getContext()).load(item.getRuta())
                .thumbnail(0.5f)
                .override(200, 200)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imagen);
        final ArrayList<ImagenParceable> data = new ArrayList<>();

        for (int j = 0; j < realmResults.size(); j++) {
            ImagenParceable imageModel = new ImagenParceable();
            imageModel.setName("Image " + j);
            imageModel.setUrl(realmResults.get(j).getRuta());
            data.add(imageModel);
            Log.d(TAG, String.valueOf(j)+data.toString());
        }

        viewHolder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GalleriaDetalleActivity.class);
                intent.putExtra(Constantes.K_I_GALLERIA_DATA, data);
                intent.putExtra(Constantes.K_I_GALLERIA_POSICION, posicion);
                getContext().startActivity(intent);
                Log.d(TAG, data.toString());
                Log.d(TAG, String.valueOf(posicion));
            }
        });
    }

    public class ViewHolder extends RealmViewHolder {
        @BindView(R.id.row_iv_imagen_grid) ImageView imagen;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
