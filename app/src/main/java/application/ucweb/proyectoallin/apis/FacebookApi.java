package application.ucweb.proyectoallin.apis;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

/**
 * Created by ucweb03 on 03/01/2017.
 */

public class FacebookApi {

    public static boolean conectado() { return AccessToken.getCurrentAccessToken() != null; }

    public static void cerrarSesion() { LoginManager.getInstance().logOut(); }
}
