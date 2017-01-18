package application.ucweb.proyectoallin.util;

/**
 * Created by ucweb02 on 21/09/2016.
 */
public class Constantes {
    public static final String DATOS_CMS =      "https://www.dropbox.com/s/38vx50pvnuswvok/DOCUMENTO%20BASE%20DEL%20CMS.docx?dl=0";
    public static final String DATOS_MOVIL =    "https://www.dropbox.com/s/bgndm508qtm1xwd/Flujo%20Final%20(ACTUAL).docx?dl=0";

    //RUTAS SERVICIOS
    public static final String REGISTRAR_USUARIO    = "http://www.uc-web.mobi/Allnight/registrarUsuario";
    public static final String EDITAR_USUARIO       = "http://www.uc-web.mobi/Allnight/editarUsuario";
    public static final String DEPARTAMENTOS        = "http://www.uc-web.mobi/Allnight/obtenerDepartamentos";
    public static final String PROVINCIAS           = "http://www.uc-web.mobi/Allnight/obtenerProvincias"; //dep_id
    public static final String DISTRITOS            = "http://www.uc-web.mobi/Allnight/obtenerDistritos"; //pro_id
    public static final String INICIAR_SESION       = "http://www.uc-web.mobi/Allnight/iniciarSesion";
    public static final String DEPARTAMENTOS_TOTAL  = "http://www.uc-web.mobi/Allnight/obtenerTotalDepartamento";
    public static final String LOCALES              = "http://www.uc-web.mobi/Allnight/api/locales";

    //EXTRAS DE INTENTS
//    public static final String K_S_ENVIO_INICIO = "k_s_envio_inicio";
    public static final String I_TIP_ESTABLECIMIENTO_MAPA = "I_TIP_ESTABLECIMIENTO_MAPA";

    //EXTRA DE LISTADISCOTECA
    public static final String K_I_TIPO_DRKER = "k_i_tipo_drker";
    public static final String K_S_TIPO_EXTRA = "k_s_tipo_extra";

    public static final String K_S_TITULO_TOOLBAR = "k_s_titulo_toolbar";
    public static final String K_L_ID_EVENTO = "k_l_id_evento";

    //DATOS PARA ENVIAR RUTA Y POSICION AL DETALLE DE GALLERÍA
    public static final String K_I_GALLERIA_DATA = "K_I_GALLERIA_DATA";
    public static final String K_I_GALLERIA_POSICION = "K_I_GALLERIA_POSICION";
    public static final String L_ID_ENLISTA = "L_ID_ENLISTA";

    //INTENTS DE ESTABLECIMIENTO DISCOTECAS 1, RESTOBARES 2, KARAOKES 3, EVENTO 4, RECOMENDADOS 5
    public static final String BUSQUEDA_CALENDARIO = "BUSQUEDA_CALENDARIO";
    public static final int I_DISCOTECAS = 1;
    public static final int I_RESTOBARES = 2;
    public static final int I_KARAOKES   = 3;
    public static final int I_EVENTOS    = 4;
    public static final int I_RECOMENDADOS = 5;

    public static final String B_MOSTRAR_PROMOCION = "B_MOSTRAR_PROMOCION";
    public static final String ACTUALIZAR_TOKEN = "http://www.uc-web.mobi/Allnight/editarToken";
}
