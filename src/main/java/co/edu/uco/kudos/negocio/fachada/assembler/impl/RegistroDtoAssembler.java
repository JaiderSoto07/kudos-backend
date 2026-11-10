package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dominio.SolicitudRegistroDominio;
import co.edu.uco.kudos.dominio.UsuarioDominio;
import co.edu.uco.kudos.dto.RegistroDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilEnum;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class RegistroDtoAssembler implements DTOAssembler<SolicitudRegistroDominio, RegistroDto> {

    private static final DTOAssembler<SolicitudRegistroDominio, RegistroDto> INSTANCIA = new RegistroDtoAssembler();

    private RegistroDtoAssembler() {
    }

    public static DTOAssembler<SolicitudRegistroDominio, RegistroDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public RegistroDto convertirADTO(SolicitudRegistroDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new SolicitudRegistroDominio.Builder().build());
        var dto = new RegistroDto();
        dto.setNombres(dominioSeguro.getSolicitante().getNombres());
        dto.setApellidos(dominioSeguro.getSolicitante().getApellidos());
        dto.setNumeroCelular(dominioSeguro.getSolicitante().getNumeroCelular());
        dto.setRolSolicitado(UtilEnum.obtenerTexto(dominioSeguro.getRolSolicitado()));
        dto.setIdFacultad(dominioSeguro.getFacultad().getId());
        dto.setIdPrograma(dominioSeguro.getPrograma().getId());
        return dto;
    }

    @Override
    public SolicitudRegistroDominio convertirADominio(RegistroDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new RegistroDto());
        var solicitante = new UsuarioDominio.Builder()
                .nombres(dtoSeguro.getNombres())
                .apellidos(dtoSeguro.getApellidos())
                .numeroCelular(dtoSeguro.getNumeroCelular())
                .build();
        return new SolicitudRegistroDominio.Builder()
                .solicitante(solicitante)
                .facultad(new FacultadDominio.Builder().id(dtoSeguro.getIdFacultad()).build())
                .programa(new ProgramaAcademicoDominio.Builder().id(dtoSeguro.getIdPrograma()).build())
                .build();
    }

    @Override
    public List<RegistroDto> convertirADTO(List<SolicitudRegistroDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<SolicitudRegistroDominio>());
        var listaDto = new ArrayList<RegistroDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
