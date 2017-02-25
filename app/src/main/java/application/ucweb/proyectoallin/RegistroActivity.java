package application.ucweb.proyectoallin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.model.zona.Departamento;
import application.ucweb.proyectoallin.model.zona.Distrito;
import application.ucweb.proyectoallin.model.zona.Provincia;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import application.ucweb.proyectoallin.util.Preferencia;
import application.ucweb.proyectoallin.util.Util;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import io.blackbox_vision.datetimepickeredittext.view.DatePickerEditText;
import io.realm.Realm;
import io.realm.RealmResults;

public class RegistroActivity extends BaseActivity {
    private static final String TAG = RegistroActivity.class.getSimpleName();
    @BindView(R.id.drawer_layout) RelativeLayout layout;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.tvDescripcionToolbar)TextView tvRegistroC;
    @BindView(R.id.idiv_layout_registro_cliente)ImageView ivFondoRegistroC;

    @BindView(R.id.etNombresRegistro) EditText etNombres;
    @BindView(R.id.etApellidoPRegistro) EditText etApellidoP;
    @BindView(R.id.etApellidoMRegistro) EditText etApellidoM;
    @BindView(R.id.dpRegistro) DatePickerEditText dpeRegistro;
    @BindView(R.id.etDniRegistro) EditText tvDni;
    @BindView(R.id.spSexoRegistro) Spinner spSexo;
    @BindView(R.id.spEstadoCivilRegistro) Spinner spEstadoCivil;
    @BindView(R.id.spDepartamentoRegistro) Spinner spDepartamento;
    @BindView(R.id.spProvinciaRegistro) Spinner spProvincia;
    @BindView(R.id.spDistritoRegistro) Spinner spDistrito;
    @BindView(R.id.etDireccionRegistro) EditText etDireccion;
    @BindView(R.id.etNumeroMovilRegistro) EditText etNumeroMovil;
    @BindView(R.id.spOperadorRegistro) Spinner spOperador;
    @BindView(R.id.rbVisaRegistro) RadioButton rbVisa;
    @BindView(R.id.rbMastercadRegistro) RadioButton rbMastercard;
    @BindView(R.id.etCorreoRegistro) EditText etCorreo;
    @BindView(R.id.etContraseniaRegistro) EditText etContrasenia;
    @BindView(R.id.tvAvatarRegistro) TextView tvAvatar;
    @BindView(R.id.rbSiRecibirRegistro) RadioButton rbSi;
    @BindView(R.id.ivImagenRegistro) ImageView ivImagen;
    private static int IMAGEN_VALOR = 1;
    private String imagen_code = "";
    private String imagen_facebook = "";
    private ProgressDialog pDialog;
    public static final int WRITE_PERMISSION = 0x01;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        realm = Realm.getDefaultInstance();
        iniciarPD();
        iniciarLayout();
        dpeRegistro.setManager(getSupportFragmentManager());
        requestDepartamentoTotal();
        if (getIntent().hasExtra(Constantes.K_FOTO_FB)) {
            imagen_facebook = getIntent().getStringExtra(Constantes.K_FOTO_FB);
            String nombre = getIntent().getStringExtra(Constantes.K_NOMBRE_FB);
            String apellido = getIntent().getStringExtra(Constantes.K_APELLIDO_FB);
            String correo = getIntent().getStringExtra(Constantes.K_CORREO_FB);
            String genero = getIntent().getStringExtra(Constantes.K_GENERO_FB);
            String apellido_m = getIntent().getStringExtra(Constantes.K_APELLIDO_M_FB);
            spSexo.setSelection(genero.equals("male") ? 0 : 1);
            etNombres.setText(nombre);
            etApellidoP.setText(apellido);
            etApellidoM.setText(apellido_m);
            tvAvatar.setText("HECHO");
            tvAvatar.setTextColor(Color.GREEN);
            etCorreo.setEnabled(false);
            etCorreo.setText(correo);
            usarGlide(this, imagen_facebook, ivImagen);
        }
    }

    @OnClick(R.id.btnRegistroCliente)
    public void irAPrincipal() {
        if (camposVacios()) {
            if (ConexionBroadcastReceiver.isConect()) {
                requestRegistrarUsuario();
            } else ConexionBroadcastReceiver.showSnack(layout, this);
        } else Toast.makeText(this, R.string.campos_vacios, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.ivImagenRegistro)
    public void cargarImagen() {
        if (!getIntent().hasExtra(Constantes.K_FOTO_FB)) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
                else
                    startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), IMAGEN_VALOR);
            } else startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), IMAGEN_VALOR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGEN_VALOR && resultCode == RESULT_OK && data != null) {
            Uri imagen_seleccionada = data.getData();
            String path = Util.getPath(imagen_seleccionada, this);
            Log.d(TAG, path);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                bitmap.recycle();
                byte[] mi_bytes = stream.toByteArray();
                String imagen_encode = Base64.encodeToString(mi_bytes, Base64.DEFAULT);
                Log.d(TAG, imagen_encode);
                imagen_code = imagen_encode;
                tvAvatar.setText("HECHO");
                tvAvatar.setTextColor(Color.GREEN);
            } else {
                tvAvatar.setText("ERROR");
                tvAvatar.setTextColor(Color.RED);
            }
        }
    }

    private void requestRegistrarUsuario() {
        final String token = new Preferencia(this).getToken();
        final String rec_ofertas = rbSi.isChecked() ? "0" : "1";
        final String date = new SimpleDateFormat("yyyy-MM-dd").format(dpeRegistro.getDate().getTime());
        final String tarjeta = !rbVisa.isChecked() && !rbMastercard.isChecked() ? "Ninguno" : (rbVisa.isChecked() ? "Visa" : "Mastercard");
        final String nombre = etNombres.getText().toString().trim();
        final String apellido_p = etApellidoP.getText().toString().trim();
        final String apellido_m = etApellidoM.getText().toString().trim();
        final String sexo = spSexo.getSelectedItem().toString().equals("Masculino") ? "M" : "F";
        final String estado_civil = spEstadoCivil.getSelectedItem().toString();
        final String direccion = etDireccion.getText().toString();
        final String num_movil = etNumeroMovil.getText().toString();
        final String num_operador = spOperador.getSelectedItem().toString();
        final String correo = etCorreo.getText().toString();
        final String password = etContrasenia.getText().toString();
        final String dni_carnet = tvDni.getText().toString();
        final String departamento = spDepartamento.getSelectedItem().toString();
        final String provincia = spProvincia.getSelectedItem().toString();
        final String distrito = spDistrito.getSelectedItem().toString();
        pDialog.setMessage(getString(R.string.enviando_peticion));
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.REGISTRAR_USUARIO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, response);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jData = jsonObject.getJSONObject("data");
                            int codigo = jsonObject.has("codigo") ? jsonObject.getInt("codigo") : 0;
                            if (codigo == 200) {
                                Usuario usuario = new Usuario();
                                usuario.setId(Usuario.ID_DEFAULT);
                                usuario.setId_server(jData.getInt("id"));
                                usuario.setNombre(nombre);
                                usuario.setApellido_p(apellido_p);
                                usuario.setApellido_m(apellido_m);
                                usuario.setFecha_nac(date);
                                usuario.setDni(dni_carnet);
                                usuario.setSexo(sexo);
                                usuario.setEstado_civil(estado_civil);
                                usuario.setDepartamento(departamento);
                                usuario.setProvincia(provincia);
                                usuario.setDistrito(distrito);
                                usuario.setDireccion(direccion);
                                usuario.setNum_movil(num_movil);
                                usuario.setOperador_movil(num_operador);
                                usuario.setTarjeta_credito(tarjeta);
                                usuario.setCorreo(correo);
                                usuario.setFoto(jData.getString("imagen"));
                                usuario.setRecibir_oferta(rec_ofertas.equals("1"));
                                usuario.setPuntos(0);
                                Usuario.iniciarSesion(usuario);

                                hidepDialog(pDialog);
                                startActivity(new Intent(RegistroActivity.this, PrincipalActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            } else if (codigo == -3){
                                hidepDialog(pDialog);
                                new AlertDialog.Builder(RegistroActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(R.string.registrar_error_3)
                                        .setPositiveButton(R.string.aceptar, null)
                                        .show();
                            } else if (codigo == -2){
                                hidepDialog(pDialog);
                                new AlertDialog.Builder(RegistroActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(R.string.registrar_error_2)
                                        .setPositiveButton(R.string.aceptar, null)
                                        .show();
                            } else {
                                hidepDialog(pDialog);
                                new AlertDialog.Builder(RegistroActivity.this)
                                        .setTitle(R.string.app_name)
                                        .setMessage(R.string.registrar_error)
                                        .setPositiveButton(R.string.aceptar, null)
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            hidepDialog(pDialog);
                            errorConexion(RegistroActivity.this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidepDialog(pDialog);
                        errorConexion(RegistroActivity.this);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("apellido_paterno", apellido_p);
                params.put("apellido_materno", apellido_m);
                params.put("fec_nacimiento", date);
                params.put("sexo", sexo);
                params.put("est_civil", estado_civil);
                params.put("direccion", direccion);
                params.put("num_movil", num_movil);
                params.put("num_operador", num_operador);
                params.put("tip_tarjeta", tarjeta);
                params.put("correo", correo);
                params.put("password", password);
                params.put("imagen", imagen_code);
                params.put("imagen_facebook", imagen_facebook);
                params.put("rec_ofertas", rec_ofertas);
                params.put("token", token);
                params.put("dni_carnet", dni_carnet);
                params.put("departamento", departamento);
                params.put("provincia", provincia);
                params.put("distrito", distrito);
                Log.d(TAG, params.toString());
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    @OnItemSelected(R.id.spDepartamentoRegistro)
    public void itemspDepartamentoRegistro(int position) {
        String departamento_nom = spDepartamento.getSelectedItem().toString();
        int fk_provincia = -1;
        RealmResults<Departamento> departamentos = realm.where(Departamento.class).findAll();
        for (Departamento item : departamentos) {
            if (item.getNombre().equalsIgnoreCase(departamento_nom)) fk_provincia = (int) item.getId();
        }
        setSpinner(this, spProvincia, Provincia.getProvincias(fk_provincia));
    }

    @OnItemSelected(R.id.spProvinciaRegistro)
    public void itemspDistritoRegistro(int position) {
        String provincia_nom = spProvincia.getSelectedItem().toString();
        int fk_provincia = -1;
        RealmResults<Provincia> provincias = realm.where(Provincia.class).findAll();
        for (Provincia item : provincias) {
            if (item.getNombre().equalsIgnoreCase(provincia_nom)) fk_provincia = item.getId_server();
        }
        setSpinner(this, spDistrito, Distrito.getDistritos(fk_provincia));
    }

    private void iniciarPD() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.cargando));
    }

    private void iniciarLayout() {
        BaseActivity.setFondoActivity(this, ivFondoRegistroC);
        BaseActivity.setToolbarSon(toolbar, this, icono_toolbar);
        tvRegistroC.setText(R.string.registro_cliente);
        BaseActivity.setSpinner(this, spSexo, R.array.arraySexo);
        BaseActivity.setSpinner(this, spOperador, R.array.arrayOperador);
        BaseActivity.setSpinner(this, spEstadoCivil, R.array.arrayEstadoCivil);
        setSpinner(this, spDepartamento, Departamento.getDepartamentos());
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
            }
        }
    }

    private boolean camposVacios() {
        return !etNombres.getText().toString().isEmpty() &&
                !etApellidoP.getText().toString().isEmpty() &&
                !etApellidoM.getText().toString().isEmpty() &&
                !spDepartamento.getSelectedItem().toString().isEmpty() &&
                !spProvincia.getSelectedItem().toString().isEmpty() &&
                !spDistrito.getSelectedItem().toString().isEmpty() &&
                !etDireccion.getText().toString().isEmpty() &&
                !etNumeroMovil.getText().toString().isEmpty() &&
                !etContrasenia.getText().toString().isEmpty() &&
                !imagen_code.isEmpty() || !imagen_facebook.isEmpty() &&
                !dpeRegistro.getText().toString().isEmpty();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }
}
