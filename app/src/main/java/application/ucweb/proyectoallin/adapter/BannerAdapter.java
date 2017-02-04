package application.ucweb.proyectoallin.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.model.Banner;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by ucweb02 on 07/11/2016.
 */
public class BannerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Banner> banners;

    public BannerAdapter(FragmentManager fm, ArrayList<Banner> banners) {
        super(fm);
        this.banners = banners;
    }

    @Override
    public Fragment getItem(int position) {
        RowBannerAdapter pageBannerFragment = new RowBannAdapter();
        Bundle bundle = new Bundle();
        bundle.putString("bannerResID", banners.get(position).getUrl());
        pageBannerFragment.setArguments(bundle);
        return pageBannerFragment;
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    public static class RowBannAdapter extends RowBannerAdapter{

        @BindView(R.id.imgBanner)
        ImageView imgBanner;

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
}
