package application.ucweb.proyectoallin.modelparseable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ucweb02 on 03/02/2017.
 */

public class ProductoSimple implements Serializable{
    private int idServer;
    private String nombre;
    private BigDecimal precio_normal;
    private BigDecimal precio_allin;
    private int precio_puntos;
    private int stock;
    private int idLocal;
    private int idEvento;
    private String promocion;
    private Date fecha_inicio;
    private Date fecha_fin;
    private String imagen;
    private int tipo;
    private boolean estado;
    private int cantidad;

    private String nombre_evento;
    private String nombre_local;
    private String condiciones;

    public ProductoSimple() {
    }

    public ProductoSimple(int idServer, String nombre, BigDecimal precio_normal, BigDecimal precio_allin, int precio_puntos, int idLocal, int idEvento, int cantidad) {
        this.idServer = idServer;
        this.nombre = nombre;
        this.precio_normal = precio_normal;
        this.precio_allin = precio_allin;
        this.precio_puntos = precio_puntos;
        this.idLocal = idLocal;
        this.idEvento = idEvento;
        this.cantidad = cantidad;
    }

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

    public BigDecimal getPrecio_normal() {
        return precio_normal;
    }

    public void setPrecio_normal(BigDecimal precio_normal) {
        this.precio_normal = precio_normal;
    }

    public BigDecimal getPrecio_allin() {
        return precio_allin;
    }

    public void setPrecio_allin(BigDecimal precio_allin) {
        this.precio_allin = precio_allin;
    }

    public int getPrecio_puntos() {
        return precio_puntos;
    }

    public void setPrecio_puntos(int precio_puntos) {
        this.precio_puntos = precio_puntos;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public String getPromocion() {
        return promocion;
    }

    public void setPromocion(String promocion) {
        this.promocion = promocion;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre_evento() {
        return nombre_evento;
    }

    public void setNombre_evento(String nombre_evento) {
        this.nombre_evento = nombre_evento;
    }

    public String getNombre_local() {
        return nombre_local;
    }

    public void setNombre_local(String nombre_local) {
        this.nombre_local = nombre_local;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }
}
