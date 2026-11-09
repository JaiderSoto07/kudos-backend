package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.UsuarioEntidad;

import java.util.UUID;

public interface UsuarioDAO extends CrearDAO<UsuarioEntidad>, ConsultarDAO<UsuarioEntidad, UUID>, ActualizarDAO<UsuarioEntidad, UUID> {
}
