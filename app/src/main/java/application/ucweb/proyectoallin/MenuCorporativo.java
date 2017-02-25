package application.ucweb.proyectoallin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import java.util.List;
import java.util.Map;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Corporativo;
import application.ucweb.proyectoallin.model.ItemSimple;
import application.ucweb.proyectoallin.util.Constantes;
import application.ucweb.proyectoallin.util.MaterialInputsDialog;
import butterknife.BindView;
import butterknife.OnClick;

public class MenuCorporativo extends BaseActivity implements IActividad{
    private static final String TAG = MenuCorporativo.class.getSimpleName();
    @BindView(R.id.idiv_layout_usuario_administrado) ImageView fondo;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.iv_imagen_usuario_administrado)ImageView ivImagenUsuarioA;
    @BindView(R.id.btnAdministradorVentas)ImageView btnAdministradorVentas;
    @BindView(R.id.btnAdministradorLista)ImageView btnAdministradorLista;
    @BindView(R.id.btnAdministradorCodigo)ImageView btnAdministradorCodigo;
    @BindView(R.id.rl_menu_corporativo) RelativeLayout layout;
    @BindView(R.id.tvNombreEstablecimiento) TextView tvNombreEstablecimiento;

    private ProgressDialog pDialog;
    private String codigo="";
    private int dni=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_corporativo);
        iniciarLayout();
        iniciarPDialog();
        tvNombreEstablecimiento.setText(Corporativo.getCorporativo().getLoc_nombre());
        Log.v("Amd", "Id Server: " + Corporativo.getCorporativo().getId_server());
        Log.v("Amd", "Id Evento: " + Corporativo.getCorporativo().getId_evento());
        Log.v("Amd", "Id Local: " + Corporativo.getCorporativo().getId_local());
        Log.v("Amd", "Local Id?: " + Corporativo.getCorporativo().getLoc_id());
    }

    @OnClick(R.id.btnAdministradorVentas)
    public void administradorVentas() {
        startActivity(new Intent(this, VentasCorporativoActivity.class));
    }

    @OnClick(R.id.btnAdministradorLista)
    public void irAEnLista() {
        if (Corporativo.getCorporativo().getId_local() != -1) {
            startActivity(new Intent(this, ListaClientesCorporativoActivity.class));
        }else if(Corporativo.getCorporativo().getId_evento()!=-1){
            startActivity(new Intent(this, ListaClientesCorporativoDetalleActivity.class));
        }
    }

    @OnClick(R.id.btnAdministradorCodigo)
    public void ingresarCodigo() {
        final String datos[] = {"", ""};

        new MaterialInputsDialog(this)
                .addInput(InputType.TYPE_CLASS_TEXT, 0, R.string.codigo)
                .addInput(InputType.TYPE_CLASS_NUMBER, 0, R.string.dni)
                .inputs(new MaterialInputsDialog.InputsCallback() {
                    @Override
                    public void onInputs(MaterialDialog dialog, List<CharSequence> inputs, boolean allInputsValidated) {
                        datos[0] = String.valueOf(inputs.get(0));
                        datos[1] = String.valueOf(inputs.get(1));
                        Log.d(TAG, datos[0]);
                        Log.d(TAG, datos[1]);
                    }
                })
                .title(getString(R.string.ingresar_codigo))
                .positiveText(getString(R.string.aceptar))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (!datos[0].isEmpty() && !datos[1].isEmpty()) { confirmarDialogoDatos(datos[0], datos[1], MenuCorporativo.this); }
                        else { Toast.makeText(getApplicationContext(), "DEBE INGRESAR TODOS LOS DATOS", Toast.LENGTH_LONG).show(); }
                    }
                })
                .negativeText(getString(R.string.cancelar))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .build()
                .show();
    }


    private void confirmarDialogoDatos(final String codigo, final String dni, final Context context) {
        new MaterialDialog.Builder(context)
                .cancelable(false)
                .title(R.string.confirmar_datos)
                .content(codigo+"\n"+dni)
                .positiveText(context.getString(R.string.aceptar))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        Toast.makeText(context, "WIP", Toast.LENGTH_SHORT).show();


                        //resultadoConfirmarTransaccion(context);
                        //requestDetalleVentaLocal(codigo, dni);

                        startActivity(new Intent(MenuCorporativo.this, ListaCanjeCorporativo.class)
                                .putExtra(Constantes.CODIGO, codigo)
                                .putExtra(Constantes.DNI, dni));
                    }
                })
                .negativeText(R.string.cancelar)
                .onNegative(null)
                .build()
                .show();
    }



    /*private void requestDetalleVentaLocal(final String codigo, final String dni) {
        showDialog(pDialog);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constantes.DETALLE_VENTA_LOCAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")){
                                JSONArray jArray = jsonObject.getJSONArray("data");
                                ArrayList<ItemSimple> detalles = new ArrayList<>();
                                for (int i = 0; i < jArray.length(); i++) {
                                    ItemSimple detalle = new ItemSimple();
                                    detalle.setId(jArray.getJSONObject(i).getInt("VENPRO_ID"));
                                    detalle.setTitulo(jArray.getJSONObject(i).getString("PRO_NOMBRE"));
                                    detalle.setTipo(jArray.getJSONObject(i).getInt("VENPRO_ESTADO"));
                                    detalles.add(detalle);
                                }
                                startActivity(new Intent(MenuCorporativo.this, ListaCanjeCorporativo.class)
                                    .putExtra(Constantes.ARRAY_S_DETALLE_CANJE_CORPORATIVO, detalles));
                            }else{
                                new AlertDialog.Builder(MenuCorporativo.this)
                                        .setTitle(getString(R.string.app_name))
                                        .setMessage(jsonObject.getString("message"))
                                        .setCancelable(false)
                                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
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
                params.put("codigo", codigo.toUpperCase().trim());
                params.put("dni", dni.trim());
                params.put("loc_id", String.valueOf(Corporativo.getCorporativo().getId_local()));
                return params;
            }
        };
        Configuracion.getInstance().addToRequestQueue(request, TAG);
    }*/

    private static void resultadoConfirmarTransaccion(Context context) {
        new MaterialDialog.Builder(context)
                .cancelable(false)
                .title(context.getString(R.string.transaccion_aprobada))
                .content("Viernes 14 de Agosto 2015 \n 11:59:10 PM")
                .positiveText(context.getString(R.string.aceptar))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();
    }

    @Override
    public boolean isSesion() {
        return false;
    }

    @Override
    public void iniciarLayout() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_white_24dp));
        usarGlide(this, R.drawable.icono_allin_toolbar, icono_toolbar);
        usarGlide(this, R.drawable.fondo_allin_desenfoquev3, fondo);
        usarGlide(this, R.drawable.logo, ivImagenUsuarioA);
        usarGlide(this, R.drawable.iconoventas, btnAdministradorVentas);
        usarGlide(this, R.drawable.iconolista, btnAdministradorLista);
        usarGlide(this, R.drawable.iconocodigo, btnAdministradorCodigo);
    }

    @Override
    public void iniciarPDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.enviando_peticion));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_corporativo_cerrar) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(getString(R.string.cerrar_sesion))
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Corporativo.cerrarSesion();
                            startActivity(new Intent(MenuCorporativo.this, InicioActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    })
                    .setNegativeButton(R.string.cancelar, null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
