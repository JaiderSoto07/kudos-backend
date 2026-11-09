package co.edu.uco.kudos.transversal.excepciones;

import co.edu.uco.kudos.transversal.excepciones.enums.Capa;

import java.io.Serial;

public class KudosNegocioExcepcion extends KudosExcepcion {

    @Serial
    private static final long serialVersionUID = -760126890392758389L;

    private KudosNegocioExcepcion(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        super(Capa.NEGOCIO, mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }

    public static KudosExcepcion crear(String mensajeUsuario) {
        return new KudosNegocioExcepcion(mensajeUsuario, mensajeUsuario, new Exception(mensajeUsuario));
    }

    public static KudosExcepcion crear(String mensajeUsuario, String mensajeTecnico) {
        return new KudosNegocioExcepcion(mensajeUsuario, mensajeTecnico, new Exception(mensajeTecnico));
    }

    public static KudosExcepcion crear(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        return new KudosNegocioExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }
}
