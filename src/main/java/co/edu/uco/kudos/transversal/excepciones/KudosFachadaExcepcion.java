package co.edu.uco.kudos.transversal.excepciones;

import co.edu.uco.kudos.transversal.excepciones.enums.Capa;

import java.io.Serial;

public class KudosFachadaExcepcion extends KudosExcepcion {

    @Serial
    private static final long serialVersionUID = -357429563151592651L;

    private KudosFachadaExcepcion(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        super(Capa.FACHADA, mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }

    public static KudosExcepcion crear(String mensajeUsuario) {
        return new KudosFachadaExcepcion(mensajeUsuario, mensajeUsuario, new Exception(mensajeUsuario));
    }

    public static KudosExcepcion crear(String mensajeUsuario, String mensajeTecnico) {
        return new KudosFachadaExcepcion(mensajeUsuario, mensajeTecnico, new Exception(mensajeTecnico));
    }

    public static KudosExcepcion crear(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        return new KudosFachadaExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }
}
