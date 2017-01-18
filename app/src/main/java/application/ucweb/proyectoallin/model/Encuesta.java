package application.ucweb.proyectoallin.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by ucweb02 on 29/09/2016.
 */
@RealmClass
public class Encuesta extends RealmObject {
    public static final String ID = "id";

    @PrimaryKey
    private long id;
    private String pregunta;

    public static int getUltimoId() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(Encuesta.class).max(ID);
        return number == null ? 0 : number.intValue() + 1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }
}
