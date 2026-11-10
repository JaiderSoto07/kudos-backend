package co.edu.uco.kudos.negocio.negocio.reglas.impl.organizacion;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.entidad.FacultadEntidad;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;

import java.util.UUID;

public final class AsegurarFacultadSinProgramasActivosRule implements Rule<Object> {

    private static final AsegurarFacultadSinProgramasActivosRule INSTANCIA = new AsegurarFacultadSinProgramasActivosRule();

    private AsegurarFacultadSinProgramasActivosRule() {
    }

    public static AsegurarFacultadSinProgramasActivosRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var idFacultad = (UUID) datos[0];
        var daoFactory = (DAOFactory) datos[1];

        var filtro = new ProgramaAcademicoEntidad.Builder().facultad(new FacultadEntidad.Builder().id(idFacultad).build()).build();
        for (var programa : daoFactory.obtenerProgramaAcademicoDAO().consultarPorFiltro(filtro)) {
            if (programa.isActivo() && programa.getFacultad().getId().equals(idFacultad)) {
                throw KudosNegocioExcepcion.crear(CatalogoMensajes.Organizacion.FACULTAD_CON_PROGRAMAS_ACTIVOS);
            }
        }
    }
}
