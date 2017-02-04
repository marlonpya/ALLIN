package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoallin.apis.FacebookApi;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Corporativo;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.model.zona.Departamento;
import application.ucweb.proyectoallin.model.zona.Distrito;
import application.ucweb.proyectoallin.model.zona.Provincia;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import application.ucweb.proyectoallin.util.Util;
import butterknife.BindView;
import butterknife.OnClick;

public class InicioActivity extends BaseActivity implements IActividad {
    private static final String TAG = InicioActivity.class.getSimpleName();
    @BindView(R.id.sontoolbarblack) Toolbar toolbar;
    @BindView(R.id.drawer_layout) View layout;
    @BindView(R.id.etUsuarioRegistro) EditText etUsuario;
    @BindView(R.id.etPasswordRegistro) EditText etPassword;
    @BindView(R.id.imageView) ImageView ivImagenLogoPrincipal;
    @BindView(R.id.idiv_layout_activity_main) ImageView ivFondoActivityM;
    @BindView(R.id.btnIniciarSesionFB) LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        callbackManager = CallbackManager.Factory.create();
        iniciarLayout();
        iniciarPDialog();
        requestDepartamentoTotal();
        FacebookApi.cerrarSesion();

        if (FacebookApi.conectado() || isSesion())
            startActivity(new Intent(this, PrincipalActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

        if (Corporativo.getCorporativo() != null && Corporativo.getCorporativo().isSesion())
            startActivity(new Intent(this, MenuCorporativo.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void iniciarPDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setTitle(R.string.app_name);
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.actualizando));
    }

    @OnClick(R.id.btnIniciarSesion)
    public void irAPrincipal() {
        if (validarCampos()) {
            if (ConexionBroadcastReceiver.isConect()) requestIniciarSesion();
            else
                ConexionBroadcastReceiver.showSnack(layout, this);
        } else
            startActivity(new Intent(this, PrincipalActivity.class));
    }

    private void requestIniciarSesion() {
        pDialog.setMessage(getString(R.string.iniciando_sesion));
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.INICIAR_SESION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jData = jsonObject.getJSONObject("data");
                            if (jsonObject.getBoolean("status")) {
                                Usuario usuario = new Usuario();
                                usuario.setId(Usuario.ID_DEFAULT);
                                usuario.setId_server(jData.getInt("USU_ID"));
                                usuario.setNombre(jData.getString("USU_NOMBRE"));
                                usuario.setApellido_p(jData.getString("USU_APE_PATERNO"));
                                usuario.setApellido_m(jData.getString("USU_APE_MATERNO"));
                                usuario.setFecha_nac(jData.getString("USU_FEC_NACIMIENTO"));
                                usuario.setDni(jData.getString("USU_DNI_CARNET"));
                                usuario.setSexo(jData.getString("USU_SEXO"));
                                usuario.setEstado_civil(jData.getString("USU_EST_CIVIL"));
                                usuario.setDepartamento(jData.getString("USU_DEP"));
                                usuario.setProvincia(jData.getString("USU_PRO"));
                                usuario.setDistrito(jData.getString("USU_DIS"));
                                usuario.setDireccion(jData.getString("USU_DIRECCION"));
                                usuario.setNum_movil(jData.getString("USU_NUM_MOVIL"));
                                usuario.setOperador_movil(jData.getString("USU_NUM_OPERADOR"));
                                usuario.setTarjeta_credito(jData.getString("USU_TIP_TARJETA"));
                                usuario.setCorreo(jData.getString("USU_CORREO"));
                                usuario.setFoto(jData.getString("USU_IMAGEN"));
                                usuario.setRecibir_oferta(jData.getInt("USU_REC_OFERTAS") == 1);
                                usuario.setPuntos(jData.getInt("USU_PUNTOS_ALLIN"));
                                Usuario.iniciarSesion(usuario);

                                startActivity(new Intent(InicioActivity.this, PrincipalActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            } else {
                                new AlertDialog.Builder(InicioActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(R.string.iniciarsesion_error)
                                        .setPositiveButton(R.string.aceptar, null)
                                        .show();
                            }
                            hidepDialog(pDialog);
                        } catch (JSONException e) {
                            Log.e(TAG, e.toString(), e);
                            hidepDialog(pDialog);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(error.toString(), error);
                        errorConexion(InicioActivity.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("correo", etUsuario.getText().toString().trim());
                params.put("password", etPassword.getText().toString().trim());
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    @OnClick(R.id.btnRegistro)
    public void irARegistro() {
        startActivity(new Intent(this, RegistroActivity.class));
    }

    @OnClick(R.id.btnIniciarSesionFB)
    public void intentIniciarSesionFB() {
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        getFacebookData(object);
                        Log.d(TAG, object.toString());
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, first_name, last_name, email, gender, middle_name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                errorConexion(InicioActivity.this);
            }
        });
    }

    private void getFacebookData(JSONObject object) {
        String foto = "";
        String nombre = "";
        String apellido = "";
        String id_fb = "";
        String correo = "";
        String genero = "";
        String apellido_m = "";

        try {
            id_fb = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id_fb + "/picture?width=200&height=200");
                Log.i("profile_pic", profile_pic + "");
                foto = profile_pic.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (object.has("first_name")) nombre = object.getString("first_name");
            if (object.has("last_name")) apellido = object.getString("last_name");
            if (object.has("email")) correo = object.getString("email");
            if (object.has("gender")) genero = object.getString("gender");
            //if (object.has("middle_name")) apellido = object.getString("middle_name");

            if (!correo.isEmpty()) requestIniciarSesionFACEBOOK(correo, foto, nombre, apellido, genero, apellido_m);
            else new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setCancelable(false)
                    .setMessage(getString(R.string.fb_contra_error))
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FacebookApi.cerrarSesion();
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FacebookApi.cerrarSesion();
                        }
                    })
                    .show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestIniciarSesionFACEBOOK(final String correo, final String foto, final String nombre, final String apellido, final String genero, final String apellido_m) {
        if (ConexionBroadcastReceiver.isConect()) {
            showDialog(pDialog);
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    Constantes.INICIAR_SESION_FACEBOOK,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("status")) {
                                    JSONObject jData = jsonObject.getJSONObject("data");
                                    JSONObject jUsuario = jData.getJSONObject("usuario");
                                    Usuario usuario = new Usuario();
                                    usuario.setId(Usuario.ID_DEFAULT);
                                    usuario.setId_server(jUsuario.getInt("USU_ID"));
                                    usuario.setNombre(jUsuario.getString("USU_NOMBRE"));
                                    usuario.setApellido_p(jUsuario.getString("USU_APE_PATERNO"));
                                    usuario.setApellido_m(jUsuario.getString("USU_APE_MATERNO"));
                                    usuario.setFecha_nac(jUsuario.getString("USU_FEC_NACIMIENTO"));
                                    usuario.setDni(jUsuario.getString("USU_DNI_CARNET"));
                                    usuario.setSexo(jUsuario.getString("USU_SEXO"));
                                    usuario.setEstado_civil(jUsuario.getString("USU_EST_CIVIL"));
                                    usuario.setDepartamento(jUsuario.getString("USU_DEP"));
                                    usuario.setProvincia(jUsuario.getString("USU_PRO"));
                                    usuario.setDistrito(jUsuario.getString("USU_DIS"));
                                    usuario.setDireccion(jUsuario.getString("USU_DIRECCION"));
                                    usuario.setNum_movil(jUsuario.getString("USU_NUM_MOVIL"));
                                    usuario.setOperador_movil(jUsuario.getString("USU_NUM_OPERADOR"));
                                    usuario.setTarjeta_credito(jUsuario.getString("USU_TIP_TARJETA"));
                                    usuario.setCorreo(jUsuario.getString("USU_CORREO"));
                                    usuario.setFoto(jUsuario.getString("USU_IMAGEN"));
                                    usuario.setRecibir_oferta(jUsuario.getInt("USU_REC_OFERTAS") == 1);
                                    usuario.setPuntos(jUsuario.getInt("USU_PUNTOS_ALLIN"));
                                    Usuario.iniciarSesion(usuario);

                                    hidepDialog(pDialog);
                                    startActivity(new Intent(InicioActivity.this, PrincipalActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                } else {
                                    hidepDialog(pDialog);
                                    startActivity(new Intent(InicioActivity.this, RegistroActivity.class)
                                            .putExtra(Constantes.K_FOTO_FB, foto)
                                            .putExtra(Constantes.K_NOMBRE_FB, nombre)
                                            .putExtra(Constantes.K_APELLIDO_FB, apellido)
                                            .putExtra(Constantes.K_CORREO_FB, correo)
                                            .putExtra(Constantes.K_GENERO_FB, genero)
                                            .putExtra(Constantes.K_APELLIDO_M_FB, apellido_m));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TAG, e.toString(), e);
                                hidepDialog(pDialog);
                                if (FacebookApi.conectado()) FacebookApi.cerrarSesion();
                                errorConexion(InicioActivity.this);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidepDialog(pDialog);
                            VolleyLog.e(error.toString(), error);
                            errorConexion(InicioActivity.this);
                            if (FacebookApi.conectado()) FacebookApi.cerrarSesion();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("correo", correo);
                    params.put("facebook", "facebook");
                    return params;
                }
            };
            Configuracion.getInstance().addToRequestQueue(request, TAG);
        }
    }

