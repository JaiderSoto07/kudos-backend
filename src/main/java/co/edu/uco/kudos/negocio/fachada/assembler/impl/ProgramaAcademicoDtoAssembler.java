package co.edu.uco.kudos.negocio.fachada.assembler.impl;

import co.edu.uco.kudos.dominio.FacultadDominio;
import co.edu.uco.kudos.dominio.ProgramaAcademicoDominio;
import co.edu.uco.kudos.dto.ProgramaAcademicoDto;
import co.edu.uco.kudos.negocio.fachada.assembler.DTOAssembler;
import co.edu.uco.kudos.transversal.utilitario.UtilObjeto;

import java.util.ArrayList;
import java.util.List;

public final class ProgramaAcademicoDtoAssembler implements DTOAssembler<ProgramaAcademicoDominio, ProgramaAcademicoDto> {

    private static final DTOAssembler<ProgramaAcademicoDominio, ProgramaAcademicoDto> INSTANCIA = new ProgramaAcademicoDtoAssembler();

    private ProgramaAcademicoDtoAssembler() {
    }

    public static DTOAssembler<ProgramaAcademicoDominio, ProgramaAcademicoDto> getInstance() {
        return INSTANCIA;
    }

    @Override
    public ProgramaAcademicoDto convertirADTO(ProgramaAcademicoDominio dominio) {
        var dominioSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dominio, new ProgramaAcademicoDominio.Builder().build());
        var dto = new ProgramaAcademicoDto();
        dto.setId(dominioSeguro.getId());
        dto.setNombre(dominioSeguro.getNombre());
        dto.setIdFacultad(dominioSeguro.getFacultad().getId());
        dto.setNombreFacultad(dominioSeguro.getFacultad().getNombre());
        dto.setActivo(dominioSeguro.isActivo());
        return dto;
    }

    @Override
    public ProgramaAcademicoDominio convertirADominio(ProgramaAcademicoDto dto) {
        var dtoSeguro = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(dto, new ProgramaAcademicoDto());
        return new ProgramaAcademicoDominio.Builder()
                .id(dtoSeguro.getId())
                .nombre(dtoSeguro.getNombre())
                .facultad(new FacultadDominio.Builder().id(dtoSeguro.getIdFacultad()).nombre(dtoSeguro.getNombreFacultad()).build())
                .activo(dtoSeguro.isActivo())
                .build();
    }

    @Override
    public List<ProgramaAcademicoDto> convertirADTO(List<ProgramaAcademicoDominio> listaDominios) {
        var listaSegura = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(listaDominios, new ArrayList<ProgramaAcademicoDominio>());
        var listaDto = new ArrayList<ProgramaAcademicoDto>();
        for (var dominio : listaSegura) {
            listaDto.add(convertirADTO(dominio));
        }
        return listaDto;
    }
}
