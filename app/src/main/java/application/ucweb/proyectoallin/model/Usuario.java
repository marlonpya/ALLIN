package application.ucweb.proyectoallin.model;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ucweb02 on 27/10/2016.
 */
public class Usuario extends RealmObject {
    public static final String TAG = Usuario.class.getSimpleName();
    public static final String ID = "id";
    public static final int ID_DEFAULT = 1;

    @PrimaryKey
    private long id;
    private int id_server;
    private String nombre;
    private String apellido_p;
    private String apellido_m;
    private String fecha_nac;
    private String dni;
    private String sexo;
    private String estado_civil;
    private String departamento;
    private String provincia;
    private String distrito;
    private String direccion;
    private String num_movil;
    private String operador_movil;
    private String tarjeta_credito;
    private String correo;
    private String foto;
    private boolean recibir_oferta;
    private int puntos;
    private boolean sesion;

    public static void iniciarSesion(Usuario usuario) {
        Realm realm = Realm.getDefaultInstance();
        Usuario user_select = realm.where(Usuario.class).equalTo(ID, ID_DEFAULT).findFirst();
        realm.beginTransaction();
        if (user_select == null) {
            Usuario user_insert = realm.createObject(Usuario.class, ID_DEFAULT);
            user_insert.setId_server(usuario.getId_server());
            user_insert.setNombre(usuario.getNombre());
            user_insert.setApellido_p(usuario.getApellido_p());
            user_insert.setApellido_m(usuario.getApellido_m());
            user_insert.setFecha_nac(usuario.getFecha_nac());
            user_insert.setDni(usuario.getDni());
            user_insert.setSexo(usuario.getSexo());
            user_insert.setEstado_civil(usuario.getEstado_civil());
            user_insert.setDepartamento(usuario.getDepartamento());
            user_insert.setProvincia(usuario.getProvincia());
            user_insert.setDistrito(usuario.getDistrito());
            user_insert.setDireccion(usuario.getDireccion());
            user_insert.setNum_movil(usuario.getNum_movil());
            user_insert.setTarjeta_credito(usuario.getTarjeta_credito());
            user_insert.setCorreo(usuario.getCorreo());
            user_insert.setFoto(usuario.getFoto());
            user_insert.setRecibir_oferta(usuario.isRecibir_oferta());
            user_insert.setPuntos(usuario.getPuntos());
            user_insert.setSesion(true);
            user_insert.setOperador_movil(usuario.getOperador_movil());
            realm.copyToRealm(user_insert);
            Log.d(TAG, user_insert.toString());
        } else {
            user_select.setId_server(usuario.getId_server());
            user_select.setNombre(usuario.getNombre());
            user_select.setApellido_p(usuario.getApellido_p());
            user_select.setApellido_m(usuario.getApellido_m());
            user_select.setFecha_nac(usuario.getFecha_nac());
            user_select.setDni(usuario.getDni());
            user_select.setSexo(usuario.getSexo());
            user_select.setEstado_civil(usuario.getEstado_civil());
            user_select.setDepartamento(usuario.getDepartamento());
            user_select.setProvincia(usuario.getProvincia());
            user_select.setDistrito(usuario.getDistrito());
            user_select.setDireccion(usuario.getDireccion());
            user_select.setNum_movil(usuario.getNum_movil());
            user_select.setTarjeta_credito(usuario.getTarjeta_credito());
            user_select.setCorreo(usuario.getCorreo());
            user_select.setFoto(usuario.getFoto());
            user_select.setRecibir_oferta(usuario.isRecibir_oferta());
            user_select.setPuntos(usuario.getPuntos());
            user_select.setSesion(true);
            user_select.setOperador_movil(usuario.getOperador_movil());
            Log.d(TAG, user_select.toString());
        }
        realm.commitTransaction();
        realm.close();
    }

    public static void cerrarSesion() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Usuario usuario = realm.where(Usuario.class).equalTo(ID, ID_DEFAULT).findFirst();
        usuario.setId_server(0);
        usuario.setNombre("");
        usuario.setApellido_p("");
        usuario.setApellido_m("");
        usuario.setFecha_nac("");
        usuario.setSexo("");
        usuario.setEstado_civil("");
        usuario.setDepartamento("");
        usuario.setProvincia("");
        usuario.setDistrito("");
        usuario.setDireccion("");
        usuario.setNum_movil("");
        usuario.setTarjeta_credito("");
        usuario.setCorreo("");
        usuario.setFoto("");
        usuario.setRecibir_oferta(false);
        usuario.setPuntos(0);
        usuario.setSesion(false);
        realm.commitTransaction();
        realm.close();
    }

    public static Usuario getUsuario() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Usuario.class).equalTo(ID, ID_DEFAULT).findFirst();
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido_p() {
        return apellido_p;
    }

    public void setApellido_p(String apellido_p) {
        this.apellido_p = apellido_p;
    }

    public String getApellido_m() {
        return apellido_m;
    }

    public void setApellido_m(String apellido_m) {
        this.apellido_m = apellido_m;
    }

    public String getFecha_nac() {
        return fecha_nac;
    }

    public void setFecha_nac(String fecha_nac) {
        this.fecha_nac = fecha_nac;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEstado_civil() {
        return estado_civil;
    }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNum_movil() {
        return num_movil;
    }

    public void setNum_movil(String num_movil) {
        this.num_movil = num_movil;
    }

    public String getOperador_movil() {
        return operador_movil;
    }

    public void setOperador_movil(String operador_movil) {
        this.operador_movil = operador_movil;
    }

    public String getTarjeta_credito() {
        return tarjeta_credito;
    }

    public void setTarjeta_credito(String tarjeta_credito) {
        this.tarjeta_credito = tarjeta_credito;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isRecibir_oferta() {
        return recibir_oferta;
    }

    public void setRecibir_oferta(boolean recibir_oferta) {
        this.recibir_oferta = recibir_oferta;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public boolean isSesion() {
        return sesion;
    }

    public void setSesion(boolean sesion) {
        this.sesion = sesion;
    }
}
