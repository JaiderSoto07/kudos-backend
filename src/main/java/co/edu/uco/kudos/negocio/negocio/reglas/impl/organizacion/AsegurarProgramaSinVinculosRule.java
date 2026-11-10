package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.entidad.VinculacionProgramaEntidad;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;

import java.util.UUID;

public final class AsegurarProgramaSinVinculosRule implements Rule<Object> {

    private static final AsegurarProgramaSinVinculosRule INSTANCIA = new AsegurarProgramaSinVinculosRule();

    private AsegurarProgramaSinVinculosRule() {
    }

    public static AsegurarProgramaSinVinculosRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var idPrograma = (UUID) datos[0];
        var daoFactory = (DAOFactory) datos[1];

        var filtro = new VinculacionProgramaEntidad.Builder().programa(new ProgramaAcademicoEntidad.Builder().id(idPrograma).build()).build();
        for (var vinculo : daoFactory.obtenerVinculacionProgramaDAO().consultarPorFiltro(filtro)) {
            if (UtilFecha.esValorDefecto(vinculo.getFechaFin()) && vinculo.getPrograma().getId().equals(idPrograma)) {
                throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.PROGRAMA_CON_VINCULOS);
            }
        }
    }
}
