package application.ucweb.proyectoallin.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import application.ucweb.proyectoallin.R;

/**
 * Created by ucweb02 on 07/11/2016.
 */
public class Util {

    public static String getPath(Uri uri, Context context) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query( uri, proj, null, null, null );
        assert cursor != null;
        if ( cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow( MediaStore.Images.Media.DATA );
            result = cursor.getString( column_index );
        }
        cursor.close();
        return result;
    }

    /**
     * DEVUELVE LA FECHA POR UN DATEPICKER (DIALOGO DE FECHA)
     * @param picker
     * @return fecha yyyy-MM-dd
     */
    public static String getFecha(DatePicker picker) {
        final Calendar calendar = Calendar.getInstance();
        int day = picker.getDayOfMonth();
        int month = picker.getMonth();
        int year = picker.getYear();
        calendar.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    /**
     * MUESTRA UN DIALOGO DE DATEPICKER
     * @param context PARA MOSTRAR DIÁLOGO
     * @param textView SETEAR EL RESULTADO
     * TRUE SETEA FALSE NO.SETEA
     */
    public static void dialogoFecha(Context context, final TextView textView) {
        View view = View.inflate(context, R.layout.layout_calendario, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.dp_calendario);

        new MaterialDialog.Builder(context)
                .title("Fecha")
                .customView(view, false)
                .positiveText(R.string.aceptar)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        textView.setText(Util.getFecha(datePicker));
                    }
                })
                .negativeText(R.string.cancelar)
                .onNegative(null)
                .show();
    }

}
