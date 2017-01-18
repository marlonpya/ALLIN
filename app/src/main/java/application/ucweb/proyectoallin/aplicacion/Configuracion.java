package application.ucweb.proyectoallin.aplicacion;

import android.app.Application;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ucweb02 on 22/09/2016.
 */
public class Configuracion extends Application {

    private static final String TAG = Configuration.class.getSimpleName();
    private RequestQueue requestQueue;

    public static Configuracion mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mInstance = this;
        RealmConfiguration configuracionR = new RealmConfiguration
                .Builder(this)
                .modules(Realm.getDefaultModule())
                .name("proyectoallin.db")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuracionR);
    }

    public static synchronized Configuracion getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
