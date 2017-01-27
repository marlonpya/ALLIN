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
    public static final int ID_NOT_FOUND = -1;

    @PrimaryKey
    private long id;
    private int id_server;
    private int id_local;
    private int id_evento;
    private String nombre;
    private boolean sesion;
    private int loc_id;
    private String loc_nombre;

    public static void iniciarSesion(Corporativo corporativo) {
        Realm realm = Realm.getDefaultInstance();
        Corporativo select_corporativo = realm.where(Corporativo.class).equalTo(ID, ID_DEFAULT).findFirst();
        realm.beginTransaction();
        if (select_corporativo == null) {
            Corporativo insert_corporativo = realm.createObject(Corporativo.class, ID_DEFAULT);
            insert_corporativo.setId_server(corporativo.getId_server());
            insert_corporativo.setId_local(corporativo.getId_local());
            insert_corporativo.setId_evento(corporativo.getId_evento());
            insert_corporativo.setNombre(corporativo.getNombre());
            insert_corporativo.setSesion(true);
            insert_corporativo.setLoc_id(corporativo.getLoc_id());
            insert_corporativo.setLoc_nombre(corporativo.getLoc_nombre());
            realm.copyToRealm(insert_corporativo);
            Log.d(TAG, insert_corporativo.toString());
        } else {
            select_corporativo.setId_server(corporativo.getId_server());
            select_corporativo.setId_local(corporativo.getId_local());
            select_corporativo.setId_evento(corporativo.getId_evento());
            select_corporativo.setNombre(corporativo.getNombre());
            select_corporativo.setSesion(true);
            select_corporativo.setLoc_id(corporativo.getLoc_id());
            select_corporativo.setLoc_nombre(corporativo.getLoc_nombre());
            Log.d(TAG, select_corporativo.toString());
        }
        realm.commitTransaction();
        realm.close();
    }

    public static void cerrarSesion() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Corporativo corporativo = realm.where(Corporativo.class).equalTo(ID, ID_DEFAULT).findFirst();
        corporativo.setId_server(-1);
        corporativo.setNombre("");
        corporativo.setSesion(false);
        corporativo.setLoc_id(-1);
        corporativo.setLoc_nombre("");
        realm.commitTransaction();
        realm.close();
    }

    public static Corporativo getCorporativo() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Corporativo.class).equalTo(ID, ID_DEFAULT).findFirst();
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

    public int getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
    }

    public int getId_evento() {
        return id_evento;
    }

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
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

    public int getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(int loc_id) {
        this.loc_id = loc_id;
    }

    public String getLoc_nombre() {
        return loc_nombre;
    }

    public void setLoc_nombre(String loc_nombre) {
        this.loc_nombre = loc_nombre;
    }
}
