package co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion;

import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

public final class AsegurarContenidoNoVetadoRule implements Rule<EstadoContenido> {

    private static final AsegurarContenidoNoVetadoRule INSTANCIA = new AsegurarContenidoNoVetadoRule();

    private AsegurarContenidoNoVetadoRule() {
    }

    public static AsegurarContenidoNoVetadoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(EstadoContenido... datos) {
        if (datos[0] == EstadoContenido.VETADO) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Moderacion.CONTENIDO_YA_VETADO);
        }
    }
}
