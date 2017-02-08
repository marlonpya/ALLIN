package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import application.ucweb.proyectoallin.adapter.MisPuntosCanjeAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.modelparseable.ItemCarrito;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.OnClick;

public class ListaCanjeActivity extends BaseActivity implements IActividad{
    public static final String TAG = ListaCanjeActivity.class.getSimpleName();
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.rv_lista_promociones) RecyclerView recyclerView;
    @BindView(R.id.tv_total_canje) TextView total_compra;
    @BindView(R.id.idiv_layout_canje)ImageView idiv_layout_canje;
    private ProgressDialog pDialog;


    private MisPuntosCanjeAdapter adapter;
    public static double valor_compra = 0.0;
    ArrayList<ItemCarrito> test = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_canje);
        iniciarLayout();test=(ArrayList<ItemCarrito>)getIntent().getSerializableExtra(Constantes.ARRAY_S_CARRITO);
        iniciarRRV();
    }

    private void iniciarRRV() {
        total_compra.setText(String.valueOf(valor_compra));
        adapter = new MisPuntosCanjeAdapter(this, test, total_compra);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btnCanjear)
    public void canjearPuntos(){
        //requestRegistrarCanje();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isSesion() {
        return Usuario.getUsuario() != null && Usuario.getUsuario().isSesion();
    }

    @Override
    public void iniciarLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
        setFondoActivity(this, idiv_layout_canje);
    }

    @Override
    public void iniciarPDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.enviando_peticion));
        pDialog.setCancelable(false);
    }

    private void requestRegistrarCanje(){
        showDialog(pDialog);
        final String nombre = "Canje Allin";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.REGISTRAR_VENTA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("status")){

                            }else {
                                new AlertDialog.Builder(ListaCanjeActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage("Ocurrió un error")
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
                            Log.d(TAG, jsonObject.toString());
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
                params.put("nombre", String.valueOf("abc"));
                params.put("fecha", String.valueOf("abc"));
                params.put("fecha_venta", String.valueOf("abc"));
                params.put("fecha_vencimiento", String.valueOf("abc"));
                params.put("usu_id", String.valueOf("abc"));
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }


}
