package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.NotificacionEntidad;

import java.util.UUID;

public interface NotificacionDAO extends CrearDAO<NotificacionEntidad>, ConsultarDAO<NotificacionEntidad, UUID>, ActualizarDAO<NotificacionEntidad, UUID> {
}
