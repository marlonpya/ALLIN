package application.ucweb.proyectoallin.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb03 on 20/01/2017.
 */

public class Corporativo extends RealmObject {
    private static final String TAG = Corporativo.class.getSimpleName();
    private static final String ID = "id";
    public static final int ID_DEFAULT = 1;

    @PrimaryKey
    private long id;
    private int id_server;
    private String nombre;
    private boolean sesion;

    public void iniciarSesion(Corporativo corporativo) {
        Realm realm = Realm.getDefaultInstance();
        Corporativo select_corporativo = realm.where(Corporativo.class).equalTo(ID, ID_DEFAULT).findFirst();
        realm.beginTransaction();
        if (select_corporativo == null) {
            Corporativo insert_corporativo = realm.createObject(Corporativo.class, corporativo.getId());
            insert_corporativo.setId_server(corporativo.getId_server());
            insert_corporativo.setNombre(corporativo.getNombre());
            insert_corporativo.setSesion(true);
            realm.copyToRealm(corporativo);
            Log.d(TAG, insert_corporativo.toString());
        } else {

        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getId_server() {
        return id_server;
    }

    public void setId_server(int id_server) {
        this.id_server = id_server;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isSesion() {
        return sesion;
    }

    public void setSesion(boolean sesion) {
        this.sesion = sesion;
    }
}
