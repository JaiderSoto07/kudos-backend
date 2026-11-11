package co.edu.uco.kudos.negocio.negocio.reglas.impl.notificacion;

import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;

import java.util.Objects;
import java.util.UUID;

public final class AsegurarNotificacionPropiaRule implements Rule<UUID> {

    private static final AsegurarNotificacionPropiaRule INSTANCIA = new AsegurarNotificacionPropiaRule();

    private AsegurarNotificacionPropiaRule() {
    }

    public static AsegurarNotificacionPropiaRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(UUID... datos) {
        if (!Objects.equals(datos[0], datos[1])) {
            throw KudosAutorizacionExcepcion.crear(CatalogoMensajes.Notificacion.NOTIFICACION_AJENA);
        }
    }
}
