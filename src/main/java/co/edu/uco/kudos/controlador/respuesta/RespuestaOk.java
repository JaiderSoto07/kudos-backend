package co.edu.uco.kudos.controlador.respuesta;

import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.List;

public class RespuestaOk<D> extends Respuesta<D> {

    protected RespuestaOk(List<D> datos, List<String> mensajes) {
        super(datos, mensajes, EstadoRespuesta.OK);
    }

    public static <D> RespuestaOk<D> crear(List<D> datos, List<String> mensajes) {
        return new RespuestaOk<>(datos, mensajes);
    }

    public static <D> RespuestaOk<D> crear(List<D> datos) {
        return new RespuestaOk<>(datos, List.of());
    }

    public static <D> RespuestaOk<D> crear(String mensaje) {
        var mensajeFormateado = UtilTexto.getUtilTexto().quitarEspaciosEnBlanco(mensaje);
        return new RespuestaOk<>(List.of(), List.of(mensajeFormateado));
    }

    public static <D> RespuestaOk<D> crear(D dato) {
        return new RespuestaOk<>(List.of(dato), List.of());
    }
}
