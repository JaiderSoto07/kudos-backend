package co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion;

import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

public final class AsegurarContenidoReportadoRule implements Rule<EstadoContenido> {

    private static final AsegurarContenidoReportadoRule INSTANCIA = new AsegurarContenidoReportadoRule();

    private AsegurarContenidoReportadoRule() {
    }

    public static AsegurarContenidoReportadoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(EstadoContenido... datos) {
        if (datos[0] == EstadoContenido.EN_REVISION) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Moderacion.REVISION_EN_CURSO);
        }
        if (datos[0] != EstadoContenido.REPORTADO) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Moderacion.CONTENIDO_SIN_REPORTES);
        }
    }
}
