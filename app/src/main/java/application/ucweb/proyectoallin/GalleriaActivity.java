package application.ucweb.proyectoallin;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import application.ucweb.proyectoallin.adapter.ImagenesRealmAdapterGrid;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Imagen;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class GalleriaActivity extends BaseActivity {
    public static final String TAG = GalleriaActivity.class.getSimpleName();
    @BindView(R.id.rrv_lista_galleria) RealmRecyclerView rrv_lista_galleria;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.tvDescripcionToolbar)TextView tvTituloGallery;
    @BindView(R.id.idiv_layout_gallery_discoteca)ImageView ivFondoGallery;
    private Realm realm;
    private ImagenesRealmAdapterGrid adapter;
    private RealmResults<Imagen> lista_imagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galleria);
        iniciarLayout();
        if (Imagen.getUltimoId() == 0) cargarData();
        cargarRealmListas();
    }

    private void cargarRealmListas() {
        realm = Realm.getDefaultInstance();
        lista_imagenes = realm.where(Imagen.class).findAll();
        adapter = new ImagenesRealmAdapterGrid(this, lista_imagenes, true, true);
        rrv_lista_galleria.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void cargarData() {
        final String IMGS[] = {
                "https://images.unsplash.com/photo-1444090542259-0af8fa96557e?q=80&fm=jpg&w=1080&fit=max&s=4b703b77b42e067f949d14581f35019b",
                "https://images.unsplash.com/photo-1439546743462-802cabef8e97?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1441155472722-d17942a2b76a?q=80&fm=jpg&w=1080&fit=max&s=80cb5dbcf01265bb81c5e8380e4f5cc1",
                "https://images.unsplash.com/photo-1437651025703-2858c944e3eb?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1431538510849-b719825bf08b?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1434873740857-1bc5653afda8?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1439396087961-98bc12c21176?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1433616174899-f847df236857?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1438480478735-3234e63615bb?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1438027316524-6078d503224b?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300"
        };
        realm = Realm.getDefaultInstance();
        for (String IMG : IMGS) {
            realm.beginTransaction();
            Imagen imagen = realm.createObject(Imagen.class);
            imagen.setId(Imagen.getUltimoId());
            imagen.setRuta(IMG);
            realm.copyToRealm(imagen);
            realm.commitTransaction();
            Log.d(TAG, imagen.toString());
        }
        realm.close();
    }

    private void iniciarLayout() {
        BaseActivity.setToolbarSon(toolbar, this, icono_toolbar);
        tvTituloGallery.setText(R.string.galeria);
        setFondoActivity(this, ivFondoGallery);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
