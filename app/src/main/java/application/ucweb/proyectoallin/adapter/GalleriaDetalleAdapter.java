package application.ucweb.proyectoallin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import application.ucweb.proyectoallin.GalleriaDetalleActivity;
import application.ucweb.proyectoallin.model.ImagenParceable;

/**
 * Created by ucweb02 on 30/09/2016.
 */
public class GalleriaDetalleAdapter extends FragmentPagerAdapter {
    public ArrayList<ImagenParceable> data = new ArrayList<>();

    public GalleriaDetalleAdapter(FragmentManager fm, ArrayList<ImagenParceable> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return GalleriaDetalleActivity.PlaceholderFragment.newInstance(position, data.get(position).getName(), data.get(position).getUrl());
    }

    @Override
    public int getCount() { return data.size(); }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getName();
    }

}
