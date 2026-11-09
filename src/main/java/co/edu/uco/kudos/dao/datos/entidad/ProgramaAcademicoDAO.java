package co.edu.uco.kudos.dao.datos.entidad;

import co.edu.uco.kudos.dao.datos.ActualizarDAO;
import co.edu.uco.kudos.dao.datos.ConsultarActivosDAO;
import co.edu.uco.kudos.dao.datos.ConsultarDAO;
import co.edu.uco.kudos.dao.datos.CrearDAO;
import co.edu.uco.kudos.entidad.ProgramaAcademicoEntidad;

import java.util.UUID;

public interface ProgramaAcademicoDAO extends CrearDAO<ProgramaAcademicoEntidad>, ConsultarDAO<ProgramaAcademicoEntidad, UUID>, ConsultarActivosDAO<ProgramaAcademicoEntidad>, ActualizarDAO<ProgramaAcademicoEntidad, UUID> {
}
