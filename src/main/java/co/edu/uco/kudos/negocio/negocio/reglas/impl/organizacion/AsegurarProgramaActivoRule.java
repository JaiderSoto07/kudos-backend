package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

public final class AsegurarProgramaActivoRule implements Rule<ProgramaAcademicoDominio> {

    private static final AsegurarProgramaActivoRule INSTANCIA = new AsegurarProgramaActivoRule();

    private AsegurarProgramaActivoRule() {
    }

    public static AsegurarProgramaActivoRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(ProgramaAcademicoDominio... datos) {
        if (!datos[0].isActivo()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.PROGRAMA_INACTIVO);
        }
    }
}
