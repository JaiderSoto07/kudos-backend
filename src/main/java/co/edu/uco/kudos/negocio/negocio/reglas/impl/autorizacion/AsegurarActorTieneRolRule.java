package co.edu.uco.kudos.negocio.negocio.reglas.impl.autorizacion;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.excepciones.KudosAutorizacionExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.List;
import java.util.UUID;

public final class AsegurarActorTieneRolRule implements Rule<Object> {

    private static final AsegurarActorTieneRolRule INSTANCIA = new AsegurarActorTieneRolRule();

    private AsegurarActorTieneRolRule() {
    }

    public static AsegurarActorTieneRolRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var idActor = (UUID) datos[0];
        var daoFactory = (DAOFactory) datos[1];
        var rolesPermitidos = (List<?>) datos[2];
        var mensajeRechazo = (String) datos[3];

        var filtro = new AsignacionRolEntidad.Builder()
                .usuario(new UsuarioEntidad.Builder().id(idActor).build())
                .build();
        var asignaciones = daoFactory.obtenerAsignacionRolDAO().consultarPorFiltro(filtro);

        for (var asignacion : asignaciones) {
            if (esVigenteYPermitida(asignacion, rolesPermitidos)) {
                return;
            }
        }
        throw KudosAutorizacionExcepcion.crear(mensajeRechazo);
    }

    private boolean esVigenteYPermitida(AsignacionRolEntidad asignacion, List<?> rolesPermitidos) {
        var rol = UtilEnum.obtenerValor(Rol.class, asignacion.getRol(), null);
        return UtilFecha.esValorDefecto(asignacion.getFechaFin())
                && !UtilObjeto.esNulo(rol)
                && rolesPermitidos.contains(rol);
    }
}
