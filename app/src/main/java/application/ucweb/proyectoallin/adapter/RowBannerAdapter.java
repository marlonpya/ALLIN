package application.ucweb.proyectoallin.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ucweb02 on 07/11/2016.
 */
public class RowBannerAdapter extends Fragment {
    @BindView(R.id.imgBanner) ImageView imgBanner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.row_banner, container, false);
        ButterKnife.bind(this, view);
        Glide.with(this)
                .load(getArguments().getString("bannerResID"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .skipMemoryCache(true)
                .into(imgBanner);
        return view;
    }
}
