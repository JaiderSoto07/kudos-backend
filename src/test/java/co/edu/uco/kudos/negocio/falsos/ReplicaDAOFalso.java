package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.ReplicaDAO;
import co.edu.uco.kudos.entidad.ReplicaEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReplicaDAOFalso implements ReplicaDAO {

    private final List<ReplicaEntidad> replicas = new ArrayList<>();

    public List<ReplicaEntidad> getReplicas() {
        return replicas;
    }

    @Override
    public void crear(ReplicaEntidad entidad) {
        replicas.add(entidad);
    }

    @Override
    public ReplicaEntidad consultarPorId(UUID id) {
        for (var replica : replicas) {
            if (replica.getId().equals(id)) {
                return replica;
            }
        }
        return new ReplicaEntidad.Builder().build();
    }

    @Override
    public List<ReplicaEntidad> consultarPorFiltro(ReplicaEntidad filtro) {
        var encontradas = new ArrayList<ReplicaEntidad>();
        for (var replica : replicas) {
            if (UtilId.esValorDefecto(filtro.getKudo().getId()) || filtro.getKudo().getId().equals(replica.getKudo().getId())) {
                encontradas.add(replica);
            }
        }
        return encontradas;
    }

    @Override
    public List<ReplicaEntidad> consultarTodos() {
        return new ArrayList<>(replicas);
    }

    @Override
    public void actualizar(UUID id, ReplicaEntidad entidad) {
        replicas.replaceAll(replica -> replica.getId().equals(id) ? entidad : replica);
    }
}
