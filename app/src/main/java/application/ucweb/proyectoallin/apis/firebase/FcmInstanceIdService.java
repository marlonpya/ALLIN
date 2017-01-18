package application.ucweb.proyectoallin.apis.firebase;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import application.ucweb.proyectoallin.aplicacion.Configuracion;
import application.ucweb.proyectoallin.model.Usuario;
import application.ucweb.proyectoallin.util.ConexionBroadcastReceiver;
import application.ucweb.proyectoallin.util.Constantes;
import application.ucweb.proyectoallin.util.Preferencia;

/**
 * Created by ucweb03 on 16/01/2017.
 */

public class FcmInstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = FcmInstanceIdService.class.getSimpleName();
    private String token = "";
    private Preferencia preferencia;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, token);
        preferencia = new Preferencia(this);
        preferencia.setToken(token);

        actualizarToken();
    }

    private void actualizarToken() {
        if (ConexionBroadcastReceiver.isConect()) {
            if (Usuario.getUsuario() != null) {
                if (Usuario.getUsuario().isSesion()) {

                    final String token = preferencia.getToken();
                    final int id_usuario = Usuario.getUsuario().getId_server();

                    StringRequest request = new StringRequest(
                            Request.Method.POST,
                            Constantes.ACTUALIZAR_TOKEN,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d(TAG, jsonObject.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    actualizarToken();
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("id_usuario", String.valueOf(id_usuario));
                            params.put("token", token);
                            return params;
                        }
                    };
                    Configuracion.getInstance().addToRequestQueue(request, TAG);
                }
            }
        }

    }
}
