package application.ucweb.proyectoallin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import application.ucweb.proyectoallin.adapter.EventoListaRapidaRealmAdapter;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;
import application.ucweb.proyectoallin.model.Establecimiento;
import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class ListaRapidaActivity extends BaseActivity {
    public static final String TAG = ListaRapidaActivity.class.getSimpleName();
    @BindView(R.id.sontoolbar) Toolbar toolbar;
    @BindView(R.id.tvTituloSonToolbar) ImageView icono_toolbar;
    @BindView(R.id.tvDescripcionToolbar) TextView tvListaRapida;
    @BindView(R.id.lstListaRapida)RealmRecyclerView lst_eventos;
    //@BindView(R.id.spRegistro)Spinner spRegistro;
    @BindView(R.id.idiv_layout_lista_rapida) ImageView fondo;
    private Realm realm;
    private RealmResults<Establecimiento> listaRapidaEventos;
    private EventoListaRapidaRealmAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_rapida);
        iniciarLayout();
        if (Establecimiento.getUltimoId() == 0) {
            //cargarDataListaRapida(); 
            new AlertDialog.Builder(ListaRapidaActivity.this)
                    .setTitle(R.string.app_name)
                    .setMessage(getString(R.string.no_hay_lista_rapida))
                    .setCancelable(false)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            onBackPressed();
                        }})
                    .show();
        }
        else {
            cargarRealmListas();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarRealmListas();
    }


    private void cargarRealmListas() {
        realm = Realm.getDefaultInstance();
        listaRapidaEventos = realm.where(Establecimiento.class).findAll();
        adapter = new EventoListaRapidaRealmAdapter(this, listaRapidaEventos, true, true);
        lst_eventos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void iniciarLayout() {
        setToolbarSon(toolbar, this, icono_toolbar);
        //setSpinner(this, spRegistro, R.array.arrayFiltrarDiscoteca);
        setFondoActivity(this, fondo);
        tvListaRapida.setText(R.string.lista_rapida);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { onBackPressed(); }
        return super.onOptionsItemSelected(item);
    }

}
