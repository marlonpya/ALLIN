package application.ucweb.proyectoallin.modelparseable;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ucweb02 on 06/02/2017.
 */

public class ItemCarrito implements Serializable{

    private int idServer;
    private String nombre;
    private double precio_normal;
    private double precio_allin;
    private int precio_puntos;
    private int idLocal;
    private int idEvento;
    private int cantidad;

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public int getPrecio_puntos() {
        return precio_puntos;
    }

    public void setPrecio_puntos(int precio_puntos) {
        this.precio_puntos = precio_puntos;
    }

    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
