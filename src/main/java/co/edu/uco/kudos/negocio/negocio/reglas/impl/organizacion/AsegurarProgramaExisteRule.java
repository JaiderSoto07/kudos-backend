package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

public final class AsegurarProgramaExisteRule implements Rule<ProgramaAcademicoDominio> {

    private static final AsegurarProgramaExisteRule INSTANCIA = new AsegurarProgramaExisteRule();

    private AsegurarProgramaExisteRule() {
    }

    public static AsegurarProgramaExisteRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(ProgramaAcademicoDominio... datos) {
        if (UtilId.esValorDefecto(datos[0].getId())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.PROGRAMA_NO_EXISTE);
        }
    }
}
