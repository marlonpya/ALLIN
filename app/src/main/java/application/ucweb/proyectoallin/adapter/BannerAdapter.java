package application.ucweb.proyectoallin.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import application.ucweb.proyectoallin.R;

/**
 * Created by ucweb02 on 07/11/2016.
 */
public class BannerAdapter extends FragmentStatePagerAdapter {

    private static final int CANTIDAD_BANNERS = 3;

    private final int BANNERS_RES_ID[] = new int[]{
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
    };

    public BannerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        RowBannerAdapter pageBannerFragment = new RowBannerAdapter();
        Bundle bundle = new Bundle();
        bundle.putInt("bannerResID", BANNERS_RES_ID[position]);
        pageBannerFragment.setArguments(bundle);
        return pageBannerFragment;
    }

    @Override
    public int getCount() {
        return CANTIDAD_BANNERS;
    }
}
