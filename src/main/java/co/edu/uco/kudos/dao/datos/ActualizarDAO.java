package co.edu.uco.kudos.dao.datos;

public interface ActualizarDAO<E, Id> {

    void actualizar(Id id, E entidad);
}
