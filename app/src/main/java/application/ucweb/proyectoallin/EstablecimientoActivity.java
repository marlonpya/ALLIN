package application.ucweb.proyectoallin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.dialogo.ImagenGrandeDialogo;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.OnClick;

public class EstablecimientoActivity extends BaseActivity {
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.tvDescripcionToolbar) TextView toolbarEvento;
    @BindView(R.id.idiv_layout_perfil_evento)ImageView ivFondoPerfilE;
    @BindView(R.id.iv_big_perfil_evento)ImageView ivBigPerfilEvento;
    @BindView(R.id.ivCartaPerfilE)ImageView ivCartaPerfilE;
    @BindView(R.id.ivApuntarmePerfilE)ImageView ivApuntarmePerfilE;
    @BindView(R.id.ivMapaEventosPerfilE)ImageView ivMapaEventosPerfilE;
    @BindView(R.id.iv_ic_webPerfilE)ImageView iv_ic_webPerfilE;
    @BindView(R.id.iv_ic_compartirPerfilE)ImageView iv_ic_compartirPerfilE;
    @BindView(R.id.iv_ic_fotosPerfilE)ImageView iv_ic_fotosPerfilE;
    @BindView(R.id.ivMarkerDiscotecaPerfilE)ImageView ivMarkerDiscotecaPerfilE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establecimiento);
        iniciarLayout();
        toolbarEvento.setText(getIntent().getStringExtra(Constantes.K_S_TITULO_TOOLBAR));
    }

    @OnClick(R.id.ivCartaPerfilE)
    public void openCarta(){ startActivity(new Intent(this, CartaEstablecimientoActivity.class));}

    @OnClick(R.id.ivApuntarmePerfilE)
    public void openComprarEntradaTarjeta(){ //startActivity(new Intent(this, ComprarEntradaActivity.class));
        Toast.makeText(this, "EN PROCESO", Toast.LENGTH_SHORT).show();
    }

    //VER MAPA DE EVENTO
    @OnClick(R.id.ivMapaEventosPerfilE)
    public void irAProximosEventos() {
        ImagenGrandeDialogo.dialogoEvento(this, R.drawable.escenario);
    }

    @OnClick(R.id.btnFotos)
    public void openGallery(){
        startActivity(new Intent(this, GalleriaActivity.class));
    }

    @OnClick(R.id.btnCompartir)
    public void goCompartir(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "@ALLINIGHT ");
        startActivity(Intent.createChooser(intent, "Compartir con"));
    }

    @OnClick(R.id.btnverWeb)
    public void openWeb(){
        Uri uriUrl = Uri.parse("http://www.uc-web.mobi/All-in-night/index.php");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @OnClick(R.id.ivMarkerDiscotecaPerfilE)
    public void eventoEnMapa() {
        startActivity(new Intent(this, MapaActivity.class));
    }

    private void iniciarLayout() {
        BaseActivity.setFondoActivity(this, ivFondoPerfilE);
        BaseActivity.usarGlide(this, R.drawable.img_party, ivBigPerfilEvento);
        BaseActivity.usarGlide(this, R.drawable.copa, ivCartaPerfilE);
        BaseActivity.usarGlide(this, R.drawable.iconoticket, ivApuntarmePerfilE);
        BaseActivity.usarGlide(this, R.drawable.iconomapa, ivMapaEventosPerfilE);
        BaseActivity.usarGlide(this, R.drawable.verweb, iv_ic_webPerfilE);
        BaseActivity.usarGlide(this, R.drawable.compartir,iv_ic_compartirPerfilE);
        BaseActivity.usarGlide(this, R.drawable.fotos,iv_ic_fotosPerfilE);
        BaseActivity.usarGlide(this, R.drawable.ubicanos, ivMarkerDiscotecaPerfilE);
        BaseActivity.setToolbarSon(toolbar, this, icono_toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }

}