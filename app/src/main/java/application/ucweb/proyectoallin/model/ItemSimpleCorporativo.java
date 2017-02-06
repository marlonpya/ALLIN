package application.ucweb.proyectoallin.model;

/**
 * Created by ucweb03 on 23/01/2017.
 */

public class ItemSimpleCorporativo {
    public static final int LOCAL = 1;
    public static final int EVENTO = 2;
    public static final int NOT_FOUND = -1;
    private String nombre;
    private int id;
    private int tipo;

    public ItemSimpleCorporativo(String nombre, int id, int tipo) {
        this.nombre = nombre;
        this.id = id;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "ItemSimpleCorporativo{" +
                "nombre='" + nombre + '\'' +
                ", id=" + id +
                ", tipo=" + tipo +
                '}';
    }
}
