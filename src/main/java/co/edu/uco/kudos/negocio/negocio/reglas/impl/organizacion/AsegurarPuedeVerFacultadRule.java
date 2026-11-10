package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;

import java.util.UUID;

public final class AsegurarPuedeVerFacultadRule implements Rule<Object> {

    private static final AsegurarPuedeVerFacultadRule INSTANCIA = new AsegurarPuedeVerFacultadRule();

    private AsegurarPuedeVerFacultadRule() {
    }

    public static AsegurarPuedeVerFacultadRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var asignacionDelActor = (AsignacionRolDominio) datos[0];
        var idFacultad = (UUID) datos[1];

        if (!asignacionDelActor.puedeVerFacultad(idFacultad)) {
            throw KudosAutorizacionExcepcion.crear(CatalogoMensajes.Organizacion.FACULTAD_FUERA_DE_AMBITO);
        }
    }
}
