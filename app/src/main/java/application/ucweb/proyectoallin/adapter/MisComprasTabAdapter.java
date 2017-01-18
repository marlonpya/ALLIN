package application.ucweb.proyectoallin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import application.ucweb.proyectoallin.fragment.MisComprasHistorialFragment;
import application.ucweb.proyectoallin.fragment.MisComprasVerFragment;

/**
 * Created by ucweb02 on 26/09/2016.
 */
public class MisComprasTabAdapter extends FragmentStatePagerAdapter {
    private int cantidad;

    public MisComprasTabAdapter(FragmentManager fm, int cantidad) {
        super(fm);
        this.cantidad = cantidad;
    }

    @Override
    public Fragment getItem(int posicion) {
        switch (posicion) {
            case 0: return new MisComprasVerFragment();
            case 1: return new MisComprasHistorialFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return cantidad;
    }
}
