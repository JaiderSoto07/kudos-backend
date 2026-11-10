package co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion;

import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;

public final class AsegurarAutoridadCompetenteRule implements Rule<Object> {

    private static final AsegurarAutoridadCompetenteRule INSTANCIA = new AsegurarAutoridadCompetenteRule();

    private AsegurarAutoridadCompetenteRule() {
    }

    public static AsegurarAutoridadCompetenteRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var asignacionDelActor = (AsignacionRolDominio) datos[0];
        var solicitud = (SolicitudRegistroDominio) datos[1];

        if (!asignacionDelActor.puedeDecidir(solicitud)) {
            throw KudosAutorizacionExcepcion.crear(CatalogoMensajes.Aprobacion.AUTORIDAD_NO_COMPETENTE);
        }
    }
}
