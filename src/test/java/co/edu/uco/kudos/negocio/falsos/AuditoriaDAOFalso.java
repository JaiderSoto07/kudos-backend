package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.AuditoriaDAO;
import co.edu.uco.kudos.entidad.AuditoriaEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuditoriaDAOFalso implements AuditoriaDAO {

    private final List<AuditoriaEntidad> registros = new ArrayList<>();

    public List<AuditoriaEntidad> getRegistros() {
        return registros;
    }

    @Override
    public void crear(AuditoriaEntidad entidad) {
        registros.add(entidad);
    }

    @Override
    public AuditoriaEntidad consultarPorId(UUID id) {
        for (var registro : registros) {
            if (registro.getId().equals(id)) {
                return registro;
            }
        }
        return new AuditoriaEntidad.Builder().build();
    }

    @Override
    public List<AuditoriaEntidad> consultarPorFiltro(AuditoriaEntidad filtro) {
        var encontrados = new ArrayList<AuditoriaEntidad>();
        for (var registro : registros) {
            if ((UtilTexto.getUtilTexto().esVacia(filtro.getAccion()) || filtro.getAccion().equals(registro.getAccion()))
                    && (UtilTexto.getUtilTexto().esVacia(filtro.getTipoObjeto()) || filtro.getTipoObjeto().equals(registro.getTipoObjeto()))) {
                encontrados.add(registro);
            }
        }
        return encontrados;
    }

    @Override
    public List<AuditoriaEntidad> consultarTodos() {
        return new ArrayList<>(registros);
    }
}
