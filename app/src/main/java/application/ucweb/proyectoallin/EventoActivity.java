package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.model.Establecimiento;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;

public class EventoActivity extends BaseActivity {
    @BindView(R.id.drawer_layout) RelativeLayout layout_PerfilDiscoteca;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.tvDescripcionToolbar) TextView toolbarDiscoteca;
    @BindView(R.id.idiv_layout_perfil_discoteca)ImageView ivFondoPerfilD;
    @BindView(R.id.ivCartaPerfilD) ImageView ivCartaPerfilD;
    @BindView(R.id.ivApuntarmePerfilD) ImageView ivApuntarmePerfilD;
    @BindView(R.id.ivEventosPerfilD) ImageView ivEventosPerfilD;
    @BindView(R.id.ivAgregarPerfilD) ImageView ivAgregarPerfilD;
    @BindView(R.id.iv_ic_webPerfilD)ImageView iv_ic_webPerfilD;
    @BindView(R.id.iv_ic_compartirPerfilD)ImageView iv_ic_compartirPerfilD;
    @BindView(R.id.iv_ic_fotosPerfilD)ImageView iv_ic_fotosPerfilD;
    @BindView(R.id.ivMarkerDiscotecaPerfilD)ImageView ivMarkerDiscotecaPerfilD;
    @BindView(R.id.iv_big_perfil_discoteca)ImageView ivBigPerfilDiscoteca;
    @BindString(R.string.dialogo_like_discoteca_no_registrada) String discoteca_no_registrada;

    @BindView(R.id.txtDireccionDiscoteca) TextView txtDireccionDiscoteca;
    @BindView(R.id.txtAforoDiscoteca) TextView txtAforoDiscoteca;
    @BindView(R.id.txtDescripcionDiscoteca) TextView txtDescripcionDiscoteca;
    @BindView(R.id.txtTipoMusicaDiscoteca) TextView txtTipoMusicaDiscoteca;
    public static final String TAG = EventoActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private String generoMusica = "";
    private Establecimiento local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.actualizando));
        iniciarLayout();
        int idLocal = getIntent().getIntExtra(Constantes.K_L_ID_EVENTO, -1);
        Realm realm = Realm.getDefaultInstance();
        local = realm.where(Establecimiento.class).equalTo("id_server", idLocal).findFirst();
        requestGeneroMusica();
        Glide.with(this).load(local.getImagen()).into(ivBigPerfilDiscoteca);
        toolbarDiscoteca.setText(getIntent().getStringExtra(Constantes.K_S_TITULO_TOOLBAR));
        txtDireccionDiscoteca.setText(local.getDireccion());
        txtAforoDiscoteca.setText(String.valueOf(local.getAforo()));
        txtDescripcionDiscoteca.setText(local.getNosotros());

        if (getIntent().hasExtra(Constantes.K_L_ID_EVENTO)) {
            long posicion = getIntent().getLongExtra(Constantes.K_L_ID_EVENTO, -1);
            if (posicion == 0) {
                BaseActivity.usarGlide(this, R.drawable.copablack, ivCartaPerfilD);
                //ivCarta.setImageResource(R.drawable.copablack);
                ivCartaPerfilD.setEnabled(false);
                BaseActivity.usarGlide(this, R.drawable.notablack, ivApuntarmePerfilD);
                //ivApuntarme.setImageResource(R.drawable.notablack);
                ivApuntarmePerfilD.setEnabled(false);
                BaseActivity.usarGlide(this, R.drawable.baileblack, ivEventosPerfilD);
                //ivEventos.setImageResource(R.drawable.baileblack);
                ivEventosPerfilD.setEnabled(false);
                BaseActivity.usarGlide(this, R.drawable.listarapidablackvacio, ivAgregarPerfilD);
                //ivAgregar.setImageResource(R.drawable.listarapidablackvacio);
                ivAgregarPerfilD.setEnabled(false);

                //dialogo like
            }
        }
    }

    private void requestGeneroMusica() {
        showDialog(progressDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.GENERO_X_LOCAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jArray = jsonObject.getJSONArray("genero");
                            for (int i = 0; i < jArray.length(); i++) {
                                if (i==0){
                                    generoMusica = jArray.getJSONObject(i).getString("GEN_NOMBRE");
                                }else {
                                    generoMusica = generoMusica + "/" + jArray.getJSONObject(i).getString("GEN_NOMBRE");
                                }
                            }
                            txtTipoMusicaDiscoteca.setText(generoMusica);
                            Log.d(TAG, jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString(), e);
                        }
                        hidepDialog(progressDialog);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(error.toString(), error);
                        hidepDialog(progressDialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("LOC_ID", String.valueOf(local.getId_server()));
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }


    private void ver() {
        final String[] stringItems = { "1", "2", "3", "4" };
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        //
        dialog.title("wtf").titleTextSize_SP(14.5f).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {

            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.ivCartaPerfilD)
    public void openCarta(){
        startActivity(new Intent(this, CartaEstablecimientoActivity.class));
    }

    @OnClick(R.id.ivApuntarmePerfilD)
    public void openApuntarme(){
        new MaterialDialog.Builder(this)
                .title(R.string.ya_estas_en_lista_titulo)
                .content(R.string.ya_estas_en_lista_mensaje)
                .positiveText(R.string.aceptar)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        apuntarmeExitoso();
                    }
                })
                .negativeText(R.string.cancelar)
                .onNegative(null)
                .build()
                .show();
    }

    private void apuntarmeExitoso() {
        new MaterialDialog.Builder(this)
                .title(R.string.ya_estas_en_lista_titulo_si)
                .content(R.string.ya_estas_en_lista_mensaje_si)
                .iconRes(R.drawable.iconobuenarosa)
                .build()
                .show();
    }

    @OnClick(R.id.ivEventosPerfilD)
    public void openEventosProximos() {
        startActivity(new Intent(this, ListaEventoActivity.class)
        .putExtra(Constantes.K_S_TITULO_TOOLBAR, "EVENTOS PRÓXIMOS"));
    }

    @OnClick(R.id.ivAgregarPerfilD)
    public void openAgregarListaRapida() {
        new MaterialDialog.Builder(this)
                .title(R.string.agregado_a_lista_rapida_titulo)
                .content(R.string.agregado_a_lista_rapida_mensaje)
                .iconRes(R.drawable.iconobuenarosa)
                .positiveText("OK")
                .build()
                .show();
    }

    @OnClick(R.id.iv_ic_fotosPerfilD)
    public void openGallery() {
        //Ir a la galería de fotos del (DRKER)
        Intent intent=new Intent(this, GalleriaActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btnCompartir)
    public void goCompartir() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "@ALLINIGHT ");
        startActivity(Intent.createChooser(intent, "Compartir con"));
    }

    @OnClick(R.id.btnverWeb)
    public void openWeb() {
        //Uri uriUrl = Uri.parse("http://www.uc-web.mobi/All-in-night/index.php");
        Uri uriUrl = Uri.parse("http://"+local.getUrl());
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @OnClick(R.id.ivMarkerDiscotecaPerfilD)
    public void establecimiendoEnMapa() {
        startActivity(new Intent(this, MapaActivity.class)
                .putExtra("LAT", local.getLatitud())
                .putExtra("LON", local.getLongitud())
                .putExtra("TIPO", 1));
    }

    private void iniciarLayout() {
        BaseActivity.setFondoActivity(this,ivFondoPerfilD);
        BaseActivity.usarGlide(this, R.drawable.vercarta, ivCartaPerfilD);
        BaseActivity.usarGlide(this, R.drawable.apuntarme, ivApuntarmePerfilD);
        BaseActivity.usarGlide(this, R.drawable.proximoseventos, ivEventosPerfilD);
        BaseActivity.usarGlide(this, R.drawable.lista, ivAgregarPerfilD);
        BaseActivity.usarGlide(this, R.drawable.verweb, iv_ic_webPerfilD);
        BaseActivity.usarGlide(this, R.drawable.compartir, iv_ic_compartirPerfilD);
        BaseActivity.usarGlide(this, R.drawable.fotos, iv_ic_fotosPerfilD);
        BaseActivity.usarGlide(this, R.drawable.ubicanos, ivMarkerDiscotecaPerfilD);
        BaseActivity.usarGlide(this,R.drawable.img_party, ivBigPerfilDiscoteca);
        BaseActivity.setToolbarSon(toolbar, this, icono_toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
