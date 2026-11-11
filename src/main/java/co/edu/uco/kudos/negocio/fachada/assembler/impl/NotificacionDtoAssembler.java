package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.NotificacionDominio;
import co.edu.uco.kudos.dominio.TipoNotificacion;
import co.edu.uco.kudos.dto.NotificacionDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class NotificacionDtoAssembler implements DTOAssembler<NotificacionDominio, NotificacionDto> {

    private static final DTOAssembler<NotificacionDominio, NotificacionDto> INSTANCIA = new NotificacionDtoAssembler();

    private NotificacionDtoAssembler() {
    }

    public static DTOAssembler<NotificacionDominio, NotificacionDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public NotificacionDto convertirADTO(NotificacionDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new NotificacionDominio.Builder().build());
        var dto = new NotificacionDto();
        dto.setId(dominioSeguro.getId());
        dto.setTipo(UtilEnum.obtenerTexto(dominioSeguro.getTipo()));
        dto.setMensaje(dominioSeguro.getMensaje());
        dto.setTipoReferencia(dominioSeguro.getTipoReferencia());
        dto.setIdReferencia(dominioSeguro.getIdReferencia());
        dto.setFechaCreacion(dominioSeguro.getFechaCreacion());
        dto.setLeida(dominioSeguro.isLeida());
        return dto;
    }

    @Override
    public NotificacionDominio convertirADominio(NotificacionDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new NotificacionDto());
        return new NotificacionDominio.Builder()
                .id(dtoSeguro.getId())
                .tipo(UtilEnum.obtenerValor(TipoNotificacion.class, dtoSeguro.getTipo(), null))
                .mensaje(dtoSeguro.getMensaje())
                .tipoReferencia(dtoSeguro.getTipoReferencia())
                .idReferencia(dtoSeguro.getIdReferencia())
                .fechaCreacion(dtoSeguro.getFechaCreacion())
                .leida(dtoSeguro.isLeida())
                .build();
    }

    @Override
    public List<NotificacionDto> convertirADTO(List<NotificacionDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<NotificacionDominio>());
        var listaDto = new ArrayList<NotificacionDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
