package co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion;

import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

public final class AsegurarSolicitudExisteRule implements Rule<SolicitudRegistroDominio> {

    private static final AsegurarSolicitudExisteRule INSTANCIA = new AsegurarSolicitudExisteRule();

    private AsegurarSolicitudExisteRule() {
    }

    public static AsegurarSolicitudExisteRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(SolicitudRegistroDominio... datos) {
        var solicitud = datos[0];

        if (UtilId.esValorDefecto(solicitud.getId())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Aprobacion.SOLICITUD_NO_EXISTE);
        }
    }
}
