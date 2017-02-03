package application.ucweb.proyectoallin.modelparseable;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import application.ucweb.proyectoallin.model.Establecimiento;

/**
 * Created by ucweb02 on 27/01/2017.
 */

public class EstablecimientoSimple implements Serializable {
    public final String TAG = Establecimiento.class.getSimpleName();
    public static final String ID                   = "id";
    public static final String TIPO_EVENTO          = "tipo";

    public static final int DISCOTECA               = 1;
    public static final int RESTOBAR                = 2;
    public static final int KARAOKE                 = 3;
    public static final int RECOMENDADA             = 4;

    private long id;
    private int id_server;
    private String imagen;
    private String nombre;
    private String nombre_evento;// <---
    private String direccion;
    private double latitud;
    private double longitud;
    private int aforo;
    private String nosotros;
    private String url;
    private boolean gay;
    private String fecha_inicio;
    private String fecha_fin;
    private String departamento;
    private String provincia;
    private String distrito;
    private int tipo;
    private boolean plus;
    private boolean estado;
    private String razon_social;
    private String ruc;
    private boolean lunes;
    private boolean martes;
    private boolean miercoles;
    private boolean jueves;
    private boolean viernes;
    private boolean sabado;
    private boolean domingo;
    private double precio;

    public EstablecimientoSimple() { }

//    private RealmList<Establecimiento> listaEventos2 =new RealmList<Establecimiento>();

    public EstablecimientoSimple(long id, int id_server, String imagen, String nombre, String nombre_evento, String direccion, double latitud, double longitud, int aforo, String nosotros, String url, boolean gay, String fecha_inicio, String fecha_fin, String departamento, String provincia, String distrito, int tipo, boolean plus, boolean estado, String razon_social, String ruc, boolean lunes, boolean martes, boolean miercoles, boolean jueves, boolean viernes, boolean sabado, boolean domingo, double precio) {
        this.id = id;
        this.id_server = id_server;
        this.imagen = imagen;
        this.nombre = nombre;
        this.nombre_evento = nombre_evento;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.aforo = aforo;
        this.nosotros = nosotros;
        this.url = url;
        this.gay = gay;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
        this.tipo = tipo;
        this.plus = plus;
        this.estado = estado;
        this.razon_social = razon_social;
        this.ruc = ruc;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.domingo = domingo;
        this.precio = precio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getId_server() {
        return id_server;
    }

    public void setId_server(int id_server) {
        this.id_server = id_server;
    }

    public String getImagen() {
            return "http://www.uc-web.mobi/Allnight/uploads/locales/"+getId_server()+"/principal.jpg";
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre_evento() {
        return nombre_evento;
    }

    public void setNombre_evento(String nombre_evento) {
        this.nombre_evento = nombre_evento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public int getAforo() {
        return aforo;
    }

    public void setAforo(int aforo) {
        this.aforo = aforo;
    }

    public String getNosotros() {
        return nosotros;
    }

    public void setNosotros(String nosotros) {
        this.nosotros = nosotros;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isGay() {
        return gay;
    }

    public void setGay(boolean gay) {
        this.gay = gay;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isPlus() {
        return plus;
    }

    public void setPlus(boolean plus) {
        this.plus = plus;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public boolean isLunes() {
        return lunes;
    }

    public void setLunes(boolean lunes) {
        this.lunes = lunes;
    }

    public boolean isMartes() {
        return martes;
    }

    public void setMartes(boolean martes) {
        this.martes = martes;
    }

    public boolean isMiercoles() {
        return miercoles;
    }

    public void setMiercoles(boolean miercoles) {
        this.miercoles = miercoles;
    }

    public boolean isJueves() {
        return jueves;
    }

    public void setJueves(boolean jueves) {
        this.jueves = jueves;
    }

    public boolean isViernes() {
        return viernes;
    }

    public void setViernes(boolean viernes) {
        this.viernes = viernes;
    }

    public boolean isSabado() {
        return sabado;
    }

    public void setSabado(boolean sabado) {
        this.sabado = sabado;
    }

    public boolean isDomingo() {
        return domingo;
    }

    public void setDomingo(boolean domingo) {
        this.domingo = domingo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Date getDateInicio(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            d = sdf.parse(getFecha_inicio());

        } catch (ParseException ex) {

        }
        return d;
    }

    public Date getDateFin(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            d = sdf.parse(getFecha_fin());

        } catch (ParseException ex) {

        }
        return d;
    }
}
