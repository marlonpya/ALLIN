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

public class Provincia extends RealmObject {
    public static final String TAG = Provincia.class.getSimpleName();
    public static final String FOREIGN_KEY_DEPARTAMENTO = "dep_id";

    @PrimaryKey
    private long id;
    private int id_server;
    @Required
    private String nombre;
    private int dep_id;

    public static int getUltimodId() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(Provincia.class).max("id");
        return number == null ? 0 : number.intValue() + 1;
    }

    public static void crearProvincia(Provincia provincia) {
        if (provincia.getDep_id() != 0) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            Provincia insert_provincia = realm.createObject(Provincia.class, getUltimodId());
            insert_provincia.setId_server(provincia.getId_server());
            insert_provincia.setNombre(provincia.getNombre());
            insert_provincia.setDep_id(provincia.getDep_id());
            realm.copyToRealm(insert_provincia);
            realm.commitTransaction();
            realm.close();
            Log.d(TAG, insert_provincia.toString());
        }
    }

    public static List<String> getProvincias(int departamento_id) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Provincia> provincias = realm.where(Provincia.class).equalTo(FOREIGN_KEY_DEPARTAMENTO, departamento_id).findAll();
        List<String> lista = new ArrayList<>();
        for (Provincia item : provincias) {
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

    public int getDep_id() {
        return dep_id;
    }

    public void setDep_id(int dep_id) {
        this.dep_id = dep_id;
    }
}
