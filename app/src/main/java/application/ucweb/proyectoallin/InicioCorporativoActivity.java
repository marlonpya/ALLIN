package application.ucweb.proyectoallin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class InicioCorporativoActivity extends BaseActivity {
    @BindView(R.id.toolbar_inicio_corporativo) Toolbar toolbar;
    @BindView(R.id.idiv_layout_inicio_corporativo) ImageView fondo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_corporativo);
        iniciarLayout();
    }

    @OnClick(R.id.ll_btn_ingresar_coporativo)
    public void ingresar_coporativo() {
        startActivity(new Intent(this, MenuCorporativo.class));
    }

    private void iniciarLayout() {
        setToolbarSonB(toolbar, this);
        usarGlide(this, R.drawable.fondo_allin, fondo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
