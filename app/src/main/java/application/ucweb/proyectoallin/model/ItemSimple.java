package application.ucweb.proyectoallin.model;

/**
 * Created by ucweb02 on 21/09/2016.
 */
public class ItemSimple {

    private String titulo;
    private int icono;
    private int id;
    private int tipo;

    public ItemSimple(){}

    public ItemSimple(String titulo, int icono) {
        this.titulo = titulo;
        this.icono = icono;
    }

    //Con id y tipo para listar departamento/provincia/distrito
    public ItemSimple(int id, String titulo, int tipo, int icono) {
        this.id = id;
        this.tipo = tipo;
        this.titulo = titulo;
        this.icono = icono;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
