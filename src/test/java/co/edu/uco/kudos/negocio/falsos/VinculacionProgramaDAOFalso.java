package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.VinculacionProgramaDAO;
import co.edu.uco.kudos.entidad.VinculacionProgramaEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VinculacionProgramaDAOFalso implements VinculacionProgramaDAO {

    private final List<VinculacionProgramaEntidad> vinculaciones = new ArrayList<>();

    public List<VinculacionProgramaEntidad> getVinculaciones() {
        return vinculaciones;
    }

    @Override
    public void crear(VinculacionProgramaEntidad entidad) {
        vinculaciones.add(entidad);
    }

    @Override
    public VinculacionProgramaEntidad consultarPorId(UUID id) {
        for (var vinculacion : vinculaciones) {
            if (vinculacion.getId().equals(id)) {
                return vinculacion;
            }
        }
        return new VinculacionProgramaEntidad.Builder().build();
    }

    @Override
    public List<VinculacionProgramaEntidad> consultarPorFiltro(VinculacionProgramaEntidad filtro) {
        var encontradas = new ArrayList<VinculacionProgramaEntidad>();
        for (var vinculacion : vinculaciones) {
            if ((UtilId.esValorDefecto(filtro.getUsuario().getId()) || vinculacion.getUsuario().getId().equals(filtro.getUsuario().getId()))
                    && (UtilId.esValorDefecto(filtro.getPrograma().getId()) || vinculacion.getPrograma().getId().equals(filtro.getPrograma().getId()))) {
                encontradas.add(vinculacion);
            }
        }
        return encontradas;
    }

    @Override
    public List<VinculacionProgramaEntidad> consultarTodos() {
        return new ArrayList<>(vinculaciones);
    }

    @Override
    public void actualizar(UUID id, VinculacionProgramaEntidad entidad) {
        vinculaciones.replaceAll(vinculacion -> vinculacion.getId().equals(id) ? entidad : vinculacion);
    }
}
