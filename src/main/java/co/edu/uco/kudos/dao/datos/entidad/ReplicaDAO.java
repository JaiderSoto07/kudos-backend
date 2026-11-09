package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.ReplicaEntidad;

import java.util.UUID;

public interface ReplicaDAO extends CrearDAO<ReplicaEntidad>, ConsultarDAO<ReplicaEntidad, UUID>, ActualizarDAO<ReplicaEntidad, UUID> {
}
