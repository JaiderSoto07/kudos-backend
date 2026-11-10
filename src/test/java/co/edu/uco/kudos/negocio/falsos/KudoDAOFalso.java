package co.edu.uco.kudos.negocio.falsos;

import co.edu.uco.kudos.dao.datos.entidad.KudoDAO;
import co.edu.uco.kudos.entidad.KudoEntidad;
import co.edu.uco.kudos.transversal.utilitario.UtilId;
import co.edu.uco.kudos.transversal.utilitario.UtilTexto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KudoDAOFalso implements KudoDAO {

    private final List<KudoEntidad> kudos = new ArrayList<>();

    public List<KudoEntidad> getKudos() {
        return kudos;
    }

    @Override
    public void crear(KudoEntidad entidad) {
        kudos.addFirst(entidad);
    }

    @Override
    public KudoEntidad consultarPorId(UUID id) {
        for (var kudo : kudos) {
            if (kudo.getId().equals(id)) {
                return kudo;
            }
        }
        return new KudoEntidad.Builder().build();
    }

    @Override
    public List<KudoEntidad> consultarPorFiltro(KudoEntidad filtro) {
        var encontrados = new ArrayList<KudoEntidad>();
        for (var kudo : kudos) {
            if (mismoId(filtro.getAutor().getId(), kudo.getAutor().getId())
                    && mismoId(filtro.getDestinatario().getId(), kudo.getDestinatario().getId())
                    && mismoId(filtro.getCategoria().getId(), kudo.getCategoria().getId())
                    && (UtilTexto.getUtilTexto().esVacia(filtro.getEstado()) || filtro.getEstado().equals(kudo.getEstado()))) {
                encontrados.add(kudo);
            }
        }
        return encontrados;
    }

    private boolean mismoId(UUID idFiltro, UUID idKudo) {
        return UtilId.esValorDefecto(idFiltro) || idFiltro.equals(idKudo);
    }

    @Override
    public List<KudoEntidad> consultarTodos() {
        return new ArrayList<>(kudos);
    }

    @Override
    public void actualizar(UUID id, KudoEntidad entidad) {
        kudos.replaceAll(kudo -> kudo.getId().equals(id) ? entidad : kudo);
    }
}
