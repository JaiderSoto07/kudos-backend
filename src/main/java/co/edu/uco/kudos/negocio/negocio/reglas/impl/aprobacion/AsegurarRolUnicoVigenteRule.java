package co.edu.uco.kudos.negocio.negocio.reglas.impl.aprobacion;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;

public final class AsegurarRolUnicoVigenteRule implements Rule<Object> {

    private static final AsegurarRolUnicoVigenteRule INSTANCIA = new AsegurarRolUnicoVigenteRule();

    private AsegurarRolUnicoVigenteRule() {
    }

    public static AsegurarRolUnicoVigenteRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var rol = (Rol) datos[0];
        var daoFactory = (DAOFactory) datos[1];

        if (rol != Rol.RECTOR) {
            return;
        }
        var filtro = new AsignacionRolEntidad.Builder().rol(rol.name()).build();
        for (var asignacion : daoFactory.obtenerAsignacionRolDAO().consultarPorFiltro(filtro)) {
            if (UtilFecha.esValorDefecto(asignacion.getFechaFin())) {
                throw KudosNegocioExcepcion.crear(CatalogoMensajes.Aprobacion.RECTOR_YA_EXISTE);
            }
        }
    }
}
