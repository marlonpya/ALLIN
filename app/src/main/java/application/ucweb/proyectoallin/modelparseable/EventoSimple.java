package application.ucweb.proyectoallin.modelparseable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ucweb03 on 27/01/2017.
 */

public class EventoSimple implements Serializable {
    //tipo
    public static final int OTROS       = 0;
    public static final int FIESTA      = 1;
    public static final int CONCIERTO   = 2;
    public static final int TODOS       = 3;

    private int id_server;
    private String nombre;
    private String imagen;
    private double precio;
    private Date fecha_inicio;
    private double latitud;
    private double longitud;
    private int tipo;
    private int id_local;
    private String nombre_local;
    private String direccion;
    private String descripcion;
    private int aforo;

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

    public String getImagen() {
        return "http://www.uc-web.mobi/Allnight/uploads/eventos/"+getId_server()+"/principal.jpg";
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
    }

    public String getNombre_local() {
        return nombre_local;
    }

    public void setNombre_local(String nombre_local) {
        this.nombre_local = nombre_local;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getAforo() {
        return aforo;
    }

    public void setAforo(int aforo) {
        this.aforo = aforo;
    }

    @Override
    public String toString() {
        return "EventoSimple{" +
                "id_server=" + id_server +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", fecha_inicio=" + fecha_inicio +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", id_local=" + id_local +
                '}';
    }
}
