package application.ucweb.proyectoallin.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.model.zona.Departamento;
import application.ucweb.proyectoallin.model.zona.Distrito;
import application.ucweb.proyectoallin.model.zona.Provincia;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import application.ucweb.proyectoallin.util.Preferencia;
import application.ucweb.proyectoallin.util.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import io.blackbox_vision.datetimepickeredittext.view.DatePickerEditText;
import io.realm.Realm;
import io.realm.RealmResults;

import static application.ucweb.proyectoallin.RegistroActivity.WRITE_PERMISSION;
import static application.ucweb.proyectoallin.aplicacion.BaseActivity.hidepDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditarPerfilFragment extends Fragment implements IActividad {
    private static final String TAG = EditarPerfilFragment.class.getSimpleName();
    @BindView(R.id.tvDescripcionToolbar) TextView tvEditarPerfilF;
    @BindView(R.id.layout_fragment_editar_perfil) LinearLayout layout;
    @BindView(R.id.tvAvatarEditar) TextView tvAvatar;
    @BindView(R.id.etNombreEditar) EditText etNombre;
    @BindView(R.id.etApellidoPEditar) EditText etApellidoP;
    @BindView(R.id.etApellidoMEditar) EditText etApellidoM;
    @BindView(R.id.txtFechaNacEditar) DatePickerEditText etFechaNac;
    @BindView(R.id.etDniEditar) EditText etDni;
    @BindView(R.id.spDepartamentoEditar)Spinner spDepartamento;
    @BindView(R.id.spProvinciaEditar)Spinner spProvincia;
    @BindView(R.id.spDistritoEditar)Spinner spDistrito;
    @BindView(R.id.spSexoEditar) Spinner spSexo;
    @BindView(R.id.spEstadoCivilEditar) Spinner spEstadoCivil;
    @BindView(R.id.etDireccionEditar) EditText etDireccion;
    @BindView(R.id.etNumeroEditar) EditText etNumero;
    @BindView(R.id.spOperadorEditar)Spinner spOperador;
    @BindView(R.id.rbVisaEditar) RadioButton rbVisa;
    @BindView(R.id.rbMastercardEditar) RadioButton rbMastercard;
    @BindView(R.id.etCorreoEditar) EditText etCorreo;
    @BindView(R.id.etContraseniaEditar) EditText etContrasenia;
    @BindView(R.id.ivImagenEditar) ImageView ivImagen;
    @BindView(R.id.rbSiRecibirEditar) RadioButton rbSiRecibir;
    @BindView(R.id.rbNoRecibirEditar) RadioButton rbNoRecibir;
    private String imagen_code = "";
    private ProgressDialog pDialog;
    private Realm realm;
    private static int IMAGEN_VALOR = 1;

    public EditarPerfilFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        iniciarPDialog();
        requestDepartamentoTotal();
        iniciarLayout();
        if (isSesion()) sesion();
        return view;
    }

    @OnItemSelected(R.id.spDepartamentoEditar)
    public void itemspDepartamentoRegistro(int position) {
        String departamento_nom = spDepartamento.getSelectedItem().toString();
        int fk_provincia = -1;
        RealmResults<Departamento> departamentos = realm.where(Departamento.class).findAll();
        for (Departamento item : departamentos) {
            if (item.getNombre().equals(departamento_nom)) fk_provincia = (int) item.getId();
        }
        BaseActivity.setSpinner(getActivity(), spProvincia, Provincia.getProvincias(fk_provincia));
    }

    @OnItemSelected(R.id.spProvinciaEditar)
    public void itemspDistritoRegistro(int position) {
        String provincia_nom = spProvincia.getSelectedItem().toString();
        int fk_provincia = -1;
        RealmResults<Provincia> provincias = realm.where(Provincia.class).findAll();
        for (Provincia item : provincias) {
            if (item.getNombre().equals(provincia_nom)) fk_provincia = item.getId_server();
        }
        BaseActivity.setSpinner(getActivity(), spDistrito, Distrito.getDistritos(fk_provincia));
    }

    private void requestDepartamentoTotal() {
        if (Departamento.isEmpty()) {
            if (ConexionBroadcastReceiver.isConect()) {
                BaseActivity.showDialog(pDialog);
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
                                    BaseActivity.hidepDialog(pDialog);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    BaseActivity.hidepDialog(pDialog);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e(error.toString(), error);
                                BaseActivity.hidepDialog(pDialog);
                            }
                        }
                );
            }
        }
    }

    @OnClick(R.id.btnEditarUsuario)
    public void editarUsuario(){
        if (camposVacios()) {
            if (ConexionBroadcastReceiver.isConect()) requestEditarUsuario();
            else
                ConexionBroadcastReceiver.showSnack(layout, getActivity());
        } else
            Toast.makeText(getActivity().getApplicationContext(), R.string.campos_vacios, Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.ivImagenEditar)
    public void cargarImagen() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
            else
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), IMAGEN_VALOR);
        } else
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), IMAGEN_VALOR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGEN_VALOR && resultCode == -1 && data != null) {
            Uri imagen_seleccionada = data.getData();
            String path = Util.getPath(imagen_seleccionada, getActivity());
            Log.d(TAG, path);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                bitmap.recycle();
                byte[] mi_bytes = stream.toByteArray();
                String imagen_encode = Base64.encodeToString(mi_bytes, Base64.DEFAULT);
                Log.d(TAG, imagen_encode);
                this.imagen_code = imagen_encode;
                Glide.with(this).load(new File(path)).into(ivImagen);
            } else
                Toast.makeText(getActivity(), R.string.imagen_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void sesion() {
        Usuario usuario = Usuario.getUsuario();
        etNombre.setText(usuario.getNombre());
        etApellidoP.setText(usuario.getApellido_p());
        etApellidoM.setText(usuario.getApellido_m());
        etFechaNac.setText(usuario.getFecha_nac());
        etDni.setText(usuario.getDni());
        spDepartamento.setSelection(Departamento.getIdDepartamento(usuario.getDepartamento()) - 1);
        spSexo.setSelection(usuario.getSexo().equalsIgnoreCase("M") ? 0 : 1);
        etDireccion.setText(usuario.getDireccion());
        etNumero.setText(usuario.getNum_movil());
        spOperador.setSelection(usuario.getOperador_movil().equalsIgnoreCase("movistar") ? 0 : (usuario.getOperador_movil().equalsIgnoreCase("claro") ? 1 : 2));
        rbVisa.setChecked(usuario.getTarjeta_credito().equalsIgnoreCase("visa"));
        rbMastercard.setChecked(usuario.getTarjeta_credito().equalsIgnoreCase("mastercard"));
        etCorreo.setText(usuario.getCorreo());
        BaseActivity.setGlide(getActivity(), usuario.getFoto(), ivImagen);
        if (usuario.isRecibir_oferta()) rbSiRecibir.setChecked(true); else rbNoRecibir.setChecked(true);
    }

    @OnItemSelected(R.id.spDepartamentoEditar)
    public void itemspDepartamentoEditar(int position) {
        String departamento_nom = spDepartamento.getSelectedItem().toString();
        int fk_provincia = -1;
        RealmResults<Departamento> departamentos = realm.where(Departamento.class).findAll();
        for (Departamento item : departamentos) {
            if (item.getNombre().equalsIgnoreCase(departamento_nom)) fk_provincia = (int) item.getId();
        }
        BaseActivity.setSpinner(getActivity(), spProvincia, Provincia.getProvincias(fk_provincia));
    }

    @OnItemSelected(R.id.spProvinciaEditar)
    public void itemspDistritoEditar(int position) {
        String provincia_nom = spProvincia.getSelectedItem().toString();
        int fk_provincia = -1;
        RealmResults<Provincia> provincias = realm.where(Provincia.class).findAll();
        for (Provincia item : provincias) {
            if (item.getNombre().equalsIgnoreCase(provincia_nom)) fk_provincia = item.getId_server();
        }
        List<String> lista = Distrito.getDistritos(fk_provincia);
        BaseActivity.setSpinner(getActivity(), spDistrito, lista);
        int focus = 0;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).equalsIgnoreCase(Usuario.getUsuario().getDistrito())) focus = i;
        }
        spDistrito.setSelection(focus);
    }

    private boolean camposVacios() {
        return !etNombre.getText().toString().isEmpty() &&
                !etApellidoP.getText().toString().isEmpty() &&
                !etApellidoM.getText().toString().isEmpty() &&
                !spDepartamento.getSelectedItem().toString().isEmpty() &&
                !spProvincia.getSelectedItem().toString().isEmpty() &&
                !spDistrito.getSelectedItem().toString().isEmpty() &&
                !etDireccion.getText().toString().isEmpty() &&
                !etNumero.getText().toString().isEmpty() &&
                !etContrasenia.getText().toString().isEmpty() &&
                !imagen_code.isEmpty()&&
                !etFechaNac.getText().toString().isEmpty();
    }

    private void requestEditarUsuario() {
        BaseActivity.showDialog(pDialog);
        final String token = new Preferencia(getActivity()).getToken();
        final String rec_ofertas = rbSiRecibir.isChecked() ? "0" : "1";
        final String date = etFechaNac.getText().toString();
        final String tarjeta = !rbVisa.isChecked() && !rbMastercard.isChecked() ? "Ninguno" : (rbVisa.isChecked() ? "Visa" : "Mastercard");
        final String nombre = etNombre.getText().toString().trim();
        final String apellido_p = etApellidoP.getText().toString().trim();
        final String apellido_m = etApellidoM.getText().toString().trim();
        final String sexo = spSexo.getSelectedItem().toString().equals("Masculino") ? "M" : "F";
        final String estado_civil = spEstadoCivil.getSelectedItem().toString();
        final String direccion = etDireccion.getText().toString();
        final String num_movil = etNumero.getText().toString();
        final String num_operador = spOperador.getSelectedItem().toString();
        final String correo = Usuario.getUsuario().getCorreo();
        final String password = etContrasenia.getText().toString();
        final String dni_carnet = etDni.getText().toString();
        final String departamento = spDepartamento.getSelectedItem().toString();
        final String provincia = spProvincia.getSelectedItem().toString();
        final String distrito = spDistrito.getSelectedItem().toString();
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.EDITAR_USUARIO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int codigo = jsonObject.getInt("codigo");
                            if (codigo == 200) {
                                Realm realm = Realm.getDefaultInstance();
                                Usuario old_user = Usuario.getUsuario();
                                Usuario usuario = realm.where(Usuario.class).equalTo(Usuario.ID, Usuario.ID_DEFAULT).findFirst();
                                realm.beginTransaction();
                                usuario.setId(Usuario.ID_DEFAULT);
                                usuario.setId_server(old_user.getId_server());
                                usuario.setNombre(nombre);
                                usuario.setApellido_p(apellido_p);
                                usuario.setApellido_m(apellido_m);
                                usuario.setFecha_nac(date);
                                usuario.setSexo(sexo);
                                usuario.setEstado_civil(estado_civil);
                                usuario.setDepartamento(departamento);
                                usuario.setProvincia(provincia);
                                usuario.setDistrito(distrito);
                                usuario.setNum_movil(num_movil);
                                usuario.setOperador_movil(num_operador);
                                usuario.setTarjeta_credito(tarjeta);
                                usuario.setCorreo(old_user.getCorreo());
                                usuario.setFoto(jsonObject.getString("foto"));
                                usuario.setRecibir_oferta(Boolean.parseBoolean(rec_ofertas));
                                realm.commitTransaction();
                                realm.close();
                                hidepDialog(pDialog);
                            } else if (codigo == 6) {
                                hidepDialog(pDialog);
                                new AlertDialog.Builder(getActivity())
                                        .setTitle(R.string.app_name)
                                        .setMessage(getString(R.string.imagen_error))
                                        .setPositiveButton(R.string.aceptar, null)
                                        .show();
                            } else {
                                hidepDialog(pDialog);
                                BaseActivity.errorConexion(getActivity());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString(), e);
                            BaseActivity.errorConexion(getActivity());
                            hidepDialog(pDialog);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e(TAG, error.toString(), error);
                        BaseActivity.errorConexion(getActivity());
                        hidepDialog(pDialog);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("apellido_p", apellido_p);
                params.put("apellido_m", apellido_m);
                params.put("dni", dni_carnet);
                params.put("sexo", sexo);
                params.put("estado_civil", estado_civil);
                params.put("departamento", departamento);
                params.put("provincia", provincia);
                params.put("distrito", distrito);
                params.put("direccion", direccion);
                params.put("numero_movil", num_movil);
                params.put("numero_operador", num_operador);
                params.put("tarjeta", tarjeta);
                params.put("correo", correo);
                params.put("contrasenia", password);
                params.put("foto", imagen_code);
                params.put("recibir_ofertas", rec_ofertas);
                params.put("token", token);
                Log.d(TAG, params.toString());
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }

    @Override
    public boolean isSesion() {
        return Usuario.getUsuario() != null && Usuario.getUsuario().isSesion();
    }

    @Override
    public void iniciarLayout() {
        tvEditarPerfilF.setText(R.string.editar_perfil);
        BaseActivity.setSpinner(getActivity(), spSexo, R.array.arraySexo);
        BaseActivity.setSpinner(getActivity(), spDepartamento, Departamento.getDepartamentos());
        BaseActivity.setSpinner(getActivity(), spEstadoCivil, R.array.arrayEstadoCivil);
        BaseActivity.setSpinner(getActivity(), spOperador, R.array.arrayOperador);
    }

    @Override
    public void iniciarPDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.actualizando));
        pDialog.setCancelable(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }
}
