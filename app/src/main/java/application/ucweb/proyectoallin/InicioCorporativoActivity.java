package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Corporativo;
import application.ucweb.proyectoallin.model.ItemSimpleCorporativo;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.BindView;
import butterknife.OnClick;

public class InicioCorporativoActivity extends BaseActivity implements IActividad{
    private static final String TAG = InicioCorporativoActivity.class.getSimpleName();
    @BindView(R.id.toolbar_inicio_corporativo) Toolbar toolbar;
    @BindView(R.id.idiv_layout_inicio_corporativo) ImageView fondo;
    @BindView(R.id.etUsuarioCorporativo) EditText etUsuario;
    @BindView(R.id.etContraseniaCorporativo) EditText etContrasenia;
    @BindView(R.id.layout_inicio_corporativo) RelativeLayout layout;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_corporativo);

        iniciarLayout();
        iniciarPDialog();
    }

    @OnClick(R.id.ll_btn_ingresar_coporativo)
    public void ingresar_coporativo() {
        requestIniciarSesion();
    }

    private void requestIniciarSesion() {
        if (ConexionBroadcastReceiver.isConect()) {
            if (validar()) {
                showDialog(pDialog);
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        Constantes.INICIAR_SESION_CORPORATIVO,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("status")) {
                                        JSONObject jData = jsonObject.getJSONObject("data");

                                        final ArrayList<ItemSimpleCorporativo> lista = new ArrayList<>();

                                        JSONArray jLocal = jData.getJSONArray("local");
                                        for (int i = 0; i < jLocal.length(); i++) {
                                            if (jLocal.getJSONObject(i).has("LOC_NOMBRE"))
                                            lista.add(new ItemSimpleCorporativo(jLocal.getJSONObject(i).getString("LOC_NOMBRE"), jLocal.getJSONObject(i).getInt("LOC_ID"), ItemSimpleCorporativo.LOCAL));
                                        }
                                        JSONArray jEvento = jData.getJSONArray("evento");
                                        for (int i = 0; i < jEvento.length(); i++) {
                                            if (jEvento.getJSONObject(i).has("EVE_NOMBRE"))
                                            lista.add(new ItemSimpleCorporativo(jLocal.getJSONObject(i).getString("EVE_NOMBRE"), jLocal.getJSONObject(i).getInt("EVE_ID"), ItemSimpleCorporativo.EVENTO));
                                        }
                                        ArrayList<String> strings = new ArrayList<>();
                                        for (int i = 0; i < lista.size(); i++) {
                                            strings.add(lista.get(i).getNombre());
                                        }
                                        hidepDialog(pDialog);
                                        if (!lista.isEmpty()) {
                                            final JSONObject jUsuario   = jData.getJSONObject("usuario");
                                            final String nombre         = jUsuario.getString("CON_NOMBRE");
                                            final int id_server         = jUsuario.getInt("CON_ID");
                                            new AlertDialog.Builder(InicioCorporativoActivity.this)
                                                    .setTitle(R.string.app_name)
                                                    .setSingleChoiceItems(strings.toArray(new String[strings.size()]), -1, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            ItemSimpleCorporativo item = lista.get(which);

                                                            Corporativo corporativo = new Corporativo();
                                                            corporativo.setId(Corporativo.ID_DEFAULT);
                                                            corporativo.setId_local(item.getTipo() == ItemSimpleCorporativo.LOCAL ? item.getId() : ItemSimpleCorporativo.NOT_FOUND);
                                                            corporativo.setId_evento(item.getTipo() == ItemSimpleCorporativo.EVENTO ? item.getId() : ItemSimpleCorporativo.NOT_FOUND);
                                                            corporativo.setNombre(nombre);
                                                            corporativo.setSesion(true);
                                                            corporativo.setId_server(id_server);
                                                            corporativo.setLoc_nombre(lista.get(which).getNombre());
                                                            Corporativo.iniciarSesion(corporativo);
                                                            dialog.dismiss();
                                                            startActivity(new Intent(InicioCorporativoActivity.this, MenuCorporativo.class)
                                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            hidepDialog(pDialog);
                                            new AlertDialog.Builder(InicioCorporativoActivity.this)
                                                    .setTitle(R.string.app_name)
                                                    .setMessage(getString(R.string.lista_establecimiento_vacio))
                                                    .setPositiveButton(R.string.aceptar, null)
                                                    .show();
                                        }
                                    } else {
                                        hidepDialog(pDialog);
                                        new AlertDialog.Builder(InicioCorporativoActivity.this)
                                                .setTitle(R.string.app_name)
                                                .setMessage(getString(R.string.iniciarsesion_error))
                                                .setPositiveButton(R.string.aceptar, null)
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    hidepDialog(pDialog);
                                    Log.e(TAG, e.toString(), e);
                                    errorConexion(InicioCorporativoActivity.this);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                hidepDialog(pDialog);
                                VolleyLog.e(TAG, error.toString(), error);
                                errorConexion(InicioCorporativoActivity.this);
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("correo", etUsuario.getText().toString().trim());
                        params.put("password", etContrasenia.getText().toString().trim());
                        return params;
                    }
                };
                Configuracion.getInstance().addToRequestQueue(request, TAG);
            } else
                Toast.makeText(this, R.string.campos_vacios, Toast.LENGTH_SHORT).show();
        } else
            ConexionBroadcastReceiver.showSnack(layout, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_corporativo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mn_corporativo) {
            startActivity(new Intent(this, InicioActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validar() {
        return !etUsuario.getText().toString().isEmpty() && !etContrasenia.getText().toString().isEmpty();
    }

    @Override
    public boolean isSesion() {
        return false;
    }

    @Override
    public void iniciarLayout() {
        setToolbarSonB(toolbar, this);
        usarGlide(this, R.drawable.fondo_allin, fondo);
    }

    @Override
    public void iniciarPDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.iniciar_sesion));
        pDialog.setCancelable(false);
    }
}
