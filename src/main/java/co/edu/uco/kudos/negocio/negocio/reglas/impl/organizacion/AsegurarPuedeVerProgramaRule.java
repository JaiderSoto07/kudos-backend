package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dominio.AsignacionRolDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;

public final class AsegurarPuedeVerProgramaRule implements Rule<Object> {

    private static final AsegurarPuedeVerProgramaRule INSTANCIA = new AsegurarPuedeVerProgramaRule();

    private AsegurarPuedeVerProgramaRule() {
    }

    public static AsegurarPuedeVerProgramaRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var asignacionDelActor = (AsignacionRolDominio) datos[0];
        var programa = (ProgramaAcademicoDominio) datos[1];

        if (!asignacionDelActor.puedeVerPrograma(programa)) {
            throw KudosAutorizacionExcepcion.crear(CatalogoMensajes.Organizacion.PROGRAMA_FUERA_DE_AMBITO);
        }
    }
}
