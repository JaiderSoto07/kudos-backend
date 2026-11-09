package co.edu.uco.kudos.transversal.excepciones;

import co.edu.uco.kudos.transversal.excepciones.enums.Capa;

import java.io.Serial;

public class KudosDominioExcepcion extends KudosExcepcion {

    @Serial
    private static final long serialVersionUID = 608547907304438502L;

    private KudosDominioExcepcion(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        super(Capa.DOMINIO, mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }

    public static KudosExcepcion crear(String mensajeUsuario) {
        return new KudosDominioExcepcion(mensajeUsuario, mensajeUsuario, new Exception(mensajeUsuario));
    }

    public static KudosExcepcion crear(String mensajeUsuario, String mensajeTecnico) {
        return new KudosDominioExcepcion(mensajeUsuario, mensajeTecnico, new Exception(mensajeTecnico));
    }

    public static KudosExcepcion crear(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        return new KudosDominioExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }
}
