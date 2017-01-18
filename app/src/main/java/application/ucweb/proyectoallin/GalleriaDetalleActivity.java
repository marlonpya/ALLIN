package application.ucweb.proyectoallin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import application.ucweb.proyectoallin.adapter.GalleriaDetalleAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.ImagenParceable;
import application.ucweb.proyectoallin.util.Constantes;
import application.ucweb.proyectoallin.util.ExtraGalleriaPager;
import butterknife.BindView;

public class GalleriaDetalleActivity extends BaseActivity {
    private GalleriaDetalleAdapter adapter;
    private ArrayList<ImagenParceable> data = new ArrayList<>();
    private int posicion;
    @BindView(R.id.contenedor_detalle_galleria) ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_detalle);
        data        = getIntent().getParcelableArrayListExtra(Constantes.K_I_GALLERIA_DATA);
        posicion    = getIntent().getIntExtra(Constantes.K_I_GALLERIA_POSICION, 0);

        adapter = new GalleriaDetalleAdapter(getSupportFragmentManager(), data);
        mViewPager.setPageTransformer(true, new ExtraGalleriaPager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(posicion);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) { setTitle(data.get(position).getName()); }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        String name, url;
        int pos;
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_IMG_TITLE = "image_title";
        private static final String ARG_IMG_URL = "image_url";

        @Override
        public void setArguments(Bundle args) {
            super.setArguments(args);
            this.pos = args.getInt(ARG_SECTION_NUMBER);
            this.name = args.getString(ARG_IMG_TITLE);
            this.url = args.getString(ARG_IMG_URL);
        }

        public static PlaceholderFragment newInstance(int sectionNumber, String name, String url) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_IMG_TITLE, name);
            args.putString(ARG_IMG_URL, url);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            final ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);
            Glide.with(getActivity()).load(url).thumbnail(0.1f).into(imageView);
            return rootView;
        }
    }

}
