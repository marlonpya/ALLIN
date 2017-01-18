package application.ucweb.proyectoallin.model.zona;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by ucweb03 on 10/01/2017.
 */

public class Distrito extends RealmObject {
    public static final String FOREIGN_KEY_PROVINCIA = "pro_id";

    @PrimaryKey
    private long id;
    private int id_server;
    @Required
    private String nombre;
    private int pro_id;

    public static int getUltimoId() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(Distrito.class).max("id");
        return number == null ? 0 : number.intValue() + 1;
    }

    public static void crearDistrito(Distrito distrito) {
        if (distrito.getPro_id() != 0) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            Distrito insert_distrito = realm.createObject(Distrito.class, getUltimoId());
            insert_distrito.setId_server(distrito.getId_server());
            insert_distrito.setNombre(distrito.getNombre());
            insert_distrito.setPro_id(distrito.getPro_id());
            realm.copyToRealm(insert_distrito);
            realm.commitTransaction();
            realm.close();
            Log.d("Distrito", insert_distrito.toString());
        }
    }

    public static List<String> getDistritos(int id_provincia) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Distrito> distritos = realm.where(Distrito.class).equalTo(FOREIGN_KEY_PROVINCIA, id_provincia).findAll();
        List<String> lista = new ArrayList<>();
        for (Distrito item : distritos) {
            lista.add(item.getNombre());
        }
        return lista;
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

    public int getPro_id() {
        return pro_id;
    }

    public void setPro_id(int pro_id) {
        this.pro_id = pro_id;
    }
}
