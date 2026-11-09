package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.CategoriaDAO;
import co.edu.uco.kudos.entidad.CategoriaEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoriaDAOFalso implements CategoriaDAO {

    private final List<CategoriaEntidad> categorias = new ArrayList<>();

    public List<CategoriaEntidad> getCategorias() {
        return categorias;
    }

    @Override
    public void crear(CategoriaEntidad entidad) {
        categorias.add(entidad);
    }

    @Override
    public CategoriaEntidad consultarPorId(UUID id) {
        for (var categoria : categorias) {
            if (categoria.getId().equals(id)) {
                return categoria;
            }
        }
        return new CategoriaEntidad.Builder().build();
    }

    @Override
    public List<CategoriaEntidad> consultarPorFiltro(CategoriaEntidad filtro) {
        var encontradas = new ArrayList<CategoriaEntidad>();
        for (var categoria : categorias) {
            var cumpleId = UtilId.esValorDefecto(filtro.getId()) || categoria.getId().equals(filtro.getId());
            var cumpleNombre = UtilTexto.getUtilTexto().esVacia(filtro.getNombre()) || categoria.getNombre().equalsIgnoreCase(filtro.getNombre());
            if (cumpleId && cumpleNombre) {
                encontradas.add(categoria);
            }
        }
        return encontradas;
    }

    @Override
    public List<CategoriaEntidad> consultarTodos() {
        return new ArrayList<>(categorias);
    }

    @Override
    public List<CategoriaEntidad> consultarActivos() {
        var activas = new ArrayList<CategoriaEntidad>();
        for (var categoria : categorias) {
            if (categoria.isActiva()) {
                activas.add(categoria);
            }
        }
        return activas;
    }

    @Override
    public void actualizar(UUID id, CategoriaEntidad entidad) {
        categorias.replaceAll(categoria -> categoria.getId().equals(id) ? entidad : categoria);
    }
}
