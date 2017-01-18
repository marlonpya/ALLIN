package application.ucweb.proyectoallin.interfaz;

/**
 * Created by ucweb03 on 17/01/2017.
 */

public interface IActividad {
    /**
     * MÉTODO PARA SABER SI EXISTE UNA SESIÓN
     * @return SESION USUARIO
     */
    boolean isSesion();

    /**
     * MÉTODO PARA INICIAR LAYOUT
     */
    void iniciarLayout();

    /**
     * MÉTODO PARA INICIAR PROGRES DIALOG
     */
    void iniciarPDialog();
}
