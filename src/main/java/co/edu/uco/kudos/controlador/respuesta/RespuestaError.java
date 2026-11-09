package co.edu.uco.kudos.controlador.respuesta;

import java.util.List;

public class RespuestaError<D> extends Respuesta<D> {

    protected RespuestaError(List<D> datos, List<String> mensajes) {
        super(datos, mensajes, EstadoRespuesta.ERROR);
    }

    public static RespuestaError<String> crear(String mensaje) {
        return new RespuestaError<>(List.of(), List.of(mensaje));
    }

    public static <D> RespuestaError<D> crear(List<D> datos, List<String> mensajes) {
        return new RespuestaError<>(datos, mensajes);
    }
}
