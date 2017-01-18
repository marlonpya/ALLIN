package application.ucweb.proyectoallin.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by ucweb02 on 31/10/2016.
 */
@RealmClass
public class EnLista extends RealmObject {
    public static final String TAG = EnLista.class.getSimpleName();
    public static final String ID = "id";

    @PrimaryKey
    private long id;
    private String fecha;
    private RealmList<Cliente> clientes;

    public static int getUltimoId() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(EnLista.class).max(ID);
        return number == null ? 0 : number.intValue() + 1;
    }

    public static void cargarData() {
        final String[] lista_nombres = {"Kevin", "Marlon", "Jesús", "Sacarías", "César", "Junior", "Izaías", "Pedro", "Luis", "Álbaro"};
        Realm realm = Realm.getDefaultInstance();
        for (int i = 0; i < 30; i++) {
            realm.beginTransaction();
            EnLista item = realm.createObject(EnLista.class);
            item.setId(getUltimoId());
            item.setFecha(i+" DE NOVIEMBRE DEL 2016");
            realm.copyToRealm(item);
            realm.commitTransaction();
            Log.d(TAG, item.toString());
            for (int j = 0; j < lista_nombres.length; j++) {
                realm.beginTransaction();
                Cliente cliente = realm.createObject(Cliente.class);
                cliente.setId(Cliente.getUltimoId());
                cliente.setNombre(lista_nombres[j]);
                cliente.setDni("6548787"+j);
                realm.copyToRealmOrUpdate(cliente);
                Log.d(TAG, cliente.toString());

                item.setClientes(new RealmList<Cliente>(cliente));
                realm.commitTransaction();
            }
        }
        realm.close();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public RealmList<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(RealmList<Cliente> clientes) {
        this.clientes = clientes;
    }
}
