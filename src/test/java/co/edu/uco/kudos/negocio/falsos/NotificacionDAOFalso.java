package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.NotificacionDAO;
import co.edu.uco.kudos.entidad.NotificacionEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificacionDAOFalso implements NotificacionDAO {

    private final List<NotificacionEntidad> notificaciones = new ArrayList<>();

    public List<NotificacionEntidad> getNotificaciones() {
        return notificaciones;
    }

    @Override
    public void crear(NotificacionEntidad entidad) {
        notificaciones.addFirst(entidad);
    }

    @Override
    public NotificacionEntidad consultarPorId(UUID id) {
        for (var notificacion : notificaciones) {
            if (notificacion.getId().equals(id)) {
                return notificacion;
            }
        }
        return new NotificacionEntidad.Builder().build();
    }

    @Override
    public List<NotificacionEntidad> consultarPorFiltro(NotificacionEntidad filtro) {
        var encontradas = new ArrayList<NotificacionEntidad>();
        for (var notificacion : notificaciones) {
            var mismoDestinatario = UtilId.esValorDefecto(filtro.getDestinatario().getId())
                    || notificacion.getDestinatario().getId().equals(filtro.getDestinatario().getId());
            var mismoTipo = UtilTexto.getUtilTexto().esVacia(filtro.getTipo()) || notificacion.getTipo().equals(filtro.getTipo());
            if (mismoDestinatario && mismoTipo) {
                encontradas.add(notificacion);
            }
        }
        return encontradas;
    }

    @Override
    public List<NotificacionEntidad> consultarTodos() {
        return new ArrayList<>(notificaciones);
    }

    @Override
    public void actualizar(UUID id, NotificacionEntidad entidad) {
        notificaciones.replaceAll(notificacion -> notificacion.getId().equals(id) ? entidad : notificacion);
    }
}
