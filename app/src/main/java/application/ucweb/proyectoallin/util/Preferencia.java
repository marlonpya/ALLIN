package application.ucweb.proyectoallin.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ucweb03 on 09/01/2017.
 */

public class Preferencia {
    public static final String PREFERENCIA_DEFAULT = "PREFERENCIA_DEFAULT";
    private SharedPreferences preferencia;

    private static final String TOKEN           = "TOKEN";
    private static final String CANTIDAD_TOKEN  = "CANTIDAD_TOKEN";

    public Preferencia(Context context) {
        preferencia = context.getSharedPreferences(PREFERENCIA_DEFAULT, Context.MODE_PRIVATE);
    }

    public String getToken() {
        return preferencia.getString(TOKEN, "");
    }

    public void setToken(String token) {
        preferencia.edit().putString(TOKEN, token).commit();
    }

    public int getCantidadToken() {
        return preferencia.getInt(CANTIDAD_TOKEN, -1);
    }

    public void setCantidadToken(int cantidad) {
        preferencia.edit().putInt(CANTIDAD_TOKEN, cantidad).commit();
    }
}
