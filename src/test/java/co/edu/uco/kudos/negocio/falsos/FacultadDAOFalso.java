package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.FacultadDAO;
import co.edu.uco.kudos.entidad.FacultadEntidad;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FacultadDAOFalso implements FacultadDAO {

    private final List<FacultadEntidad> facultades = new ArrayList<>();

    @Override
    public void crear(FacultadEntidad entidad) {
        facultades.add(entidad);
    }

    @Override
    public FacultadEntidad consultarPorId(UUID id) {
        for (var facultad : facultades) {
            if (facultad.getId().equals(id)) {
                return facultad;
            }
        }
        return new FacultadEntidad.Builder().build();
    }

    @Override
    public List<FacultadEntidad> consultarPorFiltro(FacultadEntidad filtro) {
        var encontradas = new ArrayList<FacultadEntidad>();
        for (var facultad : facultades) {
            if ((filtro.getNombre().isEmpty() || facultad.getNombre().equalsIgnoreCase(filtro.getNombre()))
                    && (filtro.getEstado().isEmpty() || facultad.getEstado().equals(filtro.getEstado()))) {
                encontradas.add(facultad);
            }
        }
        return encontradas;
    }

    @Override
    public List<FacultadEntidad> consultarTodos() {
        return new ArrayList<>(facultades);
    }

    @Override
    public void actualizar(UUID id, FacultadEntidad entidad) {
        facultades.replaceAll(facultad -> facultad.getId().equals(id) ? entidad : facultad);
    }
}
