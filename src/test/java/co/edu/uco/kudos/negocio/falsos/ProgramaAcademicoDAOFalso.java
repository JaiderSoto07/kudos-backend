package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.ProgramaAcademicoDAO;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProgramaAcademicoDAOFalso implements ProgramaAcademicoDAO {

    private final List<ProgramaAcademicoEntidad> programas = new ArrayList<>();

    @Override
    public void crear(ProgramaAcademicoEntidad entidad) {
        programas.add(entidad);
    }

    @Override
    public ProgramaAcademicoEntidad consultarPorId(UUID id) {
        for (var programa : programas) {
            if (programa.getId().equals(id)) {
                return programa;
            }
        }
        return new ProgramaAcademicoEntidad.Builder().build();
    }

    @Override
    public List<ProgramaAcademicoEntidad> consultarPorFiltro(ProgramaAcademicoEntidad filtro) {
        return new ArrayList<>(programas);
    }

    @Override
    public List<ProgramaAcademicoEntidad> consultarTodos() {
        return new ArrayList<>(programas);
    }

    @Override
    public List<ProgramaAcademicoEntidad> consultarActivos() {
        var activos = new ArrayList<ProgramaAcademicoEntidad>();
        for (var programa : programas) {
            if (programa.isActivo()) {
                activos.add(programa);
            }
        }
        return activos;
    }

    @Override
    public void actualizar(UUID id, ProgramaAcademicoEntidad entidad) {
        programas.replaceAll(programa -> programa.getId().equals(id) ? entidad : programa);
    }
}
