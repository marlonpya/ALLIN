package application.ucweb.proyectoallin.dialogo;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import application.ucweb.proyectoallin.R;
import application.ucweb.proyectoallin.aplicacion.BaseActivity;

/**
 * Created by ucweb02 on 03/11/2016.
 */
public class ImagenGrandeDialogo {

    public static void dialogoEvento(Context context, String imagen) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialogo_ver_mapa_evento, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.ivDialogoMapaEvento);
        ImageView cerrar = (ImageView) view.findViewById(R.id.ivCerrarMapaEvento);
        //BaseActivity.usarGlide(context, imagen, imageView);
        Glide.with(context).load(imagen).into(imageView);
        final AlertDialog dialogo = new AlertDialog.Builder(context)
                .setView(view)
                .show();

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });
    }
}
