package co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion;

import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

public final class AsegurarContenidoEnRevisionRule implements Rule<EstadoContenido> {

    private static final AsegurarContenidoEnRevisionRule INSTANCIA = new AsegurarContenidoEnRevisionRule();

    private AsegurarContenidoEnRevisionRule() {
    }

    public static AsegurarContenidoEnRevisionRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(EstadoContenido... datos) {
        if (datos[0] != EstadoContenido.EN_REVISION) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Moderacion.CONTENIDO_NO_EN_REVISION);
        }
    }
}
