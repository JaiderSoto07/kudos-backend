package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.CalificacionDAO;
import co.edu.uco.kudos.entidad.CalificacionEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CalificacionDAOFalso implements CalificacionDAO {

    private final List<CalificacionEntidad> calificaciones = new ArrayList<>();

    public List<CalificacionEntidad> getCalificaciones() {
        return calificaciones;
    }

    @Override
    public void crear(CalificacionEntidad entidad) {
        calificaciones.add(entidad);
    }

    @Override
    public CalificacionEntidad consultarPorId(UUID id) {
        for (var calificacion : calificaciones) {
            if (calificacion.getId().equals(id)) {
                return calificacion;
            }
        }
        return new CalificacionEntidad.Builder().build();
    }

    @Override
    public List<CalificacionEntidad> consultarPorFiltro(CalificacionEntidad filtro) {
        var encontradas = new ArrayList<CalificacionEntidad>();
        for (var calificacion : calificaciones) {
            if (mismoId(filtro.getAutor().getId(), calificacion.getAutor().getId())
                    && mismoId(filtro.getKudo().getId(), calificacion.getKudo().getId())
                    && mismoId(filtro.getReplica().getId(), calificacion.getReplica().getId())) {
                encontradas.add(calificacion);
            }
        }
        return encontradas;
    }

    private boolean mismoId(UUID idFiltro, UUID idCalificacion) {
        return UtilId.esValorDefecto(idFiltro) || idFiltro.equals(idCalificacion);
    }

    @Override
    public List<CalificacionEntidad> consultarTodos() {
        return new ArrayList<>(calificaciones);
    }

    @Override
    public void actualizar(UUID id, CalificacionEntidad entidad) {
        calificaciones.replaceAll(calificacion -> calificacion.getId().equals(id) ? entidad : calificacion);
    }
}
