package co.edu.uco.kudos.transversal.excepciones;

import co.edu.uco.kudos.transversal.excepciones.enums.Capa;

import java.io.Serial;

public class KudosDtoExcepcion extends KudosExcepcion {

    @Serial
    private static final long serialVersionUID = -113245719726822482L;

    private KudosDtoExcepcion(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        super(Capa.DTO, mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }

    public static KudosExcepcion crear(String mensajeUsuario) {
        return new KudosDtoExcepcion(mensajeUsuario, mensajeUsuario, new Exception(mensajeUsuario));
    }

    public static KudosExcepcion crear(String mensajeUsuario, String mensajeTecnico) {
        return new KudosDtoExcepcion(mensajeUsuario, mensajeTecnico, new Exception(mensajeTecnico));
    }

    public static KudosExcepcion crear(String mensajeUsuario, String mensajeTecnico, Exception excepcionRaiz) {
        return new KudosDtoExcepcion(mensajeUsuario, mensajeTecnico, excepcionRaiz);
    }
}
