package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.CodigoVerificacionDAO;
import co.edu.uco.kudos.entidad.CodigoVerificacionEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CodigoVerificacionDAOFalso implements CodigoVerificacionDAO {

    private final List<CodigoVerificacionEntidad> codigos = new ArrayList<>();

    public List<CodigoVerificacionEntidad> getCodigos() {
        return codigos;
    }

    @Override
    public void crear(CodigoVerificacionEntidad entidad) {
        codigos.addFirst(entidad);
    }

    @Override
    public CodigoVerificacionEntidad consultarPorId(UUID id) {
        for (var codigo : codigos) {
            if (codigo.getId().equals(id)) {
                return codigo;
            }
        }
        return new CodigoVerificacionEntidad.Builder().build();
    }

    @Override
    public List<CodigoVerificacionEntidad> consultarPorFiltro(CodigoVerificacionEntidad filtro) {
        var encontrados = new ArrayList<CodigoVerificacionEntidad>();
        for (var codigo : codigos) {
            var mismoUsuario = UtilId.esValorDefecto(filtro.getUsuario().getId()) || codigo.getUsuario().getId().equals(filtro.getUsuario().getId());
            var mismoCanal = UtilTexto.getUtilTexto().esVacia(filtro.getCanal()) || codigo.getCanal().equals(filtro.getCanal());
            if (mismoUsuario && mismoCanal) {
                encontrados.add(codigo);
            }
        }
        return encontrados;
    }

    @Override
    public List<CodigoVerificacionEntidad> consultarTodos() {
        return new ArrayList<>(codigos);
    }

    @Override
    public void actualizar(UUID id, CodigoVerificacionEntidad entidad) {
        codigos.replaceAll(codigo -> codigo.getId().equals(id) ? entidad : codigo);
    }
}
