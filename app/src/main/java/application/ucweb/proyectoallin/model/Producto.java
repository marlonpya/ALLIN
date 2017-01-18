package application.ucweb.proyectoallin.model;

import android.util.Log;

import application.ucweb.proyectoallin.R;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

/**
 * Created by ucweb02 on 23/09/2016.
 */
@RealmClass
public class Producto extends RealmObject {
    public static final String TAG = Producto.class.getSimpleName();
    public static final String ID_PRODUCTO = "id";
    public static final String PROMOCION = "promocion";
    public static final String A_CARRITO = "a_carrito";

    @PrimaryKey
    private long id;
    @Required
    private String descripcion_producto;
    private int puntos_allin;
    private int imagen_producto;
    private double precio_normal;
    private double precio_allin;
    private boolean promocion;
    private boolean a_carrito;
    private int cantidad;

    public static int getUltimoId() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(Producto.class).max(ID_PRODUCTO);
        return number == null ? 0 : number.intValue() + 1;
    }

    public static void cargarData() {
        Realm realm = Realm.getDefaultInstance();
        String[] array = new String[]{"1 Mes de GYM FITNESS", "1 Hamburguesa clásica + Papas", "1 Semana en MAGESTA", "1 Noche en el Bar Luna"};
        int[] arrayInt = new int[]{R.drawable.imagegym, R.drawable.imagenhamburguesa, R.drawable.imagenlavado, R.drawable.imagenlicor};
        for (int i = 0; i < array.length; i++) {
            realm.beginTransaction();
            Producto producto = realm.createObject(Producto.class, getUltimoId());
            producto.setImagen_producto(arrayInt[i]);
            producto.setDescripcion_producto(array[i]);
            producto.setPuntos_allin(99);
            producto.setPromocion(false);
            producto.setA_carrito(false);
            producto.setPrecio_allin(100);
            producto.setPrecio_normal(120);
            producto.setCantidad(0);
            realm.copyToRealm(producto);
            realm.commitTransaction();

            Log.d(TAG, producto.toString());
        }

        String[] arrayString1 = new String[]{"Botella Chivas Regal" ,"Balde de 10 cervezas Miler" ,"1 Botella Cuatro Gallos Especial" ,"1 Gran Russkaya"};
        int[] arrayInt2 = new int[]{R.drawable.imagen_chivas, R.drawable.imagen_kit_miler, R.drawable.imagen_cuatro_gallos, R.drawable.imagen_russkaya};
        for (int i = 0; i < arrayString1.length; i++) {
            realm.beginTransaction();
            Producto producto = realm.createObject(Producto.class, getUltimoId());
            producto.setDescripcion_producto(arrayString1[i]);
            producto.setImagen_producto(arrayInt2[i]);
            producto.setPrecio_normal(350);
            producto.setPrecio_allin(262.5);
            producto.setPromocion(true);
            producto.setA_carrito(false);
            producto.setCantidad(0);
            realm.copyToRealm(producto);
            realm.commitTransaction();
            realm.close();
            Log.d(TAG, producto.toString());
        }

        realm.close();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion_producto() {
        return descripcion_producto;
    }

    public void setDescripcion_producto(String descripcion_producto) {
        this.descripcion_producto = descripcion_producto;
    }

    public int getPuntos_allin() {
        return puntos_allin;
    }

    public void setPuntos_allin(int puntos_allin) {
        this.puntos_allin = puntos_allin;
    }

    public int getImagen_producto() {
        return imagen_producto;
    }

    public void setImagen_producto(int imagen_producto) {
        this.imagen_producto = imagen_producto;
    }

    public double getPrecio_normal() {
        return precio_normal;
    }

    public void setPrecio_normal(double precio_normal) {
        this.precio_normal = precio_normal;
    }

    public double getPrecio_allin() {
        return precio_allin;
    }

    public void setPrecio_allin(double precio_allin) {
        this.precio_allin = precio_allin;
    }

    public boolean isPromocion() {
        return promocion;
    }

    public void setPromocion(boolean promocion) {
        this.promocion = promocion;
    }

    public boolean isA_carrito() {
        return a_carrito;
    }

    public void setA_carrito(boolean a_carrito) {
        this.a_carrito = a_carrito;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
