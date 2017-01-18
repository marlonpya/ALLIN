package application.ucweb.proyectoallin.model.zona;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb03 on 10/01/2017.
 */

public class Departamento extends RealmObject {
    public static final String TAG = Departamento.class.getSimpleName();
    public static final String ID = "id";

    @PrimaryKey
    private long id;
    private String nombre;

    public static void crearDepartamento(Departamento departamento) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Departamento insert_departamento = realm.createObject(Departamento.class, departamento.getId());
        insert_departamento.setNombre(departamento.getNombre());
        realm.copyToRealm(insert_departamento);
        realm.commitTransaction();
        Log.d(TAG, departamento.getNombre());
        Log.d(TAG, String.valueOf(departamento.getId()));
        realm.close();
    }

    public static boolean isEmpty() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Departamento.class).findAll().isEmpty();
    }

    public static List<String> getDepartamentos() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Departamento> departamentos = realm.where(Departamento.class).findAll();
        List<String> lista = new ArrayList<>();
        for (Departamento item : departamentos) {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
