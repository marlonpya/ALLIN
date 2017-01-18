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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
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
import java.util.Timer;
import java.util.TimerTask;

import application.ucweb.proyectoallin.ListaDiscotecasActivity;
import application.ucweb.proyectoallin.ListaEventoActivity;
import application.ucweb.proyectoallin.ListaRapidaActivity;
import application.ucweb.proyectoallin.MapaActivity;
import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.RegistroActivity;
import application.ucweb.proyectoallin.adapter.BannerAdapter;
import application.ucweb.proyectoallin.adapter.DialogAdapter;
import application.ucweb.proyectoallin.adapter.DialogoDepartamentosAdapter;
import application.ucweb.proyectoallin.adapter.DialogoEventosAdapter;
import application.ucweb.proyectoallin.adapter.DialogoRecomendacionesAdapter;
import application.ucweb.proyectoallin.adapter.DialogoTipoMusicaAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Establecimiento;
import application.ucweb.proyectoallin.model.ItemSimple;
import application.ucweb.proyectoallin.model.MaterialSimpleListAdapter;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import application.ucweb.proyectoallin.util.Preferencia;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

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
    private Preferencia preferencia;
    private Realm realm;
    private BannerAdapter bannerAdapter;
    private static int index_banner = 0;
    private ProgressDialog pDialog;

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
        requestLocales();
        return view;
    }

    private void iniciarViewPager() {
        bannerAdapter = new BannerAdapter(getFragmentManager());
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
        //dialogoListaRepetida(1);
        ArrayList<ItemSimple> itemSimples = new ArrayList<>();
        itemSimples.add(new ItemSimple(getString(R.string.busqueda_gps),          R.drawable.busqueda_por_gps_64px));
        itemSimples.add(new ItemSimple(getString(R.string.busqueda_distrito),     R.drawable.busqueda_por_distritos_64px));
        itemSimples.add(new ItemSimple(getString(R.string.busqueda_tipomusica),   R.drawable.busqueda_por_tipomusica_64px));
        itemSimples.add(new ItemSimple(getString(R.string.busqueda_calendario),   R.drawable.busquedad_calendario));

        DialogAdapter adapter = new DialogAdapter(getActivity(), itemSimples);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.elija_busquedad)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0 : startActivity(new Intent(getActivity().getApplicationContext(), MapaActivity.class)
                            .putExtra(Constantes.I_TIP_ESTABLECIMIENTO_MAPA, Establecimiento.DISCOTECA)); break;
                            case 1 : break;
                            case 2 : break;
                            case 3 : break;
                        }
                    }
                })
                .show();
    }

    @OnClick(R.id.btnRestobares)
    public void dialogoListaBuscarRestobares() {
        dialogoListaRepetida(2);
    }

    @OnClick(R.id.btnKaraoke)
    public void dialogoListaBuscarKaraokes() {
        dialogoListaRepetida(3);
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
        final DialogoEventosAdapter adapter = new DialogoEventosAdapter(getActivity(), new DialogoDepartamentosAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index, ItemSimple item) {
                dialog.dismiss();
            }
        });
        adapter.add(new ItemSimple(getString(R.string.eventos_fiestas), R.drawable.busqueda_fiestas_64px));
        adapter.add(new ItemSimple(getString(R.string.eventos_conciertos), R.drawable.busqueda_vip_pass_64px));
        adapter.add(new ItemSimple(getString(R.string.eventos_otroseventos), R.drawable.busqueda_otros_eventos_64px));
        adapter.add(new ItemSimple(getString(R.string.eventos_calendario), R.drawable.busquedad_calendario));
        adapter.add(new ItemSimple(getString(R.string.eventos_vertodo), R.drawable.busqueda_ver_todo_64px));

        new MaterialDialog.Builder(getActivity())
                .title(getString(R.string.elija_busquedad))
                .adapter(adapter, null)
                .show();
    }

    private void dialogoListaRepetida(int tipo) {
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(getActivity(), new MaterialSimpleListAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index, ItemSimple item) {
                dialog.dismiss();
            }
        });
        adapter.add(new ItemSimple(getString(R.string.busqueda_gps),          R.drawable.busqueda_por_gps_64px));
        adapter.add(new ItemSimple(getString(R.string.busqueda_distrito),     R.drawable.busqueda_por_distritos_64px));
        adapter.add(new ItemSimple(getString(R.string.busqueda_tipomusica),   R.drawable.busqueda_por_tipomusica_64px));
        adapter.add(new ItemSimple(getString(R.string.busqueda_calendario),   R.drawable.busquedad_calendario));

        new MaterialDialog.Builder(getActivity())
                .title(elija_busquedad)
                .adapter(adapter, null )
                .show();
    }

    @OnClick(R.id.btnListaRapida)
    public void irAListarapida() {
        startActivity(new Intent(getActivity().getApplicationContext(), ListaRapidaActivity.class));
    }

    public static void dialogoListaDepartamentos2(Context context) {
        final DialogoDepartamentosAdapter adapter = new DialogoDepartamentosAdapter(context, new DialogoDepartamentosAdapter.Callback() {
            @Override
            public void onMaterialListItemSelected(MaterialDialog dialog, int index, ItemSimple item) {
                dialog.dismiss();
            }
        });
        adapter.add(new ItemSimple(context.getString(R.string.ancon), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.ate), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.barranco), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.breña), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.Carabayllo), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.Chaclacayo), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.Chorrillos), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.Cieneguilla), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.Comas), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.el_agustino), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.independencia), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.jesus_maria), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.la_molina), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.la_victoria), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.lima), R.drawable.icono_linea));
        adapter.add(new ItemSimple(context.getString(R.string.lince), R.drawable.icono_linea));

        new MaterialDialog.Builder(context)
                .title(context.getString(R.string.elija_busquedad))
                .adapter(adapter, null)
                .show();
    }

    public static void dialogoTipoDeMusica2(Context context, String titulo) {
        final ArrayList<ItemSimple> arrayList = new ArrayList<>();
        arrayList.add(new ItemSimple("Pachanga", R.drawable.busqueda_pachanga_64px));
        arrayList.add(new ItemSimple("Salsa", R.drawable.busqueda_salsa_64px));
        arrayList.add(new ItemSimple("Electronica", R.drawable.busqueda_electronica_64px));
        arrayList.add(new ItemSimple("Alternativa", R.drawable.busqueda_alternativa_64px));

        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(titulo)
                .adapter(new DialogoTipoMusicaAdapter(context,arrayList), null)
                .build();

        RecyclerView recyclerView = dialog.getRecyclerView();
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void intentAListaDRKER(String detalle_extra, Context context) {
        Intent intent = new Intent(context, ListaDiscotecasActivity.class);
        intent.putExtra(Constantes.K_S_TITULO_TOOLBAR, detalle_extra);
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
                        startActivity(new Intent(getActivity().getApplicationContext(), RegistroActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    private void requestLocales() {
        if (ConexionBroadcastReceiver.isConect()) {
            BaseActivity.showDialog(pDialog);
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    Constantes.LOCALES,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jLocales = jsonObject.getJSONArray("local");
                                for (int i = 0; i < jLocales.length(); i++) {
                                    Establecimiento establecimiento = new Establecimiento();
                                    establecimiento.setId(Establecimiento.getUltimoId());
                                    establecimiento.setId_server(jLocales.getJSONObject(i).getInt("LOC_ID"));
                                    establecimiento.setImagen(""); //TODO: FALTA IMAGEN
                                    establecimiento.setNombre(jLocales.getJSONObject(i).getString("LOC_NOMBRE"));
                                    establecimiento.setDireccion(jLocales.getJSONObject(i).getString("LOC_DIRECCION"));
                                    establecimiento.setLatitud(jLocales.getJSONObject(i).getDouble("LOC_LATITUD"));
                                    establecimiento.setLongitud(jLocales.getJSONObject(i).getDouble("LOC_LONGITUD"));
                                    establecimiento.setAforo(jLocales.getJSONObject(i).getInt("LOC_AFORO"));
                                    establecimiento.setNosotros(jLocales.getJSONObject(i).getString("LOC_NOSOTROS"));
                                    establecimiento.setUrl(jLocales.getJSONObject(i).getString("LOC_URL"));
                                    establecimiento.setGay(jLocales.getJSONObject(i).getInt("LOC_GAY") == 1);
                                    establecimiento.setFecha_inicio(jLocales.getJSONObject(i).getString("LOC_FEC_INICIO"));
                                    establecimiento.setFecha_fin(jLocales.getJSONObject(i).getString("LOC_FEC_FIN"));
                                    establecimiento.setDepartamento(jLocales.getJSONObject(i).getString("LOC_DEPARTAMENTO"));
                                    establecimiento.setProvincia(jLocales.getJSONObject(i).getString("LOC_PROVINCIA"));
                                    establecimiento.setDistrito(jLocales.getJSONObject(i).getString("LOC_DISTRITO"));
                                    establecimiento.setPlus(jLocales.getJSONObject(i).getInt("LOC_PLUS") == 1);
                                    establecimiento.setEstado(jLocales.getJSONObject(i).getInt("LOC_ESTADO") == 1);
                                    Establecimiento.insertOrUpdate(establecimiento);
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

    private void movimientoBanner() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (index_banner == bannerAdapter.getCount()) {
                    index_banner = 0;
                }
                viewPager.setCurrentItem(index_banner++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
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
}
