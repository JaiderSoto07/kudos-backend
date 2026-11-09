package co.edu.uco.kudos.controlador.respuesta;

import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Respuesta<D> {

    private final String correlationId;
    private final LocalDateTime timestamp;
    private final List<D> datos;
    private final List<String> mensajes;
    private final EstadoRespuesta estado;

    protected Respuesta(List<D> datos, List<String> mensajes, EstadoRespuesta estado) {
        this.datos = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(datos, new ArrayList<>());
        this.mensajes = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(mensajes, new ArrayList<>());
        this.estado = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(estado, EstadoRespuesta.ERROR);
        this.timestamp = UtilFecha.fechaActual();
        this.correlationId = UtilId.generarIdComoTexto();
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<D> getDatos() {
        return datos;
    }

    public List<String> getMensajes() {
        return mensajes;
    }

    public EstadoRespuesta getEstado() {
        return estado;
    }
}
