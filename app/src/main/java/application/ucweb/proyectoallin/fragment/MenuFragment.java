package application.ucweb.proyectoallin.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import application.ucweb.proyectoallin.Calendario2Activity;
import application.ucweb.proyectoallin.ListaDiscotecasActivity;
import application.ucweb.proyectoallin.ListaEventoActivity;
import application.ucweb.proyectoallin.ListaRapidaActivity;
import application.ucweb.proyectoallin.MapaActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.RegistroActivity;
import application.ucweb.proyectoallin.adapter.BannerAdapter;
import application.ucweb.proyectoallin.adapter.DialogAdapter;
import application.ucweb.proyectoallin.adapter.DialogDepaProvDistAdapter;
import application.ucweb.proyectoallin.adapter.DialogoDepartamentosAdapter;
import application.ucweb.proyectoallin.adapter.DialogoRecomendacionesAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Banner;
import application.ucweb.proyectoallin.model.Establecimiento;
import application.ucweb.proyectoallin.model.ItemSimple;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.model.zona.Departamento;
import application.ucweb.proyectoallin.model.zona.Distrito;
import application.ucweb.proyectoallin.model.zona.Provincia;
import application.ucweb.proyectoallin.modelparseable.EventoSimple;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import application.ucweb.proyectoallin.util.Preferencia;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * AIzaSyAIyIgyqYaU-kbFrQzz1aBjpwMWn0IMUJ0
 */
public class MenuFragment extends Fragment implements IActividad{
    public static final String TAG = MenuFragment.class.getSimpleName();
    @BindView(R.id.layout_f_menu) LinearLayout layout;
    @BindView(R.id.btnEventosEspeciales) ImageView btnEventosEspeciales;
    @BindView(R.id.btnRecomendamos) ImageView btnRecomendamos;
    @BindView(R.id.btnListaRapida) ImageView btnListaRapida;
    @BindView(R.id.btnDiscotecas) ImageView btnDiscotecas;
    @BindView(R.id.btnKaraoke) ImageView btnKaraoke;
    @BindView(R.id.btnRestobares) ImageView btnRestobares;
    @BindView(R.id.vp_fragment_menu) ViewPager viewPager;
    @BindString(R.string.elija_busquedad) String elija_busquedad;
    @BindString(R.string.elija_departamento) String elija_departamento;
    @BindString(R.string.elija_provincia) String elija_provincia;
    @BindString(R.string.elija_distrito) String elija_distrito;
    private Preferencia preferencia;
    private Realm realm;
    private BannerAdapter bannerAdapter;
    private static int index_banner = 0;
    private ProgressDialog pDialog;
    final ArrayList<ItemSimple> itemGenero = new ArrayList<>();
    private int idGenero;

