package application.ucweb.proyectoallin.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.adapter.MisComprasTabAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MisComprasTabFragment extends Fragment {
    public static final String TAG = MisComprasTabFragment.class.getSimpleName();
    @BindView(R.id.tab_layout) TabLayout tab_layout;
    @BindView(R.id.pager)ViewPager pager;
    private MisComprasTabAdapter adapter;

    public MisComprasTabFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_compras, container, false);
        ButterKnife.bind(this, view);

        tab_layout.addTab(tab_layout.newTab());
        tab_layout.addTab(tab_layout.newTab());
        setupTabLayout();
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new MisComprasTabAdapter(getFragmentManager(),tab_layout.getTabCount());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
        return view;
    }

    private void setupTabLayout() {
        TextView customTab1 = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.textview_tablayout_tab_accent, null);
        TextView customTab2 = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.textview_tablayout_tab_accent, null);
        customTab1.setText(getString(R.string.mis_compras).toUpperCase());
        customTab1.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icono_compras_tabs, 0, 0);
        customTab2.setText(R.string.historial);
        customTab2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icono_historial_tabs, 0, 0);
        tab_layout.getTabAt(0).setCustomView(customTab1);
        tab_layout.getTabAt(1).setCustomView(customTab2);
    }

}
