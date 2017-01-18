package application.ucweb.proyectoallin.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.zona.Departamento;
import application.ucweb.proyectoallin.model.zona.Distrito;
import application.ucweb.proyectoallin.model.zona.Provincia;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import application.ucweb.proyectoallin.util.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import io.blackbox_vision.datetimepickeredittext.view.DatePickerEditText;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditarPerfilFragment extends Fragment {
    @BindView(R.id.spSexo) Spinner spSexo;
    @BindView(R.id.spDepartamentoEditar)Spinner spDepartamento;
    @BindView(R.id.spProvinciaEditar)Spinner spProvincia;
    @BindView(R.id.spDistritoEditar)Spinner spDistrito;
    @BindView(R.id.spOperador)Spinner spOperador;
    @BindView(R.id.spEstadoCivil)Spinner spEstadoCivil;
    @BindView(R.id.tvDescripcionToolbar) TextView tvEditarPerfilF;
    @BindView(R.id.txtFechaNac) DatePickerEditText tvCalendario;

    @BindView(R.id.etUsuarioEditar) EditText etUsuario;
    @BindView(R.id.etApellidoPEditar) EditText etApellidoP;
    @BindView(R.id.etApellidoMEditar) EditText etApellidoM;
    private ProgressDialog pDialog;
    private Realm realm;

    public EditarPerfilFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        iniciarPDialog();
        requestDepartamentoTotal();
        iniciarLayout();
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

    private void iniciarLayout() {
        BaseActivity.setSpinner(getContext(), spSexo, R.array.arraySexo);
        BaseActivity.setSpinner(getContext(), spDepartamento, R.array.arrayDepartamento);
        BaseActivity.setSpinner(getContext(), spProvincia, R.array.arrayProvincia);
        tvEditarPerfilF.setText(R.string.editar_perfil);
        BaseActivity.setSpinner(getActivity(), spDepartamento, Departamento.getDepartamentos());
    }

    private void iniciarPDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.actualizando));
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
}
