package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoallin.adapter.ImagenesAdapterGrid;
import application.ucweb.proyectoallin.adapter.ImagenesRealmAdapterGrid;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.model.Imagen;
import application.ucweb.proyectoallin.model.ItemSimple;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class GalleriaActivity extends BaseActivity {
    public static final String TAG = GalleriaActivity.class.getSimpleName();
    //@BindView(R.id.rrv_lista_galleria) RealmRecyclerView rrv_lista_galleria;
    @BindView(R.id.rrv_lista_galleria) RecyclerView rrv_lista_galleria;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.tvDescripcionToolbar)TextView tvTituloGallery;
    @BindView(R.id.idiv_layout_gallery_discoteca)ImageView ivFondoGallery;
    @BindView(R.id.id_gallery) RelativeLayout id_gallery;
    //private Realm realm;
    //private ImagenesRealmAdapterGrid adapter;
    private ImagenesAdapterGrid adapter;
    //private RealmResults<Imagen> lista_imagenes;
    private ArrayList<ItemSimple> lista_imagenes = new ArrayList<>();
    private int idLocal, idEvento;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galleria);
        iniciarLayout();
        iniciarPDialog();
        //if (Imagen.getUltimoId() == 0) cargarData();
        idLocal = getIntent().getIntExtra(Constantes.ID_LOCAL, -1);
        getIntent().removeExtra(Constantes.ID_LOCAL);
        idEvento = getIntent().getIntExtra(Constantes.ID_EVENTO, -1);
        getIntent().removeExtra(Constantes.ID_EVENTO);

        if (idLocal!=-1) {
            if(ConexionBroadcastReceiver.isConect()){
                requestImagenesLocal();
            }
            else ConexionBroadcastReceiver.showSnack(id_gallery, this);
        }
        else if (idEvento!=-1) {
            if (ConexionBroadcastReceiver.isConect()){
                requestImagenesEvento();
            }
            else ConexionBroadcastReceiver.showSnack(id_gallery, this);
        }
        //cargarRealmListas();
    }

    private void mostrarImagenes() {
        adapter = new ImagenesAdapterGrid(GalleriaActivity.this, lista_imagenes);
        rrv_lista_galleria.setHasFixedSize(true);
        rrv_lista_galleria.setLayoutManager(new GridLayoutManager(GalleriaActivity.this, 3));
        rrv_lista_galleria.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /*private void cargarData() {
        final String IMGS[] = {
                "https://images.unsplash.com/photo-1444090542259-0af8fa96557e?q=80&fm=jpg&w=1080&fit=max&s=4b703b77b42e067f949d14581f35019b",
                "https://images.unsplash.com/photo-1439546743462-802cabef8e97?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1441155472722-d17942a2b76a?q=80&fm=jpg&w=1080&fit=max&s=80cb5dbcf01265bb81c5e8380e4f5cc1",
                "https://images.unsplash.com/photo-1437651025703-2858c944e3eb?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1431538510849-b719825bf08b?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1434873740857-1bc5653afda8?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1439396087961-98bc12c21176?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1433616174899-f847df236857?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1438480478735-3234e63615bb?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300",
                "https://images.unsplash.com/photo-1438027316524-6078d503224b?dpr=2&fit=crop&fm=jpg&h=725&q=50&w=1300"
        };
        realm = Realm.getDefaultInstance();
        for (String IMG : IMGS) {
            realm.beginTransaction();
            Imagen imagen = realm.createObject(Imagen.class);
            imagen.setId(Imagen.getUltimoId());
            imagen.setRuta(IMG);
            realm.copyToRealm(imagen);
            realm.commitTransaction();
            Log.d(TAG, imagen.toString());
        }
        realm.close();
    }
*/
    private void iniciarLayout() {
        BaseActivity.setToolbarSon(toolbar, this, icono_toolbar);
        tvTituloGallery.setText(R.string.galeria);
        setFondoActivity(this, ivFondoGallery);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void requestImagenesLocal() {
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.IMAGENES_LOCAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jEventos = jsonObject.getJSONArray("data");
                            if (jEventos.length()>1) {
                                //JSONArray jEventos = jsonObject.getJSONArray("data");
                                for (int i = 1; i < jEventos.length(); i++) {
                                    ItemSimple imagen = new ItemSimple();
                                    imagen.setTitulo(jEventos.getJSONObject(i).getString("IMG_NOMBRE"));
                                    lista_imagenes.add(imagen);
                                }
                                mostrarImagenes();
                            }else {
                                new AlertDialog.Builder(GalleriaActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(getString(R.string.imagenes_not_found))
                                        .setCancelable(false)
                                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                onBackPressed();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString(), e);
                        }
                        hidepDialog(pDialog);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(error.toString(), error);
                        hidepDialog(pDialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(idLocal));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    private void requestImagenesEvento() {
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.IMAGENES_EVENTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jEventos = jsonObject.getJSONArray("data");
                            if (jEventos.length()>1) {
                                //JSONArray jEventos = jsonObject.getJSONArray("data");
                                for (int i = 1; i < jEventos.length(); i++) {
                                    ItemSimple imagen = new ItemSimple();
                                    imagen.setTitulo(jEventos.getJSONObject(i).getString("IMG_NOMBRE"));
                                    lista_imagenes.add(imagen);
                                }
                                mostrarImagenes();
                            }else {
                                new AlertDialog.Builder(GalleriaActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(getString(R.string.imagenes_not_found))
                                        .setCancelable(false)
                                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                onBackPressed();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString(), e);
                        }
                        hidepDialog(pDialog);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(error.toString(), error);
                        hidepDialog(pDialog);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(idEvento));
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }


    public void iniciarPDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.enviando_peticion));
        pDialog.setCancelable(false);
    }



}
