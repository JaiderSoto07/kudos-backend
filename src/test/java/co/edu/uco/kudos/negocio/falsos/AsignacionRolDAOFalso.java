package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.AsignacionRolDAO;
import co.edu.uco.kudos.entidad.AsignacionRolEntidad;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AsignacionRolDAOFalso implements AsignacionRolDAO {

    private final List<AsignacionRolEntidad> asignaciones = new ArrayList<>();

    @Override
    public void crear(AsignacionRolEntidad entidad) {
        asignaciones.add(entidad);
    }

    @Override
    public AsignacionRolEntidad consultarPorId(UUID id) {
        for (var asignacion : asignaciones) {
            if (asignacion.getId().equals(id)) {
                return asignacion;
            }
        }
        return new AsignacionRolEntidad.Builder().build();
    }

    @Override
    public List<AsignacionRolEntidad> consultarPorFiltro(AsignacionRolEntidad filtro) {
        var encontradas = new ArrayList<AsignacionRolEntidad>();
        for (var asignacion : asignaciones) {
            if (asignacion.getUsuario().getId().equals(filtro.getUsuario().getId())) {
                encontradas.add(asignacion);
            }
        }
        return encontradas;
    }

    @Override
    public List<AsignacionRolEntidad> consultarTodos() {
        return new ArrayList<>(asignaciones);
    }

    @Override
    public void actualizar(UUID id, AsignacionRolEntidad entidad) {
        asignaciones.replaceAll(asignacion -> asignacion.getId().equals(id) ? entidad : asignacion);
    }
}
