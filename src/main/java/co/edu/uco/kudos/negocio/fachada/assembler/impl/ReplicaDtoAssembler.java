package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.EstadoContenido;
import co.edu.uco.kudos.dominio.KudoDominio;
import co.edu.uco.kudos.dominio.ReplicaDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.ReplicaDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.catalogo.CatalogoMensajes;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class ReplicaDtoAssembler implements DTOAssembler<ReplicaDominio, ReplicaDto> {

    private static final DTOAssembler<ReplicaDominio, ReplicaDto> INSTANCIA = new ReplicaDtoAssembler();

    private ReplicaDtoAssembler() {
    }

    public static DTOAssembler<ReplicaDominio, ReplicaDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public ReplicaDto convertirADTO(ReplicaDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new ReplicaDominio.Builder().build());
        var vetada = dominioSeguro.getEstado() == EstadoContenido.VETADO;
        var dto = new ReplicaDto();
        dto.setId(dominioSeguro.getId());
        dto.setIdKudo(dominioSeguro.getKudo().getId());
        dto.setIdReplicaPadre(dominioSeguro.getIdReplicaPadre());
        dto.setIdAutor(dominioSeguro.getAutor().getId());
        dto.setNombreAutor(dominioSeguro.getAutor().nombreCompleto());
        dto.setContenido(vetada ? CatalogoMensajes.Replica.CONTENIDO_VETADO : dominioSeguro.getContenido());
        dto.setFechaCreacion(dominioSeguro.getFechaCreacion());
        dto.setEstado(UtilEnum.obtenerTexto(dominioSeguro.getEstado()));
        dto.setVetada(vetada);
        dto.setPromedioCalificacion(dominioSeguro.getPromedioCalificacion());
        dto.setCantidadCalificaciones(dominioSeguro.getCantidadCalificaciones());
        return dto;
    }

    @Override
    public ReplicaDominio convertirADominio(ReplicaDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new ReplicaDto());
        return new ReplicaDominio.Builder()
                .id(dtoSeguro.getId())
                .autor(new UsuarioDominio.Builder().id(dtoSeguro.getIdAutor()).build())
                .kudo(new KudoDominio.Builder().id(dtoSeguro.getIdKudo()).build())
                .idReplicaPadre(dtoSeguro.getIdReplicaPadre())
                .contenido(dtoSeguro.getContenido())
                .build();
    }

    @Override
    public List<ReplicaDto> convertirADTO(List<ReplicaDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<ReplicaDominio>());
        var listaDto = new ArrayList<ReplicaDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
