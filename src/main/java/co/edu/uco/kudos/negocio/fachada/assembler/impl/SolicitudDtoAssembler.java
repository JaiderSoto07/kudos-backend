package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.EstadoSolicitud;
import co.edu.uco.kudos.dominio.Rol;
import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.SolicitudDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class SolicitudDtoAssembler implements DTOAssembler<SolicitudRegistroDominio, SolicitudDto> {

    private static final DTOAssembler<SolicitudRegistroDominio, SolicitudDto> INSTANCIA = new SolicitudDtoAssembler();

    private SolicitudDtoAssembler() {
    }

    public static DTOAssembler<SolicitudRegistroDominio, SolicitudDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public SolicitudDto convertirADTO(SolicitudRegistroDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new SolicitudRegistroDominio.Builder().build());
        var dto = new SolicitudDto();
        dto.setId(dominioSeguro.getId());
        dto.setIdSolicitante(dominioSeguro.getSolicitante().getId());
        dto.setNombreSolicitante(dominioSeguro.getSolicitante().nombreCompleto());
        dto.setCorreoSolicitante(dominioSeguro.getSolicitante().getCorreoInstitucional());
        dto.setRolSolicitado(UtilEnum.obtenerTexto(dominioSeguro.getRolSolicitado()));
        dto.setNombreFacultad(dominioSeguro.getFacultad().getNombre());
        dto.setNombrePrograma(dominioSeguro.getPrograma().getNombre());
        dto.setEstado(UtilEnum.obtenerTexto(dominioSeguro.getEstado()));
        dto.setFechaSolicitud(dominioSeguro.getFechaSolicitud());
        dto.setMotivoRechazo(dominioSeguro.getMotivoRechazo());
        return dto;
    }

    @Override
    public SolicitudRegistroDominio convertirADominio(SolicitudDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new SolicitudDto());
        return new SolicitudRegistroDominio.Builder()
                .id(dtoSeguro.getId())
                .solicitante(new UsuarioDominio.Builder().id(dtoSeguro.getIdSolicitante()).build())
                .rolSolicitado(UtilEnum.obtenerValor(Rol.class, dtoSeguro.getRolSolicitado(), Rol.ESTUDIANTE))
                .estado(UtilEnum.obtenerValor(EstadoSolicitud.class, dtoSeguro.getEstado(), EstadoSolicitud.PENDIENTE))
                .fechaSolicitud(dtoSeguro.getFechaSolicitud())
                .motivoRechazo(dtoSeguro.getMotivoRechazo())
                .build();
    }

    @Override
    public List<SolicitudDto> convertirADTO(List<SolicitudRegistroDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<SolicitudRegistroDominio>());
        var listaDto = new ArrayList<SolicitudDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
