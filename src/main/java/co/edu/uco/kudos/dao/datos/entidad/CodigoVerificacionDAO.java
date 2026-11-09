package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.CodigoVerificacionEntidad;

import java.util.UUID;

public interface CodigoVerificacionDAO extends CrearDAO<CodigoVerificacionEntidad>, ConsultarDAO<CodigoVerificacionEntidad, UUID>, ActualizarDAO<CodigoVerificacionEntidad, UUID> {
}
