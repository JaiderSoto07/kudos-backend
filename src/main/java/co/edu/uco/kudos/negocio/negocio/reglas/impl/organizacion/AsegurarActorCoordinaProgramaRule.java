package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;

import java.util.UUID;

public final class AsegurarActorCoordinaProgramaRule implements Rule<Object> {

    private static final AsegurarActorCoordinaProgramaRule INSTANCIA = new AsegurarActorCoordinaProgramaRule();

    private AsegurarActorCoordinaProgramaRule() {
    }

    public static AsegurarActorCoordinaProgramaRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var asignacionDelActor = (AsignacionRolDominio) datos[0];
        var idPrograma = (UUID) datos[1];

        if (!asignacionDelActor.coordinaPrograma(idPrograma)) {
            throw KudosAutorizacionExcepcion.crear(CatalogoMensajes.Organizacion.ACTOR_NO_COORDINA_PROGRAMA);
        }
    }
}
