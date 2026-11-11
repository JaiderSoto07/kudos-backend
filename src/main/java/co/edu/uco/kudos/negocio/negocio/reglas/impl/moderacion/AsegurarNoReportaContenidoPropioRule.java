package co.edu.uco.kudos.negocio.negocio.reglas.impl.moderacion;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

import java.util.Objects;
import java.util.UUID;

public final class AsegurarNoReportaContenidoPropioRule implements Rule<UUID> {

    private static final AsegurarNoReportaContenidoPropioRule INSTANCIA = new AsegurarNoReportaContenidoPropioRule();

    private AsegurarNoReportaContenidoPropioRule() {
    }

    public static AsegurarNoReportaContenidoPropioRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(UUID... datos) {
        if (Objects.equals(datos[0], datos[1])) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Moderacion.CONTENIDO_PROPIO);
        }
    }
}
