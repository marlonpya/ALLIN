package application.ucweb.proyectoallin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.util.MaterialInputsDialog;
import butterknife.BindView;
import butterknife.OnClick;

public class MenuCorporativo extends BaseActivity {
    public static final String TAG = MenuCorporativo.class.getSimpleName();
    @BindView(R.id.idiv_layout_usuario_administrado) ImageView fondo;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.iv_imagen_usuario_administrado)ImageView ivImagenUsuarioA;
    @BindView(R.id.btnAdministradorVentas)ImageView btnAdministradorVentas;
    @BindView(R.id.btnAdministradorLista)ImageView btnAdministradorLista;
    @BindView(R.id.btnAdministradorCodigo)ImageView btnAdministradorCodigo;
    @BindView(R.id.rl_menu_corporativo) RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_corporativo);
        iniciarLayout();
    }

    @OnClick(R.id.btnAdministradorVentas)
    public void administradorVentas() {
        startActivity(new Intent(this, VentasCorporativoActivity.class));
    }

    @OnClick(R.id.btnAdministradorLista)
    public void irAEnLista() {
        startActivity(new Intent(this, ListaClientesCorporativoActivity.class));
    }

    @OnClick(R.id.btnAdministradorCodigo)
    public void ingresarCodigo() {
        final String datos[] = {"", ""};
        new MaterialInputsDialog(this)
                .addInput(InputType.TYPE_CLASS_TEXT, 0, R.string.codigo)
                .addInput(InputType.TYPE_CLASS_TEXT, 0, R.string.dni)
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


    private static void confirmarDialogoDatos(String codigo, String dni, final Context context) {
        new MaterialDialog.Builder(context)
                .cancelable(false)
                .title(R.string.confirmar_datos)
                .content(codigo+"\n"+dni)
                .positiveText(context.getString(R.string.aceptar))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        resultadoConfirmarTransaccion(context);
                    }
                })
                .negativeText(R.string.cancelar)
                .onNegative(null)
                .build()
                .show();
    }

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

    private void iniciarLayout() {
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
}
