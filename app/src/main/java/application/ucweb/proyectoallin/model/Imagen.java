package application.ucweb.proyectoallin.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb02 on 29/09/2016.
 */
public class Imagen extends RealmObject {
    public static final String ID = "id";

    @PrimaryKey
    private long id;
    private String ruta;

    public static int getUltimoId() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(Imagen.class).max(ID);
        return number == null ? 0 : number.intValue() + 1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
