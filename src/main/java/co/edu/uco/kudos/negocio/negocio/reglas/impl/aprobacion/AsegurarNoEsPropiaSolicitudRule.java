package co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion;

import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;

import java.util.UUID;

public final class AsegurarNoEsPropiaSolicitudRule implements Rule<Object> {

    private static final AsegurarNoEsPropiaSolicitudRule INSTANCIA = new AsegurarNoEsPropiaSolicitudRule();

    private AsegurarNoEsPropiaSolicitudRule() {
    }

    public static AsegurarNoEsPropiaSolicitudRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var idActor = (UUID) datos[0];
        var solicitud = (SolicitudRegistroDominio) datos[1];

        if (solicitud.getSolicitante().getId().equals(idActor)) {
            throw KudosAutorizacionExcepcion.crear(CatalogoMensajes.Aprobacion.SOLICITUD_PROPIA);
        }
    }
}
