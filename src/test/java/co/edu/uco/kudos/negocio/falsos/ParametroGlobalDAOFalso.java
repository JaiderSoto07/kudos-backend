package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.ParametroGlobalDAO;
import co.edu.uco.kudos.entidad.ParametroGlobalEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParametroGlobalDAOFalso implements ParametroGlobalDAO {

    private final List<ParametroGlobalEntidad> parametros = new ArrayList<>();

    @Override
    public void crear(ParametroGlobalEntidad entidad) {
        parametros.add(entidad);
    }

    @Override
    public ParametroGlobalEntidad consultarPorId(UUID id) {
        for (var parametro : parametros) {
            if (parametro.getId().equals(id)) {
                return parametro;
            }
        }
        return new ParametroGlobalEntidad.Builder().build();
    }

    @Override
    public List<ParametroGlobalEntidad> consultarPorFiltro(ParametroGlobalEntidad filtro) {
        var encontrados = new ArrayList<ParametroGlobalEntidad>();
        for (var parametro : parametros) {
            if (UtilTexto.getUtilTexto().esVacia(filtro.getClave()) || parametro.getClave().equals(filtro.getClave())) {
                encontrados.add(parametro);
            }
        }
        return encontrados;
    }

    @Override
    public List<ParametroGlobalEntidad> consultarTodos() {
        return new ArrayList<>(parametros);
    }

    @Override
    public void actualizar(UUID id, ParametroGlobalEntidad entidad) {
        parametros.replaceAll(parametro -> parametro.getId().equals(id) ? entidad : parametro);
    }
}