    private void requestDepartamentoTotal() {
        if (Departamento.isEmpty()) {
            if (ConexionBroadcastReceiver.isConect()) {
                showDialog(pDialog);
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        Constantes.DEPARTAMENTOS_TOTAL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("status")) {
                                        JSONArray jDepartamento = jsonObject.getJSONArray("departamento");
                                        for (int i = 0; i < jDepartamento.length(); i++) {
                                            Departamento departamento = new Departamento();
                                            departamento.setId(jDepartamento.getJSONObject(i).getInt("DEP_ID"));
                                            departamento.setNombre(jDepartamento.getJSONObject(i).getString("DEP_NOMBRE"));
                                            Departamento.crearDepartamento(departamento);
                                        }
                                        JSONArray jProvincia = jsonObject.getJSONArray("provincia");
                                        for (int i = 0; i < jProvincia.length(); i++) {
                                            Provincia provincia = new Provincia();
                                            provincia.setId(Provincia.getUltimodId());
                                            provincia.setNombre(jProvincia.getJSONObject(i).getString("PRO_NOMBRE"));
                                            provincia.setId_server(jProvincia.getJSONObject(i).getInt("PRO_ID"));
                                            provincia.setDep_id(jProvincia.getJSONObject(i).getInt("DEP_ID"));
                                            Provincia.crearProvincia(provincia);
                                        }
                                        JSONArray jDistrito = jsonObject.getJSONArray("distrito");
                                        for (int i = 0; i < jDistrito.length(); i++) {
                                            Distrito distrito = new Distrito();
                                            distrito.setId(Distrito.getUltimoId());
                                            distrito.setId_server(jDistrito.getJSONObject(i).getInt("DIS_ID"));
                                            distrito.setNombre(jDistrito.getJSONObject(i).getString("DIS_NOMBRE"));
                                            distrito.setPro_id(jDistrito.getJSONObject(i).getInt("PRO_ID"));
                                            Distrito.crearDistrito(distrito);
                                        }
                                    }
                                    hidepDialog(pDialog);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    hidepDialog(pDialog);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e(error.toString(), error);
                                hidepDialog(pDialog);
                            }
                        }
                );
                Configuracion.getInstance().addToRequestQueue(request, TAG);
            }
        }
    }

    private boolean validarCampos() {
        return !etUsuario.getText().toString().trim().isEmpty() && !etPassword.getText().toString().trim().isEmpty();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean isSesion() {
        return Usuario.getUsuario() != null && Usuario.getUsuario().isSesion();
    }

    @Override
    public void iniciarLayout() {
        setToolbarSonB(toolbar, this);
        usarGlide(this, R.drawable.img_principal, ivImagenLogoPrincipal);
        setImagenConGlidePrincipal(this, ivFondoActivityM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mn_usuario) {
            startActivity(new Intent(this, InicioCorporativoActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return true;
    }
}
