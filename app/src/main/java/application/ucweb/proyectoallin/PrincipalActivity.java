package application.ucweb.proyectoallin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import application.ucweb.proyectoallin.apis.FacebookApi;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.fragment.EditarPerfilFragment;
import application.ucweb.proyectoallin.fragment.MenuFragment;
import application.ucweb.proyectoallin.fragment.MisComprasTabFragment;
import application.ucweb.proyectoallin.fragment.MisPuntosFragment;
import application.ucweb.proyectoallin.fragment.NavegadorFragment;
import application.ucweb.proyectoallin.interfaz.IActividad;
import application.ucweb.proyectoallin.model.Producto;
import application.ucweb.proyectoallin.model.Usuario;
import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;

public class PrincipalActivity extends BaseActivity implements NavegadorFragment.FragmentDrawerListener, IActividad {
    public static final String TAG = PrincipalActivity.class.getSimpleName();
    private NavegadorFragment drawerFragment;
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.idiv_layout_principal_usuario) ImageView ivFondoPrincipalU;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    public static int posicion = 0;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        configuracionNavegador();
        iniciarLayout();
        cambiarFragment(0);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        if (isSesion() || position == 0) cambiarFragment(position);
        else new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(getString(R.string.debe_iniciar_sesion))
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(PrincipalActivity.this, RegistroActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    private void cambiarFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:fragment = new MenuFragment();
                                posicion = 0;               break;
            case 1:fragment = new EditarPerfilFragment();
                                posicion = 1;               break;
            case 2:compartir();                             break;
            case 3:fragment = new MisComprasTabFragment();
                                posicion = 1;               break;
            case 4: fragment = new MisPuntosFragment();
                                posicion = 4;               break;
            case 5: cerrarSesionUsuario();                  break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body_fragment, fragment);
            fragmentTransaction.commit();
        }
    }
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (posicion != 0) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container_body_fragment, new MenuFragment());
                transaction.commit();
                posicion = 0;
            } else if (posicion == 0) {
                super.onBackPressed();
            }
        }
    }

    private void cerrarSesionUsuario() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(getString(R.string.cerrar_sesion))
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (FacebookApi.conectado()) FacebookApi.cerrarSesion();
                        Usuario.cerrarSesion();
                        startActivity(new Intent(PrincipalActivity.this, InicioActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }
    @Override
    public void iniciarPDialog() { }

    private void configuracionNavegador() {
        drawerFragment = (NavegadorFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
    }

    private void compartir() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "@ALLINIGHT ");
        startActivity(Intent.createChooser(intent, "Compartir con"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }

    @Override
    public boolean isSesion() {
        return Usuario.getUsuario() != null && Usuario.getUsuario().isSesion();
    }

    @Override
    public void iniciarLayout() {
        usarGlide(this, R.drawable.icono_allin_toolbar, icono_toolbar);
        setFondoActivity(this, ivFondoPrincipalU);
    }
}
