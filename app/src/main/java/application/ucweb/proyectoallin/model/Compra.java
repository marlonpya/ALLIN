package application.ucweb.proyectoallin.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb02 on 26/09/2016.
 */
public class Compra extends RealmObject {
    public static final String TAG = Compra.class.getSimpleName();
    public static final String ID_COMPRA = "id";
    public static final String FECHA_COMPRA = "fecha_compra";
    public static final String FECHA_VENCIMIENTO = "fecha_vencimiento";
    public static final String LUGAR_COMPRA = "lugar_compra";
    public static final String PRECIO_TOTAL = "precio_total";

    @PrimaryKey
    private long id;
    private String fecha_compra;
    private String fecha_vencimiento;
    private String evento_compra;
    private String drkere_compra;
    private double precio_total;
    private RealmList<Producto> lista_productos;

    public static int getUltimoId() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(Compra.class).max(ID_COMPRA);
        return number == null ? 0 : number.intValue() + 1;
    }

    public static void cargarData() {
        final String[] lista_nombres = {"Botella", "Marlon", "Jesús", "Sacarías", "César", "Junior", "Izaías", "Pedro", "Luis", "Álbaro"};
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

    public String getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(String fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    public String getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public String getEvento_compra() {
        return evento_compra;
    }

    public void setEvento_compra(String evento_compra) {
        this.evento_compra = evento_compra;
    }

    public String getDrkere_compra() {
        return drkere_compra;
    }

    public void setDrkere_compra(String drkere_compra) {
        this.drkere_compra = drkere_compra;
    }

    public double getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(double precio_total) {
        this.precio_total = precio_total;
    }

    public RealmList<Producto> getLista_productos() {
        return lista_productos;
    }

    public void setLista_productos(RealmList<Producto> lista_productos) {
        this.lista_productos = lista_productos;
    }
}
