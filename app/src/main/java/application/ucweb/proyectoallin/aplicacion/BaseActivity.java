package application.ucweb.proyectoallin.aplicacion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.alamkanak.weekview.WeekViewEvent;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyco.dialog.widget.NormalDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.util.CircleTransform;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by ucweb02 on 21/09/2016.
 */
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    Realm realm;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public static void setToolbarSon(Toolbar toolbar, AppCompatActivity activity, ImageView imageView) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        usarGlide(activity.getApplicationContext(), R.drawable.icono_allin_toolbar, imageView);
    }

    public static void setToolbarSon(Toolbar toolbar, AppCompatActivity activity) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setOverflowIcon(activity.getResources().getDrawable(R.drawable.ic_more_vert_white_24dp));
    }

    public static void setToolbarSonB(Toolbar toolbar, AppCompatActivity activity) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setTitle("");
        toolbar.setOverflowIcon(activity.getResources().getDrawable(R.drawable.ic_more_vert_white_24dp));
    }

    public static void usarGlide(Context context, int rutaIcono, ImageView imageView){
        Glide.with(context)
                .load(rutaIcono)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    public static void usarGlide(Context context, String rutaIcono, ImageView imageView){
        Glide.with(context)
                .load(rutaIcono)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    public static void setFondoActivity(Context context, ImageView imageView) {
        Glide.with(context)
                .load(R.drawable.fondo_allin_desenfoquev3)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }

    public static void setImageConGlideCircular(Context context,ImageView imageView, int ruta_imagen) {
        Glide.with(context.getApplicationContext()).load(ruta_imagen)
                .transform(new CircleTransform(context.getApplicationContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public static void setImageConGlideCircular(Context context,ImageView imageView, String ruta_imagen) {
        Glide.with(context.getApplicationContext()).load(ruta_imagen)
                .transform(new CircleTransform(context.getApplicationContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(imageView);
    }

    public static void setImagenConGlidePrincipal(Context context, ImageView imageView) {
        Glide.with(context)
                .load(R.drawable.fondo_allin_layout)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
    }

    public static void setSpinner(Context context, Spinner spinner, int array) {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(context,array, R.layout.layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public static void setSpinnerRosa(Context context,Spinner spinner,int array) {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(context,array, R.layout.layoutrosa);
        adapter.setDropDownViewResource(R.layout.checkspinnerrosa);
        spinner.setAdapter(adapter);
    }

    public static void setSpinner(Context context, Spinner spinner, List<String> lista) {
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(context, R.layout.layout, lista);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    public static void showDialog(ProgressDialog dialog) {
        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }

    public static void hidepDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public static void errorConexion(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.app_name)
                .setMessage(context.getString(R.string.conexion_error))
                .setPositiveButton(R.string.aceptar, null)
                .show();
    }

    public static void setGlide(Context context, String ruta, ImageView imageView) {
        if (ConexionBroadcastReceiver.isConect())
            Glide.with(context)
                    .load(ruta)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView);
        else
            usarGlide(context, ruta, imageView);
    }

    public static void setGlideCircular(Context context, String ruta, ImageView imageView){
        if (ConexionBroadcastReceiver.isConect())
            Glide.with(context)
                    .load(ruta)
                    .transform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView);
        else
            usarGlide(context, ruta, imageView);
    }
}
