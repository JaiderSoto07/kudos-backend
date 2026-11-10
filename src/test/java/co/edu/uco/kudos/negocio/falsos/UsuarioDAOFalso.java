package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.UsuarioDAO;
import co.edu.uco.kudos.entidad.UsuarioEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsuarioDAOFalso implements UsuarioDAO {

    private final List<UsuarioEntidad> usuarios = new ArrayList<>();

    @Override
    public void crear(UsuarioEntidad entidad) {
        usuarios.add(entidad);
    }

    @Override
    public UsuarioEntidad consultarPorId(UUID id) {
        for (var usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return new UsuarioEntidad.Builder().build();
    }

    @Override
    public List<UsuarioEntidad> consultarPorFiltro(UsuarioEntidad filtro) {
        var encontrados = new ArrayList<UsuarioEntidad>();
        for (var usuario : usuarios) {
            if (coincide(filtro.getCorreoInstitucional(), usuario.getCorreoInstitucional())
                    && coincide(filtro.getNumeroCelular(), usuario.getNumeroCelular())) {
                encontrados.add(usuario);
            }
        }
        return encontrados;
    }

    private boolean coincide(String valorFiltro, String valorUsuario) {
        return UtilTexto.getUtilTexto().esVacia(valorFiltro) || valorFiltro.equals(valorUsuario);
    }

    @Override
    public List<UsuarioEntidad> consultarTodos() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public void actualizar(UUID id, UsuarioEntidad entidad) {
        usuarios.replaceAll(usuario -> usuario.getId().equals(id) ? entidad : usuario);
    }
}
