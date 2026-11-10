package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.SolicitudRegistroDAO;
import co.edu.uco.kudos.entidad.SolicitudRegistroEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SolicitudRegistroDAOFalso implements SolicitudRegistroDAO {

    private final List<SolicitudRegistroEntidad> solicitudes = new ArrayList<>();

    public List<SolicitudRegistroEntidad> getSolicitudes() {
        return solicitudes;
    }

    @Override
    public void crear(SolicitudRegistroEntidad entidad) {
        solicitudes.addFirst(entidad);
    }

    @Override
    public SolicitudRegistroEntidad consultarPorId(UUID id) {
        for (var solicitud : solicitudes) {
            if (solicitud.getId().equals(id)) {
                return solicitud;
            }
        }
        return new SolicitudRegistroEntidad.Builder().build();
    }

    @Override
    public List<SolicitudRegistroEntidad> consultarPorFiltro(SolicitudRegistroEntidad filtro) {
        var encontradas = new ArrayList<SolicitudRegistroEntidad>();
        for (var solicitud : solicitudes) {
            var mismoSolicitante = UtilId.esValorDefecto(filtro.getSolicitante().getId())
                    || solicitud.getSolicitante().getId().equals(filtro.getSolicitante().getId());
            var mismoEstado = filtro.getEstado().isEmpty() || solicitud.getEstado().equals(filtro.getEstado());
            if (mismoSolicitante && mismoEstado) {
                encontradas.add(solicitud);
            }
        }
        return encontradas;
    }

    @Override
    public List<SolicitudRegistroEntidad> consultarTodos() {
        return new ArrayList<>(solicitudes);
    }

    @Override
    public void actualizar(UUID id, SolicitudRegistroEntidad entidad) {
        solicitudes.replaceAll(solicitud -> solicitud.getId().equals(id) ? entidad : solicitud);
    }
}
