package co.edu.uco.kudos.negocio.negocio.reglas.impl.calificacion;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

import java.util.Objects;
import java.util.UUID;

public final class AsegurarContenidoAjenoRule implements Rule<UUID> {

    private static final AsegurarContenidoAjenoRule INSTANCIA = new AsegurarContenidoAjenoRule();

    private AsegurarContenidoAjenoRule() {
    }

    public static AsegurarContenidoAjenoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(UUID... datos) {
        if (Objects.equals(datos[0], datos[1])) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Calificacion.CONTENIDO_PROPIO);
        }
    }
}
