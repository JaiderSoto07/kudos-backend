package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.ReporteDAO;
import co.edu.uco.kudos.entidad.ReporteEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReporteDAOFalso implements ReporteDAO {

    private final List<ReporteEntidad> reportes = new ArrayList<>();

    public List<ReporteEntidad> getReportes() {
        return reportes;
    }

    @Override
    public void crear(ReporteEntidad entidad) {
        reportes.add(entidad);
    }

    @Override
    public ReporteEntidad consultarPorId(UUID id) {
        for (var reporte : reportes) {
            if (reporte.getId().equals(id)) {
                return reporte;
            }
        }
        return new ReporteEntidad.Builder().build();
    }

    @Override
    public List<ReporteEntidad> consultarPorFiltro(ReporteEntidad filtro) {
        var encontrados = new ArrayList<ReporteEntidad>();
        for (var reporte : reportes) {
            if (coincide(filtro.getReportante().getId(), reporte.getReportante().getId())
                    && coincide(filtro.getKudo().getId(), reporte.getKudo().getId())
                    && coincide(filtro.getReplica().getId(), reporte.getReplica().getId())
                    && coincide(filtro.getModeracion().getId(), reporte.getModeracion().getId())
                    && (UtilTexto.getUtilTexto().esVacia(filtro.getEstado()) || filtro.getEstado().equals(reporte.getEstado()))) {
                encontrados.add(reporte);
            }
        }
        return encontrados;
    }

    @Override
    public List<ReporteEntidad> consultarTodos() {
        return new ArrayList<>(reportes);
    }

    @Override
    public void actualizar(UUID id, ReporteEntidad entidad) {
        reportes.replaceAll(reporte -> reporte.getId().equals(id) ? entidad : reporte);
    }

    private boolean coincide(UUID idFiltro, UUID idReporte) {
        return UtilId.esValorDefecto(idFiltro) || idFiltro.equals(idReporte);
    }
}
