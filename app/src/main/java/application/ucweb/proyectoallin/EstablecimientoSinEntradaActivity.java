package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Establecimiento;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.modelparseable.EstablecimientoSimple;
import application.ucweb.proyectoallin.modelparseable.EventoSimple;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class EstablecimientoSinEntradaActivity extends BaseActivity implements IActividad {
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
    @BindView(R.id.btnListaRapida) LinearLayout btnListaRapida;

    //@BindView(R.id.btnFotos)LinearLayout btnFotos;

    @BindString(R.string.dialogo_like_discoteca_no_registrada) String discoteca_no_registrada;

    @BindView(R.id.txtDireccionDiscoteca) TextView txtDireccionDiscoteca;
    @BindView(R.id.txtAforoDiscoteca) TextView txtAforoDiscoteca;
    @BindView(R.id.txtDescripcionDiscoteca) TextView txtDescripcionDiscoteca;
    @BindView(R.id.txtTipoMusicaDiscoteca) TextView txtTipoMusicaDiscoteca;
    public static final String TAG = EstablecimientoSinEntradaActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private String generoMusica = "";
    private EstablecimientoSimple local;
    private EventoSimple evento;
    private int idLocal=-1;
    private int idEvento=-1;
    private int tipoVista=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establecimiento_sin_entrada);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.enviando_peticion));
        iniciarLayout();
        toolbarDiscoteca.setText(getIntent().getStringExtra(Constantes.K_S_TITULO_TOOLBAR));
        if (getIntent().hasExtra(Constantes.OBJ_S_ESTABLECIMIENTO)){
            idLocal = getIntent().getIntExtra(Constantes.K_L_ID_EVENTO, -1);
            local = (EstablecimientoSimple)getIntent().getSerializableExtra(Constantes.OBJ_S_ESTABLECIMIENTO);
            getIntent().removeExtra(Constantes.OBJ_S_ESTABLECIMIENTO);
            if (ConexionBroadcastReceiver.isConect()){requestGeneroMusica();}
            else {ConexionBroadcastReceiver.showSnack(layout_PerfilDiscoteca, this);}
            Glide.with(this).load(local.getImagen()).into(ivBigPerfilDiscoteca);
            txtDireccionDiscoteca.setText(local.getDireccion().trim());
            txtAforoDiscoteca.setText(String.valueOf(local.getAforo()));
            txtDescripcionDiscoteca.setText(local.getNosotros().trim());
            tipoVista=Constantes.VISTA_DE_LOCAL;
        }
        else if (getIntent().hasExtra(Constantes.OBJ_S_EVENTO)){
            btnListaRapida.setVisibility(View.GONE);

            evento = (EventoSimple)getIntent().getSerializableExtra(Constantes.OBJ_S_EVENTO);
            idEvento=evento.getId_server();
            getIntent().removeExtra(Constantes.OBJ_S_EVENTO);

            Glide.with(this).load(evento.getImagen()).into(ivBigPerfilDiscoteca);
            txtDescripcionDiscoteca.setText(evento.getDescripcion());
            tipoVista=Constantes.VISTA_DE_EVENTO;
            if (evento.getId_local()!=0){
                txtDireccionDiscoteca.setText(evento.getDireccion());
                txtAforoDiscoteca.setText(String.valueOf(evento.getAforo()));
                idLocal=evento.getId_local();
                requestGeneroMusica();
            }
            else {
                txtDireccionDiscoteca.setText(getString(R.string.no_especificado));
                txtAforoDiscoteca.setText(getString(R.string.no_especificado));
                txtTipoMusicaDiscoteca.setText(getString(R.string.no_especificado));
            }
        }
        if (!isSesion()){
            BaseActivity.usarGlide(this, R.drawable.copablack, ivCartaPerfilD);
            ivCartaPerfilD.setEnabled(false);
            BaseActivity.usarGlide(this, R.drawable.notablack, ivApuntarmePerfilD);
            ivApuntarmePerfilD.setEnabled(false);
            BaseActivity.usarGlide(this, R.drawable.baileblack, ivEventosPerfilD);
            ivEventosPerfilD.setEnabled(false);
            BaseActivity.usarGlide(this, R.drawable.listarapidablackvacio, ivAgregarPerfilD);
            ivAgregarPerfilD.setEnabled(false);
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
                params.put("LOC_ID", String.valueOf(idLocal));
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
        if (tipoVista==Constantes.VISTA_DE_LOCAL){
            startActivity(new Intent(this, CartaEstablecimientoActivity.class)
                    //.putExtra(Constantes.ID_LOCAL, local.getId_server())
                    .putExtra(Constantes.OBJ_S_ESTABLECIMIENTO, local));
        }else if(tipoVista==Constantes.VISTA_DE_EVENTO){
            //Toast.makeText(this, "Es evento " + evento.getId_server(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CartaEstablecimientoActivity.class)
                    //.putExtra(Constantes.ID_EVENTO, evento.getId_server())
                    .putExtra(Constantes.OBJ_S_EVENTO, evento));
        }
    }

    @OnClick(R.id.ivApuntarmePerfilD)
    public void openApuntarme(){
        if (tipoVista==Constantes.VISTA_DE_LOCAL){
            startActivity(new Intent(getApplicationContext(), Calendario2Activity.class)
                    .putExtra(Constantes.CALENDARIO_APUNTAR_LISTA, true)
                    .putExtra(Constantes.OBJ_S_ESTABLECIMIENTO, local));
        }else if(tipoVista==Constantes.VISTA_DE_EVENTO){
            new AlertDialog.Builder(EstablecimientoSinEntradaActivity.this)
                    .setTitle(R.string.ya_estas_en_lista_titulo)
                    .setMessage(getString(R.string.ya_estas_en_lista_mensaje))
                    .setCancelable(false)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            requestRegistrarEnListaEventoConLocal();
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    private void requestRegistrarEnListaEventoConLocal() {
        showDialog(progressDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.REGISTRAR_EN_LISTA_EVENTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String mensaje=jsonObject.getString("message");
                            if (jsonObject.getBoolean("status")) {
                                new AlertDialog.Builder(EstablecimientoSinEntradaActivity.this)
                                        .setTitle(R.string.ya_estas_en_lista_titulo_si)
                                        .setMessage(getString(R.string.ya_estas_en_lista_mensaje_si))
                                        .setIcon(R.drawable.iconobuenarosa)
                                        .setCancelable(false)
                                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }else {
                                new AlertDialog.Builder(EstablecimientoSinEntradaActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(mensaje)
                                        .setCancelable(false)
                                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
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
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("loc_id", String.valueOf(evento.getId_local()));
                params.put("usu_id", String.valueOf(Usuario.getUsuario().getId_server()));
                params.put("eve_id", String.valueOf(evento.getId_server()));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Configuracion.getInstance().addToRequestQueue(request, TAG);
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
                .putExtra(Constantes.ID_LOCAL, idLocal)
                .putExtra(Constantes.I_EVENTO_DIALOG, Constantes.FILTRO_X_LOCAL)
                /*.putExtra(Constantes.OBJ_S_ESTABLECIMIENTO, local)*/);
    }

    @OnClick(R.id.ivAgregarPerfilD)
    public void openAgregarListaRapida() {
        if (tipoVista==Constantes.VISTA_DE_LOCAL){
            final Realm realm = Realm.getDefaultInstance();
            final RealmResults <Establecimiento> listaRapida;
            listaRapida = realm.where(Establecimiento.class).equalTo("id_server", local.getId_server()).findAll();
            if (listaRapida.size()>0){
                //Toast.makeText(this, "Ya fue agregado", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(EstablecimientoSinEntradaActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(getString(R.string.ya_existe_lista_rapida_mensaje))
                        .setCancelable(false)
                        .setIcon(R.drawable.iconobuenarosa)
                        .setPositiveButton(R.string.mantener, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.remover, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                realm.beginTransaction();
                                listaRapida.deleteAllFromRealm();
                                realm.commitTransaction();
                                realm.close();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }else {
                realm.beginTransaction();
                Establecimiento establecimiento = realm.createObject(Establecimiento.class);

                establecimiento.setId(Establecimiento.getUltimoId());
                establecimiento.setId_server(local.getId_server());
                establecimiento.setImagen(local.getImagen());
                establecimiento.setNombre(local.getNombre());
                establecimiento.setDireccion(local.getDireccion());
                establecimiento.setLatitud(local.getLatitud());
                establecimiento.setLongitud(local.getLongitud());
                establecimiento.setAforo(local.getAforo());
                establecimiento.setNosotros(local.getNosotros());
                establecimiento.setUrl(local.getUrl());
                establecimiento.setGay(local.isGay());
                establecimiento.setFecha_inicio(local.getFecha_inicio());
                establecimiento.setFecha_fin(local.getFecha_fin());
                establecimiento.setDepartamento(local.getDepartamento());
                establecimiento.setProvincia(local.getProvincia());
                establecimiento.setDistrito(local.getDistrito());
                establecimiento.setPlus(local.isPlus());
                establecimiento.setEstado(local.isEstado());
                establecimiento.setRazon_social(local.getRazon_social());
                establecimiento.setRuc(local.getRuc());
                establecimiento.setLunes(local.isLunes());
                establecimiento.setMartes(local.isMartes());
                establecimiento.setMiercoles(local.isMiercoles());
                establecimiento.setJueves(local.isJueves());
                establecimiento.setViernes(local.isViernes());
                establecimiento.setSabado(local.isSabado());
                establecimiento.setDomingo(local.isDomingo());
                establecimiento.setPrecio(local.getPrecio());
                establecimiento.setFechaAdded(Calendar.getInstance().getTime());
                realm.copyToRealm(establecimiento);
                realm.commitTransaction();
                realm.close();

                new AlertDialog.Builder(EstablecimientoSinEntradaActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage(getString(R.string.agregado_a_lista_rapida_mensaje))
                        .setCancelable(false)
                        .setIcon(R.drawable.iconobuenarosa)
                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
        /*else if(tipoVista==Constantes.CARTA_DE_EVENTO){
            //Evento
        }*/
    }

    @OnClick(R.id.btnFotos)
    public void openGallery() {
        //Ir a la galería de fotos del (DRKER)
        if (idEvento==-1) {
            Intent intent = new Intent(this, GalleriaActivity.class).putExtra(Constantes.ID_LOCAL, local.getId_server());
            startActivity(intent);
        }else {
            Intent intent=new Intent(this, GalleriaActivity.class).putExtra(Constantes.ID_EVENTO, evento.getId_server());
            startActivity(intent);
        }
    }
    @OnClick(R.id.btnCompartir)
    public void goCompartir() {
        if (idEvento==-1) {
            String texto = local.getNombre().toUpperCase() + " - " +
                    local.getDireccion();
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentTitle(getString(R.string.app_name))
                    .setContentUrl(Uri.parse(local.getUrl()))
                    .setContentDescription(texto)
                    .setImageUrl(Uri.parse(local.getImagen()))
                    .build();
            ShareDialog.show(this, content);
        }else {
            String texto = evento.getNombre().toUpperCase();
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentTitle(getString(R.string.app_name))
                    //.setContentUrl(Uri.parse("http://www.uc-web.mobi/All-in-night/index.php"))
                    .setContentUrl(Uri.parse("https://www.google.com"))
                    .setContentDescription(texto)
                    .setImageUrl(Uri.parse(evento.getImagen()))
                    .build();
            ShareDialog.show(this, content);
        }

    }

    @OnClick(R.id.btnverWeb)
    public void openWeb() {
        if (idEvento==-1) {
            Log.v("Amd", local.getUrl());
            try {
                Uri uriUrl = Uri.parse(local.getUrl());
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }catch (Exception e){

                Uri uriUrl = Uri.parse("http://" + local.getUrl());
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        }else {
            Uri uriUrl = Uri.parse("https://www.google.com");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }
    }

    @OnClick(R.id.btnUbicanos)
    public void establecimiendoEnMapa() {
        if (idEvento==-1){
            startActivity(new Intent(this, MapaActivity.class)
                    .putExtra(Constantes.LATITUD, local.getLatitud())
                    .putExtra(Constantes.LONGITUD, local.getLongitud())
                    .putExtra(Constantes.FILTRO, 2));
        }
        else {
            startActivity(new Intent(this, MapaActivity.class)
                    .putExtra(Constantes.LATITUD, evento.getLatitud())
                    .putExtra(Constantes.LONGITUD, evento.getLongitud())
                    .putExtra(Constantes.FILTRO, 2));
        }
    }

    @Override
    public boolean isSesion() {
        return Usuario.getUsuario() != null && Usuario.getUsuario().isSesion();
    }

    @Override
    public void iniciarLayout() {
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
    public void iniciarPDialog() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
