package co.edu.uco.kudos.negocio.negocio.reglas.impl.identidad;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.EstadoFacultad;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.List;
import java.util.UUID;

public final class AsegurarAmbitoSegunRolRule implements Rule<Object> {

    private static final List<Rol> ROLES_DE_PROGRAMA = List.of(Rol.ESTUDIANTE, Rol.DOCENTE, Rol.COORDINADOR_PROGRAMA);

    private static final AsegurarAmbitoSegunRolRule INSTANCIA = new AsegurarAmbitoSegunRolRule();

    private AsegurarAmbitoSegunRolRule() {
    }

    public static AsegurarAmbitoSegunRolRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var rol = (Rol) datos[0];
        var idFacultad = UtilId.valorDefecto((UUID) datos[1]);
        var idPrograma = UtilId.valorDefecto((UUID) datos[2]);
        var daoFactory = (DAOFactory) datos[3];

        if (ROLES_DE_PROGRAMA.contains(rol)) {
            validarPrograma(idPrograma, daoFactory);
        } else if (rol == Rol.COORDINADOR_FACULTAD) {
            validarFacultad(idFacultad, daoFactory);
        }
    }

    private void validarPrograma(UUID idPrograma, DAOFactory daoFactory) {
        if (UtilId.esValorDefecto(idPrograma)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.PROGRAMA_OBLIGATORIO);
        }
        var programa = daoFactory.obtenerProgramaAcademicoDAO().consultarPorId(idPrograma);
        if (UtilId.esValorDefecto(programa.getId())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.PROGRAMA_NO_EXISTE);
        }
        if (!programa.isActivo()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.PROGRAMA_INACTIVO);
        }
    }

    private void validarFacultad(UUID idFacultad, DAOFactory daoFactory) {
        if (UtilId.esValorDefecto(idFacultad)) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.FACULTAD_OBLIGATORIA);
        }
        var facultad = daoFactory.obtenerFacultadDAO().consultarPorId(idFacultad);
        if (UtilId.esValorDefecto(facultad.getId())) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.FACULTAD_NO_EXISTE);
        }
        var estado = UtilEnum.obtenerValor(EstadoFacultad.class, facultad.getEstado(), EstadoFacultad.INACTIVA);
        if (!estado.admiteCoordinador()) {
            throw KudosNegocioExcepcion.crear(CatalogoMensajes.Identidad.FACULTAD_NO_ADMITE_COORDINADOR);
        }
    }
}
