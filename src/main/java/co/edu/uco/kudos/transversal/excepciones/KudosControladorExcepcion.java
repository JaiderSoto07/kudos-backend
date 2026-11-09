package co.edu.uco.kudos.transversal.excepciones;

import co.edu.uco.kudos.transversal.excepciones.enums.Capa;

import java.io.Serial;

public class KudosControladorExcepcion extends KudosExcepcion {

    @Serial
    private static final long serialVersionUID = 468340549049951257L;

    private KudosControladorExcepcion(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        super(Capa.CONTROLADOR, mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }

    public static KudosExcepcion crear(String mensajeUsuario) {
        return new KudosControladorExcepcion(mensajeUsuario, mensajeUsuario, new Exception(mensajeUsuario));
    }

    public static KudosExcepcion crear(String mensajeUsuario, String mensajeTecnico) {
        return new KudosControladorExcepcion(mensajeUsuario, mensajeTecnico, new Exception(mensajeTecnico));
    }

    public static KudosExcepcion crear(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        return new KudosControladorExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }
}
