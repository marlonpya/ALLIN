package application.ucweb.proyectoallin.modelparseable;

/**
 * Created by ucweb02 on 23/02/2017.
 */

public class UsuarioSimple {
    private int idServer;
    private String nombre;
    private String apellido;
    private int dni;

    public UsuarioSimple() {
    }

    public UsuarioSimple(int idServer, String nombre, String apellido, int dni) {
        this.idServer = idServer;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }
}
