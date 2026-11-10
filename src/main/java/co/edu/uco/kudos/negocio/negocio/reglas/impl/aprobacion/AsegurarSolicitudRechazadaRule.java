package co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion;

import co.edu.uco.kudos.dominio.EstadoSolicitud;
import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

public final class AsegurarSolicitudRechazadaRule implements Rule<SolicitudRegistroDominio> {

    private static final AsegurarSolicitudRechazadaRule INSTANCIA = new AsegurarSolicitudRechazadaRule();

    private AsegurarSolicitudRechazadaRule() {
    }

    public static AsegurarSolicitudRechazadaRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(SolicitudRegistroDominio... datos) {
        var ultimaSolicitud = datos[0];

        if (UtilId.esValorDefecto(ultimaSolicitud.getId()) || ultimaSolicitud.getEstado() != EstadoSolicitud.RECHAZADA) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Aprobacion.SOLICITUD_NO_RECHAZADA);
        }
    }
}