    public MenuFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);

        preferencia = new Preferencia(getActivity());

        iniciarLayout();
        iniciarPDialog();
        iniciarViewPager();
        if (!isSesion()) usuarioNoRegistrado();
        //requestLocales();
        //requestGeneros();
        if (ConexionBroadcastReceiver.isConect()) requestBanner();
        return view;
    }

    private void iniciarViewPager() {
        ArrayList<Banner> banners = new ArrayList<>(Banner.getBanners());
        if (!banners.isEmpty() || banners != null) {
            bannerAdapter = new BannerAdapter(getFragmentManager(), banners);
            viewPager.setAdapter(bannerAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

                @Override
                public void onPageSelected(int position) { index_banner = position; }

                @Override
                public void onPageScrollStateChanged(int state) { }
            });
            movimientoBanner();
        }
    }

    private void usuarioNoRegistrado() {
        dialogoRegistrateAllin();
        BaseActivity.usarGlide(getActivity(), R.drawable.recomendacionesblack_24, btnRecomendamos);
        //btnRecomendamos.setImageResource(R.drawable.recomendacionesblack_24);
        btnRecomendamos.setEnabled(false);
        BaseActivity.usarGlide(getActivity(), R.drawable.listarapidablack_24, btnListaRapida);
        //btnListaRapida.setImageResource(R.drawable.listarapidablack_24);
        btnListaRapida.setEnabled(false);
    }

    @OnClick(R.id.btnDiscotecas)
    public void dialogoListaBuscarDiscotecas() {
        metodo(Establecimiento.DISCOTECA);
    }

    private void metodo(final int tipo_local) {
        ArrayList<ItemSimple> itemSimples = new ArrayList<>();
        itemSimples.add(new ItemSimple(getString(R.string.busqueda_gps),          R.drawable.busqueda_por_gps_64px));
        itemSimples.add(new ItemSimple(getString(R.string.busqueda_distrito),     R.drawable.busqueda_por_distritos_64px));
        itemSimples.add(new ItemSimple(getString(R.string.busqueda_tipomusica),   R.drawable.busqueda_por_tipomusica_64px));
        itemSimples.add(new ItemSimple(getString(R.string.busqueda_calendario),   R.drawable.busquedad_calendario));

        DialogAdapter adapter = new DialogAdapter(getActivity(), itemSimples);
        new AlertDialog.Builder(getActivity())
                .setTitle(elija_busquedad)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (ConexionBroadcastReceiver.isConect()) {
                            switch (which) {
                                case 0: startActivity(new Intent(getActivity().getApplicationContext(), MapaActivity.class)
                                            .putExtra(Constantes.I_TIP_ESTABLECIMIENTO_MAPA, tipo_local)
                                            .putExtra(Constantes.FILTRO, Constantes.FILTRO_GPS)); break;
                                case 1: dialogoListaDepartamentos(getContext(), tipo_local, Constantes.FILTRO_DISTRITO); break;
                                case 2: dialogoTipoDeMusica2(getContext(), getResources().getString(R.string.elija_genero), tipo_local, Constantes.FILTRO_MUSICA); break;
                                case 3: dialogoCalendario(tipo_local); break;
                            }
                        }else
                            ConexionBroadcastReceiver.showSnack(layout, getContext());
                    }
                })
                .show();
    }

    @OnClick(R.id.btnRestobares)
    public void dialogoListaBuscarRestobares() {
        //dialogoListaRepetida(2);
        metodo(Establecimiento.RESTOBAR);
    }

    @OnClick(R.id.btnKaraoke)
    public void dialogoListaBuscarKaraokes() {
        //dialogoListaRepetida(3);
        metodo(Establecimiento.KARAOKE);
    }

    @OnClick(R.id.btnRecomendamos)
    public void dialogoListaRecomendaciones() {
        final DialogoRecomendacionesAdapter adapter = new DialogoRecomendacionesAdapter(getActivity(), new DialogoDepartamentosAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index, ItemSimple item) {
                dialog.dismiss();
            }
        });
        adapter.add(new ItemSimple(getString(R.string.recomendados_lugar),        R.drawable.busqueda_bebida));
        adapter.add(new ItemSimple(getString(R.string.recomendados_eventosespec), R.drawable.busqueda_musica));
        new MaterialDialog.Builder(getActivity())
                .title(getString(R.string.elija_busquedad))
                .adapter(adapter, null)
                .show();
    }

    @OnClick(R.id.btnEventosEspeciales)
    public void dialogoListaEventosE() {
        final ArrayList<ItemSimple> items = new ArrayList<>();
        items.add(new ItemSimple(getString(R.string.eventos_fiestas), R.drawable.busqueda_fiestas_64px));
        items.add(new ItemSimple(getString(R.string.eventos_conciertos), R.drawable.busqueda_vip_pass_64px));
        items.add(new ItemSimple(getString(R.string.eventos_otroseventos), R.drawable.busqueda_otros_eventos_64px));
        items.add(new ItemSimple(getString(R.string.eventos_calendario), R.drawable.busquedad_calendario));
        items.add(new ItemSimple(getString(R.string.eventos_vertodo), R.drawable.busqueda_ver_todo_64px));
        final DialogAdapter adapter = new DialogAdapter(getActivity(), items);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.busqueda_titulo)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (ConexionBroadcastReceiver.isConect()) {
                            Intent intent = new Intent(getActivity(), ListaEventoActivity.class)
                                    .putExtra(Constantes.S_EVENTO_TOOLBAR, "EVENTOS");
                            switch (which) {
                                case 0 : intent.putExtra(Constantes.I_EVENTO_DIALOG, EventoSimple.FIESTA) ; break;
                                case 1 : intent.putExtra(Constantes.I_EVENTO_DIALOG, EventoSimple.CONCIERTO); break;
                                case 2 : intent.putExtra(Constantes.I_EVENTO_DIALOG, EventoSimple.OTROS); break;
                                case 3 : startActivity(new Intent(getActivity(), Calendario2Activity.class)
                                        .putExtra(Constantes.TIPO_ESTABLECIMIENTO, Establecimiento.TIPO_EVENTO)); break;
                                case 4 : intent.putExtra(Constantes.I_EVENTO_DIALOG, EventoSimple.TODOS); break;
                            }
                            if (which != 3) startActivity(intent);
                        } else
                            ConexionBroadcastReceiver.showSnack(layout, getContext());
                    }
                })
                .show();
    }

    @OnClick(R.id.btnListaRapida)
    public void irAListarapida() {
        startActivity(new Intent(getActivity().getApplicationContext(), ListaRapidaActivity.class));
    }

    private void dialogoCalendario(int tipo_local){
        startActivity(new Intent(getActivity().getApplicationContext(), Calendario2Activity.class)
                .putExtra(Constantes.TIPO_ESTABLECIMIENTO, tipo_local));
    }

    private void dialogoTipoDeMusica2(final Context context, final String titulo, final int tipoLocal, final int filtro ) {
        final ArrayList<ItemSimple> arrayList = new ArrayList<>();
        arrayList.add(new ItemSimple("Pachanga", R.drawable.busqueda_pachanga_64px));
        arrayList.add(new ItemSimple("Salsa", R.drawable.busqueda_salsa_64px));
        arrayList.add(new ItemSimple("Electrónica", R.drawable.busqueda_electronica_64px));
        arrayList.add(new ItemSimple("Alternativa", R.drawable.busqueda_alternativa_64px));

        final DialogAdapter adapter = new DialogAdapter(getActivity(), arrayList);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.elija_genero)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        idGenero=which+1;
                        intentAListaDRKER(adapter.getItem(which).getTitulo(), context, tipoLocal, filtro);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void dialogoListaDepartamentos(final Context context, final int tipoLocal, final int filtro){
        final ArrayList<ItemSimple> itemSimples = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Departamento> departamento = realm.where(Departamento.class).findAll();
        for (int i = 0; i < departamento.size(); i++){
            itemSimples.add(new ItemSimple((int)departamento.get(i).getId(), departamento.get(i).getNombre(), 0, R.drawable.icono_linea));
        }
        final DialogDepaProvDistAdapter adapter = new DialogDepaProvDistAdapter(getActivity(), itemSimples);
        new AlertDialog.Builder(getActivity())
                .setTitle(elija_departamento)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogoListaProvincias(context, adapter.getItem(which).getId(), tipoLocal, filtro);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void dialogoListaProvincias(final Context context, int idDep, final int tipoLocal, final int filtro){
        final ArrayList<ItemSimple> itemSimples = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Provincia> provincia = realm.where(Provincia.class).equalTo("dep_id", idDep).findAll();
        for (int i = 0; i < provincia.size(); i++){
            itemSimples.add(new ItemSimple(provincia.get(i).getId_server(), provincia.get(i).getNombre(), 0, R.drawable.icono_linea));
        }
        final DialogDepaProvDistAdapter adapter = new DialogDepaProvDistAdapter(getActivity(), itemSimples);
        new AlertDialog.Builder(getActivity())
                .setTitle(elija_provincia)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogoListarDistritos(context, adapter.getItem(which).getId(), tipoLocal, filtro);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void dialogoListarDistritos(final Context context, int idPro, final int tipoLocal, final int filtro){
        final ArrayList<ItemSimple> itemSimples = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Distrito> distrito = realm.where(Distrito.class).equalTo("pro_id", idPro).findAll();
        for (int i = 0; i < distrito.size(); i++){
            itemSimples.add(new ItemSimple(distrito.get(i).getId_server(), distrito.get(i).getNombre(), 0, R.drawable.icono_linea));
        }
        final DialogDepaProvDistAdapter adapter = new DialogDepaProvDistAdapter(getActivity(), itemSimples);
        new AlertDialog.Builder(getActivity())
                .setTitle(elija_distrito)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intentAListaDRKER(adapter.getItem(which).getTitulo(), context, tipoLocal, filtro);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void dialogoGeneroMusica(final Context context, final int tipoLocal, final int filtro){
        final DialogAdapter adapter = new DialogAdapter(getActivity(), itemGenero);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.elija_genero)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context, itemGenero.get(which).getTitulo(), Toast.LENGTH_SHORT).show();
                        //idGenero=itemGenero.get(which).getId();
                        intentAListaDRKER(adapter.getItem(which).getTitulo(), context, tipoLocal, filtro);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void intentAListaDRKER(String detalle_extra, Context context, int tipo, int filtro) {
        Intent intent = new Intent(context, ListaDiscotecasActivity.class);
        intent.putExtra(Constantes.K_S_TITULO_TOOLBAR, detalle_extra);
        intent.putExtra(Constantes.TIPO_ESTABLECIMIENTO, tipo);
        intent.putExtra(Constantes.FILTRO, filtro);
        intent.putExtra(Constantes.GENERO_MUSICA, idGenero);
        context.startActivity(intent);
    }

    public static void intentAListaEventos(String titulo, Context context) {
        Intent intent = new Intent(context.getApplicationContext(), ListaEventoActivity.class);
        intent.putExtra(Constantes.K_S_TITULO_TOOLBAR, titulo);
        context.startActivity(intent);
    }

    private void dialogoRegistrateAllin() {
        new AlertDialog.Builder(getActivity())
                .setTitle("REGISTRATE")
                .setMessage("Si quieres acceder a mas beneficios de ALLIN, hazte miembro GRATUITAMENTE")
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(getActivity().getApplicationContext(), RegistroActivity.class));
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    private void movimientoBanner() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (index_banner == bannerAdapter.getCount())
                    index_banner = 0;
                viewPager.setCurrentItem(index_banner++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 3000, 3000);
    }

    @Override
    public void iniciarLayout() {
        BaseActivity.usarGlide(getActivity(), R.drawable.eventosespeciales, btnEventosEspeciales);
        BaseActivity.usarGlide(getActivity(), R.drawable.recomendaciones, btnRecomendamos);
        BaseActivity.usarGlide(getActivity(), R.drawable.listarapida, btnListaRapida);
        BaseActivity.usarGlide(getActivity(), R.drawable.discoteca, btnDiscotecas);
        BaseActivity.usarGlide(getActivity(), R.drawable.karaoke, btnKaraoke);
        BaseActivity.usarGlide(getActivity(), R.drawable.restobar, btnRestobares);
    }

    @Override
    public boolean isSesion() {
        return Usuario.getUsuario() != null && Usuario.getUsuario().isSesion();
    }

    @Override
    public void iniciarPDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.actualizando));
    }

    private void requestGeneros() {
        if (ConexionBroadcastReceiver.isConect()) {
            BaseActivity.showDialog(pDialog);
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    Constantes.GENEROS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jGeneros = jsonObject.getJSONArray("categoria");
                                for (int i = 0; i < jGeneros.length(); i++) {
                                    itemGenero.add(new ItemSimple(jGeneros.getJSONObject(i).getInt("GEN_ID"), jGeneros.getJSONObject(i).getString("GEN_NOMBRE"), -1, R.drawable.busqueda_alternativa_64px));
                                }
                                BaseActivity.hidepDialog(pDialog);
                            } catch (JSONException e) {
                                Log.e(TAG, e.toString(), e);
                                BaseActivity.hidepDialog(pDialog);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e(error.toString(), error);
                            BaseActivity.errorConexion(getActivity());
                            BaseActivity.hidepDialog(pDialog);
                        }
                    }
            );
            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Configuracion.getInstance().addToRequestQueue(request, TAG);
        }
    }

    private void requestBanner() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.BANNERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")) {
                                JSONArray jBanner = jsonObject.getJSONArray("data");
                                ArrayList<Banner> banners = new ArrayList<>();
                                for (int i = 0 ; i < jBanner.length(); i++) {
                                    Banner banner = new Banner();
                                    banner.setId(jBanner.getJSONObject(i).getInt("BAN_ID"));
                                    banner.setUrl(jBanner.getJSONObject(i).getString("BAN_IMAGEN"));
                                    banners.add(banner);
                                }
                                if (!banners.isEmpty()) {
                                    Banner.insertOrUpdate(banners);
                                    iniciarViewPager();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString(), e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(TAG, error.toString(), error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("banner", "banner");
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }
}
