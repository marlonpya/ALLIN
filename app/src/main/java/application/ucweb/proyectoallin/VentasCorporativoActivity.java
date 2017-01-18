package application.ucweb.proyectoallin;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import application.ucweb.proyectoallin.adapter.MisComprasTabAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import butterknife.BindView;

public class VentasCorporativoActivity extends BaseActivity {
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.tab_layout_admi)TabLayout tabLayout;
    @BindView(R.id.view_pager)ViewPager pager;
    @BindView(R.id.idiv_layout_ventas_administrador)ImageView fondo;
    private MisComprasTabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas_corporativo);
        iniciarLayout();

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        setupTabLayout();
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new MisComprasTabAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void iniciarLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
        usarGlide(this, R.drawable.fondo_allin_desenfoquev3, fondo);
    }

    private void setupTabLayout() {
        TextView customTab1 = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.textview_tablayout_tab_accent, null);
        TextView customTab2 = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.textview_tablayout_tab_accent, null);
        customTab1.setText(getString(R.string.ventas_realizadas).toUpperCase());
        customTab1.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icono_compras_tabs, 0, 0);
        customTab2.setText(R.string.historial);
        customTab2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.icono_historial_tabs, 0, 0);
        tabLayout.getTabAt(0).setCustomView(customTab1);
        tabLayout.getTabAt(1).setCustomView(customTab2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
