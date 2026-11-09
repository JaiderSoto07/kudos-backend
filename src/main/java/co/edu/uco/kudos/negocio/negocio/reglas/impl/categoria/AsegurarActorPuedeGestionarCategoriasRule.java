package co.edu.uco.kudos.negocio.negocio.reglas.impl.categoria;

import co.edu.uco.kudos.dao.factoria.DAOFactory;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.negocio.negocio.reglas.Rule;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.excepciones.KudosNegocioExcepcion;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilFecha;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.UUID;

public final class AsegurarActorPuedeGestionarCategoriasRule implements Rule<Object> {

    private static final AsegurarActorPuedeGestionarCategoriasRule INSTANCIA = new AsegurarActorPuedeGestionarCategoriasRule();

    private AsegurarActorPuedeGestionarCategoriasRule() {
    }

    public static AsegurarActorPuedeGestionarCategoriasRule obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    public void ejecutar(Object... datos) {
        var idActor = (UUID) datos[0];
        var daoFactory = (DAOFactory) datos[1];

        var filtro = new AsignacionRolEntidad.Builder()
                .usuario(new UsuarioEntidad.Builder().id(idActor).build())
                .build();
        var asignaciones = daoFactory.obtenerAsignacionRolDAO().consultarPorFiltro(filtro);

        for (var asignacion : asignaciones) {
            if (esVigenteYPuedeGestionar(asignacion)) {
                return;
            }
        }
        throw KudosNegocioExcepcion.crear(CatalogoMensajes.CategoriaNegocioImpl.ACTOR_NO_PUEDE_GESTIONAR_CATEGORIAS);
    }

    private boolean esVigenteYPuedeGestionar(AsignacionRolEntidad asignacion) {
        var rol = UtilEnum.obtenerValor(Rol.class, asignacion.getRol(), null);
        return UtilFecha.esValorDefecto(asignacion.getFechaFin())
                && !UtilObjeto.esNulo(rol)
                && rol.puedeGestionarCategorias();
    }
}
