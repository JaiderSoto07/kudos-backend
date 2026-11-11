package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.ModeracionDAO;
import co.edu.uco.kudos.entidad.ModeracionEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModeracionDAOFalso implements ModeracionDAO {

    private final List<ModeracionEntidad> moderaciones = new ArrayList<>();

    public List<ModeracionEntidad> getModeraciones() {
        return moderaciones;
    }

    @Override
    public void crear(ModeracionEntidad entidad) {
        moderaciones.add(entidad);
    }

    @Override
    public ModeracionEntidad consultarPorId(UUID id) {
        for (var moderacion : moderaciones) {
            if (moderacion.getId().equals(id)) {
                return moderacion;
            }
        }
        return new ModeracionEntidad.Builder().build();
    }

    @Override
    public List<ModeracionEntidad> consultarPorFiltro(ModeracionEntidad filtro) {
        var encontradas = new ArrayList<ModeracionEntidad>();
        for (var moderacion : moderaciones) {
            if (coincide(filtro.getKudo().getId(), moderacion.getKudo().getId())
                    && coincide(filtro.getReplica().getId(), moderacion.getReplica().getId())
                    && coincideTexto(filtro.getEstado(), moderacion.getEstado())
                    && coincideTexto(filtro.getDecision(), moderacion.getDecision())) {
                encontradas.add(moderacion);
            }
        }
        return encontradas;
    }

    @Override
    public List<ModeracionEntidad> consultarTodos() {
        return new ArrayList<>(moderaciones);
    }

    @Override
    public void actualizar(UUID id, ModeracionEntidad entidad) {
        moderaciones.replaceAll(moderacion -> moderacion.getId().equals(id) ? entidad : moderacion);
    }

    private boolean coincide(UUID idFiltro, UUID idModeracion) {
        return UtilId.esValorDefecto(idFiltro) || idFiltro.equals(idModeracion);
    }

    private boolean coincideTexto(String textoFiltro, String textoModeracion) {
        return UtilTexto.getUtilTexto().esVacia(textoFiltro) || textoFiltro.equals(textoModeracion);
    }
}
