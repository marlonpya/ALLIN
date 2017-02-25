package application.ucweb.proyectoallin.modelparseable;

import java.io.Serializable;

/**
 * Created by ucweb02 on 16/02/2017.
 */

public class PreguntaSimple implements Serializable{

    private int idServer;
    private int idEncuesta;
    private String pregunta;
    private int respuesta;
    private boolean respondio;

    public PreguntaSimple() {
    }

    public PreguntaSimple(int idServer, int idEncuesta, String pregunta) {
        this.idServer = idServer;
        this.idEncuesta = idEncuesta;
        this.pregunta = pregunta;
    }

    public int getIdServer() {
        return idServer;
    }

    public void setIdServer(int idServer) {
        this.idServer = idServer;
    }

    public int getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(int idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public int getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(int respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isRespondio() {
        return respondio;
    }

    public void setRespondio(boolean respondio) {
        this.respondio = respondio;
    }
}
