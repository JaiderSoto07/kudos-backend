package co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion;

import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

public final class AsegurarSolicitudPendienteRule implements Rule<SolicitudRegistroDominio> {

    private static final AsegurarSolicitudPendienteRule INSTANCIA = new AsegurarSolicitudPendienteRule();

    private AsegurarSolicitudPendienteRule() {
    }

    public static AsegurarSolicitudPendienteRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(SolicitudRegistroDominio... datos) {
        var solicitud = datos[0];

        if (!solicitud.estaPendiente()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Aprobacion.SOLICITUD_YA_DECIDIDA);
        }
    }
}
