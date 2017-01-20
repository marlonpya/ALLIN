package application.ucweb.proyectoallin.model;

import android.util.Log;

import java.io.Serializable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb02 on 21/09/2016.
 */
public class Establecimiento extends RealmObject{
    private static final String TAG = Establecimiento.class.getSimpleName();
    public static final String ID                   = "id";
    public static final String TIPO_EVENTO          = "tipo";

    public static final int DISCOTECA               = 1;
    public static final int RESTOBAR                = 2;
    public static final int KARAOKE                 = 3;
    public static final int RECOMENDADA             = 4;

    @PrimaryKey
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

    public static int getUltimoId() {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(Establecimiento.class).max(ID);
        return number == null ? 0 : number.intValue() + 1;
    }

    public static int getUltimoId(int tipo) {
        Realm realm = Realm.getDefaultInstance();
        Number number = realm.where(Establecimiento.class).equalTo(TIPO_EVENTO, tipo).max(ID);
        return number == null ? 0 : number.intValue();
    }

    public static void insertOrUpdate(Establecimiento establecimiento) {
        Realm realm = Realm.getDefaultInstance();
        Establecimiento select_establecimiento = realm.where(Establecimiento.class).equalTo(ID, establecimiento.getId()).findFirst();
        realm.beginTransaction();
        if (select_establecimiento == null) {
            Establecimiento insert_establecimiento = realm.createObject(Establecimiento.class, establecimiento.getId());
            insert_establecimiento.setId_server(establecimiento.getId_server());
            insert_establecimiento.setImagen(establecimiento.getImagen());
            insert_establecimiento.setNombre(establecimiento.getNombre());
            insert_establecimiento.setDireccion(establecimiento.getDireccion());
            insert_establecimiento.setLatitud(establecimiento.getLatitud());
            insert_establecimiento.setLongitud(establecimiento.getLongitud());
            insert_establecimiento.setAforo(establecimiento.getAforo());
            insert_establecimiento.setNosotros(establecimiento.getNosotros());
            insert_establecimiento.setUrl(establecimiento.getUrl());
            insert_establecimiento.setGay(establecimiento.isGay());
            insert_establecimiento.setFecha_inicio(establecimiento.getFecha_inicio());
            insert_establecimiento.setFecha_inicio(establecimiento.getFecha_fin());
            insert_establecimiento.setDepartamento(establecimiento.getDepartamento());
            insert_establecimiento.setProvincia(establecimiento.getProvincia());
            insert_establecimiento.setDistrito(establecimiento.getDistrito());
            insert_establecimiento.setPlus(establecimiento.isPlus());
            insert_establecimiento.setEstado(establecimiento.isEstado());
            insert_establecimiento.setRazon_social(establecimiento.getRazon_social());
            insert_establecimiento.setRuc(establecimiento.getRuc());
            realm.copyToRealm(insert_establecimiento);
            Log.d(TAG, insert_establecimiento.toString());
        } else {
            select_establecimiento.setId_server(establecimiento.getId_server());
            select_establecimiento.setImagen(establecimiento.getImagen());
            select_establecimiento.setNombre(establecimiento.getNombre());
            select_establecimiento.setDireccion(establecimiento.getDireccion());
            select_establecimiento.setLatitud(establecimiento.getLatitud());
            select_establecimiento.setLongitud(establecimiento.getLongitud());
            select_establecimiento.setAforo(establecimiento.getAforo());
            select_establecimiento.setNosotros(establecimiento.getNosotros());
            select_establecimiento.setUrl(establecimiento.getUrl());
            select_establecimiento.setGay(establecimiento.isGay());
            select_establecimiento.setFecha_inicio(establecimiento.getFecha_inicio());
            select_establecimiento.setFecha_inicio(establecimiento.getFecha_fin());
            select_establecimiento.setDepartamento(establecimiento.getDepartamento());
            select_establecimiento.setProvincia(establecimiento.getProvincia());
            select_establecimiento.setDistrito(establecimiento.getDistrito());
            select_establecimiento.setPlus(establecimiento.isPlus());
            select_establecimiento.setEstado(establecimiento.isEstado());
            select_establecimiento.setRazon_social(establecimiento.getRazon_social());
            select_establecimiento.setRuc(establecimiento.getRuc());
            Log.d(TAG, select_establecimiento.toString());
        }
        realm.commitTransaction();
        realm.close();
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
        return imagen;
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
}
