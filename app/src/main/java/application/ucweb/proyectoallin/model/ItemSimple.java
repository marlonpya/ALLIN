package application.ucweb.proyectoallin.model;

/**
 * Created by ucweb02 on 21/09/2016.
 */
public class ItemSimple {

    private String titulo;
    private int icono;

    public ItemSimple(String titulo, int icono) {
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
}
