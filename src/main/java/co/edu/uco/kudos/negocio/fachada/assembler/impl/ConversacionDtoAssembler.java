package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.ReplicaDominio;
import co.edu.uco.kudos.dto.ReplicaDto;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class ConversacionDtoAssembler {

    private static final ConversacionDtoAssembler INSTANCIA = new ConversacionDtoAssembler();

    private ConversacionDtoAssembler() {
    }

    public static ConversacionDtoAssembler getInstance() {
        return INSTANCIA;
    }

    public List<ReplicaDto> armarArbol(List<ReplicaDominio> replicas) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(replicas, new ArrayList<ReplicaDominio>());
        var nodos = ReplicaDtoAssembler.getInstance().convertirADTO(listaSegura);
        var nodosPorId = new HashMap<UUID, ReplicaDto>();
        for (var nodo : nodos) {
            nodosPorId.put(nodo.getId(), nodo);
        }

        var raices = new ArrayList<ReplicaDto>();
        for (var nodo : nodos) {
            var padre = nodosPorId.get(nodo.getIdReplicaPadre());
            if (padre == null) {
                raices.add(nodo);
            } else {
                padre.getRespuestas().add(nodo);
            }
        }
        return raices;
    }
}
