package application.ucweb.proyectoallin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.dialogo.ImagenGrandeDialogo;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.modelparseable.EventoSimple;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.OnClick;

public class EstablecimientoConEntradaActivity extends BaseActivity implements IActividad{
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
    @BindView(R.id.txtDireccionDiscoteca) TextView txtDireccionDiscoteca;
    @BindView(R.id.txtAforoDiscoteca) TextView txtAforoDiscoteca;
    @BindView(R.id.txtDescripcionDiscoteca) TextView txtDescripcionDiscoteca;
    @BindView(R.id.txtUbicanos) TextView txtUbicanos;
    @BindView(R.id.btnMarkerEvento) FrameLayout btnMarkerEvento;

    private EventoSimple evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establecimiento_con_entrada);
        iniciarLayout();
        toolbarEvento.setText(getIntent().getStringExtra(Constantes.K_S_TITULO_TOOLBAR));
        evento = (EventoSimple) getIntent().getSerializableExtra(Constantes.OBJ_S_EVENTO);
        Glide.with(this).load(evento.getImagen()).into(ivBigPerfilEvento);
        txtDescripcionDiscoteca.setText(evento.getDescripcion());
        if (evento.getId_local()==0){
            txtDireccionDiscoteca.setText(getString(R.string.no_especificado));
            txtAforoDiscoteca.setText(getString(R.string.no_especificado));
            //btnMarkerEvento.setEnabled(false);
            //ivMarkerDiscotecaPerfilE.setColorFilter(getResources().getColor(R.color.colorgreyBotonDark));
            //txtUbicanos.setTextColor(getResources().getColor(R.color.colorgreyBotonDark));

        }else {
            txtDireccionDiscoteca.setText(evento.getDireccion());
            txtAforoDiscoteca.setText(String.valueOf(evento.getAforo()));
        }

        if (!isSesion()){
            BaseActivity.usarGlide(this, R.drawable.copablack, ivCartaPerfilE);
            ivCartaPerfilE.setEnabled(false);
            BaseActivity.usarGlide(this, R.drawable.iconoticketblack, ivApuntarmePerfilE);
            ivApuntarmePerfilE.setEnabled(false);
            BaseActivity.usarGlide(this, R.drawable.iconomapablack, ivMapaEventosPerfilE);
            ivMapaEventosPerfilE.setEnabled(false);
        }

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
        String url_imagen = "http://www.uc-web.mobi/Allnight/uploads/eventos/" + evento.getId_server() + "/mapa.jpg";
        ImagenGrandeDialogo.dialogoEvento(this, url_imagen);
        //ImagenGrandeDialogo.dialogoEvento(this, R.drawable.escenario);
    }

    @OnClick(R.id.btnFotos)
    public void openGallery(){
        Intent intent=new Intent(this, GalleriaActivity.class).putExtra(Constantes.ID_EVENTO, evento.getId_server());
        startActivity(intent);
    }

    @OnClick(R.id.btnCompartir)
    public void goCompartir(){
        String texto = evento.getNombre().toUpperCase();
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle(getString(R.string.app_name))
                .setContentUrl(Uri.parse("http://www.uc-web.mobi/All-in-night/index.php"))
                .setContentDescription(texto)
                .setImageUrl(Uri.parse(evento.getImagen()))
                .build();
        ShareDialog.show(this, content);
    }

    @OnClick(R.id.btnverWeb)
    public void openWeb(){
        Uri uriUrl = Uri.parse("http://www.uc-web.mobi/All-in-night/index.php");
        //Uri uriUrl = Uri.parse("http://"+evento.get());
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @OnClick(R.id.btnMarkerEvento)
    public void eventoEnMapa() {
        //startActivity(new Intent(this, MapaActivity.class));
        startActivity(new Intent(this, MapaActivity.class)
                .putExtra(Constantes.LATITUD, evento.getLatitud())
                .putExtra(Constantes.LONGITUD, evento.getLongitud())
                .putExtra(Constantes.FILTRO, 2));
    }

    @Override
    public boolean isSesion() {
        return Usuario.getUsuario() != null && Usuario.getUsuario().isSesion();
    }

    @Override
    public void iniciarLayout() {
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
    public void iniciarPDialog() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }

}