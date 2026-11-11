package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.AuditoriaDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.AuditoriaDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class AuditoriaDtoAssembler implements DTOAssembler<AuditoriaDominio, AuditoriaDto> {

    private static final DTOAssembler<AuditoriaDominio, AuditoriaDto> INSTANCIA = new AuditoriaDtoAssembler();

    private AuditoriaDtoAssembler() {
    }

    public static DTOAssembler<AuditoriaDominio, AuditoriaDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public AuditoriaDto convertirADTO(AuditoriaDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new AuditoriaDominio.Builder().build());
        var dto = new AuditoriaDto();
        dto.setId(dominioSeguro.getId());
        dto.setFechaHora(dominioSeguro.getFechaHora());
        dto.setIdActor(dominioSeguro.getActor().getId());
        dto.setNombreActor(dominioSeguro.getActor().nombreCompleto());
        dto.setAccion(dominioSeguro.getAccion());
        dto.setTipoObjeto(dominioSeguro.getTipoObjeto());
        dto.setIdObjeto(dominioSeguro.getIdObjeto());
        dto.setEstadoAnterior(dominioSeguro.getEstadoAnterior());
        dto.setEstadoNuevo(dominioSeguro.getEstadoNuevo());
        dto.setMotivo(dominioSeguro.getMotivo());
        dto.setDetalle(dominioSeguro.getDetalle());
        return dto;
    }

    @Override
    public AuditoriaDominio convertirADominio(AuditoriaDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new AuditoriaDto());
        return new AuditoriaDominio.Builder()
                .id(dtoSeguro.getId())
                .fechaHora(dtoSeguro.getFechaHora())
                .actor(new UsuarioDominio.Builder().id(dtoSeguro.getIdActor()).build())
                .accion(dtoSeguro.getAccion())
                .tipoObjeto(dtoSeguro.getTipoObjeto())
                .idObjeto(dtoSeguro.getIdObjeto())
                .estadoAnterior(dtoSeguro.getEstadoAnterior())
                .estadoNuevo(dtoSeguro.getEstadoNuevo())
                .motivo(dtoSeguro.getMotivo())
                .detalle(dtoSeguro.getDetalle())
                .build();
    }

    @Override
    public List<AuditoriaDto> convertirADTO(List<AuditoriaDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<AuditoriaDominio>());
        var listaDto = new ArrayList<AuditoriaDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
