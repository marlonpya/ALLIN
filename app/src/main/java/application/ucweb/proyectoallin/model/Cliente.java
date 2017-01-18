package application.ucweb.proyectoallin.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by ucweb02 on 31/10/2016.
 */
@RealmClass
public class Cliente extends RealmObject {
    public static final String ID = "id";

    @PrimaryKey
    private long id;
    private String nombre;
    private String dni;

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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public static long getUltimoId() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(Cliente.class).max(ID);
        return number == null ? 0 : number.intValue() + 1;
    }
}
